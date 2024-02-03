package com.frontendAttic.service.impl;

import com.frontendAttic.entity.constants.Constants;
import com.frontendAttic.entity.dto.ImportErrorItem;
import com.frontendAttic.entity.dto.UserAdminSessionDto;
import com.frontendAttic.entity.enums.*;
import com.frontendAttic.entity.po.Category;
import com.frontendAttic.entity.po.ExamQuestion;
import com.frontendAttic.entity.po.ExamQuestionItem;
import com.frontendAttic.entity.query.ExamQuestionItemQuery;
import com.frontendAttic.entity.query.ExamQuestionQuery;
import com.frontendAttic.entity.query.SimplePage;
import com.frontendAttic.entity.vo.PageResultVO;
import com.frontendAttic.exception.BusinessException;
import com.frontendAttic.mappers.ExamQuestionItemMapper;
import com.frontendAttic.mappers.ExamQuestionMapper;
import com.frontendAttic.service.CategoryService;
import com.frontendAttic.service.ExamQuestionService;
import com.frontendAttic.utils.ExcelUtils;
import com.frontendAttic.utils.StringUtil;
import com.frontendAttic.utils.VerifyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 考试题目 业务接口实现
 */
@Service("examQuestionService")
public class ExamQuestionServiceImpl implements ExamQuestionService {

    @Resource
    private ExamQuestionMapper<ExamQuestion, ExamQuestionQuery> examQuestionMapper;

    @Resource
    private ExamQuestionItemMapper<ExamQuestionItem, ExamQuestionItemQuery> examQuestionItemMapper;

    @Resource
    private CategoryService categoryService;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<ExamQuestion> findListByParam(ExamQuestionQuery param) {
        return this.examQuestionMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(ExamQuestionQuery param) {
        return this.examQuestionMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PageResultVO<ExamQuestion> findListByPage(ExamQuestionQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<ExamQuestion> list = this.findListByParam(param);
        PageResultVO<ExamQuestion> result = new PageResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(ExamQuestion bean) {
        return this.examQuestionMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<ExamQuestion> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.examQuestionMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<ExamQuestion> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.examQuestionMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(ExamQuestion bean, ExamQuestionQuery param) {
        StringUtil.checkParam(param);
        return this.examQuestionMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(ExamQuestionQuery param) {
        StringUtil.checkParam(param);
        return this.examQuestionMapper.deleteByParam(param);
    }

    /**
     * 根据QuestionId获取对象
     */
    @Override
    public ExamQuestion getExamQuestionByQuestionId(Integer questionId) {
        return this.examQuestionMapper.selectByQuestionId(questionId);
    }

    /**
     * 根据QuestionId修改
     */
    @Override
    public Integer updateExamQuestionByQuestionId(ExamQuestion bean, Integer questionId) {
        return this.examQuestionMapper.updateByQuestionId(bean, questionId);
    }

    /**
     * 根据QuestionId删除
     */
    @Override
    public Integer deleteExamQuestionByQuestionId(Integer questionId) {
        return this.examQuestionMapper.deleteByQuestionId(questionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExamQuestion(ExamQuestion examQuestion, List<ExamQuestionItem> examQuestionItemList, Boolean superAdmin) {
        Category category = categoryService.getCategoryByCategoryId(examQuestion.getCategoryId());
        if (category == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        examQuestion.setCategoryName(category.getCategoryName());

        Integer questionId = examQuestion.getQuestionId();
        if (null == questionId) {
            examQuestion.setCreateTime(new Date());
            this.examQuestionMapper.insert(examQuestion);
        } else {
            examQuestion.setQuestionType(null);
            ExamQuestion dbInfo = this.examQuestionMapper.selectByQuestionId(questionId);
            if (!dbInfo.getCreateUserId().equals(examQuestion.getCreateUserId()) && !superAdmin) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            examQuestion.setCreateUserId(null);
            examQuestion.setCreateUserName(null);
            examQuestion.setCreateTime(null);
            this.examQuestionMapper.updateByQuestionId(examQuestion, questionId);
        }
        examQuestionItemList.forEach(item -> {
            item.setQuestionId(examQuestion.getQuestionId());
        });

        /**
         * 更新的选项
         */
        List<ExamQuestionItem> updateItemList = examQuestionItemList.stream().filter(item -> item.getItemId() != null).collect(Collectors.toList());

        /**
         * 新增的选项
         */
        List<ExamQuestionItem> addItemList = examQuestionItemList.stream().filter(item -> item.getItemId() == null).collect(Collectors.toList());


        Map<Integer, ExamQuestionItem> paramItemMap = updateItemList.stream().collect(Collectors.toMap(ExamQuestionItem::getItemId, Function.identity(), (data1, data2) -> data2));


        ExamQuestionItemQuery itemQuery = new ExamQuestionItemQuery();
        itemQuery.setQuestionId(questionId);
        List<ExamQuestionItem> dbItemList = examQuestionItemMapper.selectList(itemQuery);

        List<Integer> delList = new ArrayList<>();
        if (!paramItemMap.isEmpty()) {
            for (ExamQuestionItem db : dbItemList) {
                if (paramItemMap.get(db.getItemId()) == null) {
                    delList.add(db.getItemId());
                }
            }
        }

        /**
         * 新增
         */
        if (!addItemList.isEmpty()) {
            examQuestionItemMapper.insertBatch(addItemList);
        }

        /**
         * 修改
         */
        for (ExamQuestionItem item : updateItemList) {
            examQuestionItemMapper.updateByItemId(item, item.getItemId());
        }

        /**
         * 删除的
         */
        if (!delList.isEmpty()) {
            examQuestionItemMapper.deleteBatch(delList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delExamQuestionBatch(String questionIds, Integer userId) {
        String[] questionIdArray = questionIds.split(",");
        if (userId != null) {
            ExamQuestionQuery infoQuery = new ExamQuestionQuery();
            infoQuery.setQuestionIds(questionIdArray);
            List<ExamQuestion> questionList = this.examQuestionMapper.selectList(infoQuery);
            List<ExamQuestion> currentUserDataList = questionList.stream().filter(a -> !a.getCreateUserId().equals(String.valueOf(userId))).collect(Collectors.toList());
            if (!currentUserDataList.isEmpty()) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }

        examQuestionItemMapper.deleteBatchByQuestionId(questionIdArray, PostStatusEnum.NO_POST.getStatus(), userId);

        ExamQuestionQuery examQuestionQuery = new ExamQuestionQuery();
        examQuestionQuery.setQuestionIds(questionIdArray);
        examQuestionQuery.setStatus(PostStatusEnum.NO_POST.getStatus());
        examQuestionMapper.deleteByParam(examQuestionQuery);
    }

    @Override
    public ExamQuestion showDetailNext(ExamQuestionQuery query, Integer nextType, Integer currentId) {
        if (nextType == null) {
            query.setQuestionId(currentId);
        } else {
            query.setNextType(nextType);
            query.setCurrentId(currentId);
        }
        ExamQuestion examQuestion = examQuestionMapper.showDetailNext(query);

        if (examQuestion == null && nextType == null) {
            throw new BusinessException("内容不存在");
        } else if (examQuestion == null && nextType == -1) {
            throw new BusinessException("已经是第一条了");
        } else if (examQuestion == null && nextType == 1) {
            throw new BusinessException("已经是最后一条了");
        }

        ExamQuestionItemQuery itemQuery = new ExamQuestionItemQuery();
        itemQuery.setQuestionId(examQuestion.getQuestionId());
        itemQuery.setOrderBy("sort asc");
        List<ExamQuestionItem> questionItemList = examQuestionItemMapper.selectList(itemQuery);
        examQuestion.setQuestionItemList(questionItemList);
        return examQuestion;
    }

    @Override
    public List<ImportErrorItem> importExamQuestion(MultipartFile file, UserAdminSessionDto userAdminDto) {
        List<Category> categoryList = categoryService.loadAllCategoryByType(CategoryTypeEnum.EXAM.getType());
        Map<String, Category> categoryMap = categoryList.stream().collect(Collectors.toMap(Category::getCategoryName, Function.identity(), (data1, data2) -> data2));
        List<List<String>> dataList = ExcelUtils.readExcel(file, Constants.EXCEL_TITLE_EXAM_QUESTION, 1);

        /**
         * 错误列
         */
        List<ImportErrorItem> errorList = new ArrayList<>();
        /**
         * 数据列
         */
        List<ExamQuestion> examQuestionList = new ArrayList<>();

        Integer dataRowNum = 2;
        for (List<String> row : dataList) {
            if (errorList.size() > Constants.LENGTH_50) {
                throw new BusinessException("错误数据超过" + Constants.LENGTH_50 + "行，请认真检查数据后再导入");
            }
            dataRowNum++;

            List<String> errorItemList = new ArrayList<>();
            Integer index = 0;
            String title = row.get(index++);
            if (StringUtil.isEmpty(title) || title.length() > Constants.LENGTH_150) {
                errorItemList.add("标题不能为空，且长度不能超过" + Constants.LENGTH_150);
            }
            String categoryName = row.get(index++);
            Category category = categoryMap.get(categoryName);
            if (category == null) {
                errorItemList.add("分类名称不存在");
            }
            String diffficultyLevel = row.get(index++);
            Integer diffficultyLevelInt = null;
            if (VerifyUtils.verify(VerifyRegexEnum.POSITIVE_INTEGER, diffficultyLevel)) {
                diffficultyLevelInt = Integer.parseInt(diffficultyLevel);
                if (diffficultyLevelInt > 5) {
                    errorItemList.add("难度只能是1到5的数字");
                }
            } else {
                errorItemList.add("难度必须是正整数");
            }

            /**
             * 问题类型
             */
            String questionType = row.get(index++);
            QuestionTypeEnum typeEnum = QuestionTypeEnum.getByDesc(questionType);
            if (typeEnum == null) {
                errorItemList.add("问题类型错误");
            }

            /**
             * 问题选项
             */
            String questionItem = row.get(index++);
            if (typeEnum != null && typeEnum != QuestionTypeEnum.TRUE_FALSE && StringUtil.isEmpty(questionItem)) {
                errorItemList.add("问题选项不能为空");
            }
            List<String> examQuestionTitleList = new ArrayList<>();
            if (!StringUtil.isEmpty(questionItem)) {
                examQuestionTitleList = Arrays.asList(questionItem.split("\\n"));
                /**
                 * 去除空格
                 */
                examQuestionTitleList = examQuestionTitleList.stream().filter(item -> !StringUtil.isEmpty(item.trim())).collect(Collectors.toList());
            }
            /**
             * 答案
             */
            String questionAnswer = row.get(index++);
            if (!StringUtil.isEmpty(questionAnswer) && typeEnum != null) {
                switch (typeEnum) {
                    case TRUE_FALSE:
                        if (Constants.TRUE_STR.equals(questionAnswer)) {
                            questionAnswer = Constants.ONE_STR;
                        } else if (Constants.FALSE_STR.equals(questionAnswer)) {
                            questionAnswer = Constants.ZERO_STR;
                        } else {
                            errorItemList.add("判断题答案只能是正确或者错误");
                        }
                        break;
                    case RADIO:
                        Integer radioAnswerIndex = Arrays.binarySearch(Constants.LETTERS, questionAnswer);
                        if (radioAnswerIndex >= 0 && radioAnswerIndex < examQuestionTitleList.size()) {
                            questionAnswer = String.valueOf(radioAnswerIndex);
                        } else {
                            errorItemList.add("题目答案不正确");
                        }
                        break;
                    case CHECK_BOX:
                        StringBuilder answerBuilder = new StringBuilder();
                        questionAnswer = questionAnswer.replace("\n", "");
                        String[] answerArray = questionAnswer.split("、");
                        Boolean answerOk = true;
                        for (String answerItem : answerArray) {
                            answerItem = answerItem.trim();
                            Integer checkBoxAnswerIndex = Arrays.binarySearch(Constants.LETTERS, answerItem);
                            if (checkBoxAnswerIndex >= 0 && checkBoxAnswerIndex < examQuestionTitleList.size()) {
                                answerBuilder.append(checkBoxAnswerIndex).append(",");
                            } else {
                                answerOk = false;
                                break;
                            }
                        }
                        if (answerOk) {
                            questionAnswer = answerBuilder.toString();
                            questionAnswer = questionAnswer.substring(0, questionAnswer.lastIndexOf(","));
                        } else {
                            errorItemList.add("题目答案不正确");
                        }
                        break;
                }
            } else if (StringUtil.isEmpty(questionAnswer)) {
                errorItemList.add("答案不能为空");
            }

            String question = row.get(index++);
            String answerAnalysis = row.get(index++);
            if (StringUtil.isEmpty(answerAnalysis)) {
                errorItemList.add("答案解析不能为空");
            }

            if (!errorItemList.isEmpty() || !errorList.isEmpty()) {
                ImportErrorItem errorItem = new ImportErrorItem();
                errorItem.setRowNum(dataRowNum);
                errorItem.setErrorItemList(errorItemList);
                errorList.add(errorItem);
                continue;
            }

            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setTitle(title);
            examQuestion.setCategoryId(category.getCategoryId());
            examQuestion.setCategoryName(category.getCategoryName());
            examQuestion.setDifficultyLevel(diffficultyLevelInt);
            examQuestion.setQuestionType(typeEnum.getType());
            examQuestion.setQuestionAnswer(questionAnswer);
            examQuestion.setQuestion(question);
            examQuestion.setCreateTime(new Date());
            examQuestion.setStatus(PostStatusEnum.NO_POST.getStatus());
            examQuestion.setAnswerAnalysis(answerAnalysis);
            examQuestion.setCreateUserId(String.valueOf(userAdminDto.getUserId()));
            examQuestion.setCreateUserName(userAdminDto.getUserName());
            /**
             * 问题选项
             */
            Collections.sort(examQuestionTitleList);
            List<ExamQuestionItem> itemList = new ArrayList<>();
            Integer sort = 0;
            for (String titleItem : examQuestionTitleList) {
                ExamQuestionItem examQuestionItem = new ExamQuestionItem();
                titleItem = titleItem.substring(titleItem.indexOf("、") + 1);
                examQuestionItem.setTitle(titleItem);
                examQuestionItem.setSort(++sort);
                itemList.add(examQuestionItem);
            }
            examQuestion.setQuestionItemList(itemList);
            examQuestionList.add(examQuestion);
        }

        if (examQuestionList.isEmpty()) {
            return errorList;
        }
        this.examQuestionMapper.insertBatch(examQuestionList);
        List<ExamQuestionItem> allExamQuestionItemList = new ArrayList<>();
        for (ExamQuestion examQuestion : examQuestionList) {
            for (ExamQuestionItem item : examQuestion.getQuestionItemList()) {
                item.setQuestionId(examQuestion.getQuestionId());
            }
            allExamQuestionItemList.addAll(examQuestion.getQuestionItemList());
        }
        if (!allExamQuestionItemList.isEmpty()) {
            this.examQuestionItemMapper.insertBatch(allExamQuestionItemList);
        }
        return errorList;
    }
}