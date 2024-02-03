package com.frontendAttic.service.impl;


import com.frontendAttic.entity.constants.Constants;
import com.frontendAttic.entity.dto.ImportErrorItem;
import com.frontendAttic.entity.dto.UserAdminSessionDto;
import com.frontendAttic.entity.enums.*;
import com.frontendAttic.entity.po.Category;
import com.frontendAttic.entity.po.Question;
import com.frontendAttic.entity.query.QuestionQuery;
import com.frontendAttic.entity.query.SimplePage;
import com.frontendAttic.entity.vo.PageResultVO;
import com.frontendAttic.exception.BusinessException;
import com.frontendAttic.mappers.CommonMapper;
import com.frontendAttic.mappers.QuestionMapper;
import com.frontendAttic.service.CategoryService;
import com.frontendAttic.service.QuestionService;
import com.frontendAttic.utils.ExcelUtils;
import com.frontendAttic.utils.StringUtil;
import com.frontendAttic.utils.VerifyUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 问题 业务接口实现
 */
@Service("QuestionService")
public class QuestionServiceImpl implements QuestionService {

    @Resource
    private QuestionMapper<Question, QuestionQuery> questionMapper;

    @Resource
    private CategoryService categoryService;

    @Resource
    private CommonMapper commonMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Question> findListByParam(QuestionQuery param) {
        return questionMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(QuestionQuery param) {
        return questionMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PageResultVO<Question> findListByPage(QuestionQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSizeEnum.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<Question> list = this.findListByParam(param);
        PageResultVO<Question> result = new PageResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Question bean) {
        return questionMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Question> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return questionMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Question> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return questionMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(Question bean, QuestionQuery param) {
        StringUtil.checkParam(param);
        return questionMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(QuestionQuery param) {
        StringUtil.checkParam(param);
        return questionMapper.deleteByParam(param);
    }

    /**
     * 根据QuestionId获取对象
     */
    @Override
    public Question getQuestionByQuestionId(Integer questionId) {
        return questionMapper.selectByQuestionId(questionId);
    }

    /**
     * 根据QuestionId修改
     */
    @Override
    public Integer updateQuestionByQuestionId(Question bean, Integer questionId) {
        return questionMapper.updateByQuestionId(bean, questionId);
    }

    /**
     * 根据QuestionId删除
     */
    @Override
    public Integer deleteQuestionByQuestionId(Integer questionId) {
        return questionMapper.deleteByQuestionId(questionId);
    }

    @Override
    public void saveQuestion(Question Question, Boolean isAdmin) {
        Category category = categoryService.getCategoryByCategoryId(Question.getCategoryId());
        Question.setCategoryName(category.getCategoryName());
        if (null == Question.getQuestionId()) {
            Question.setCreateTime(new Date());
            questionMapper.insert(Question);
        } else {
            Question dbInfo = questionMapper.selectByQuestionId(Question.getQuestionId());
            if (!dbInfo.getCreateUserId().equals(Question.getCreateUserId()) && !isAdmin) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            Question.setCreateUserId(null);
            Question.setCreateUserName(null);
            Question.setCreateTime(null);
            questionMapper.updateByQuestionId(Question, Question.getQuestionId());
        }
    }

    @Override
    public void delQuestionBatch(String questionIds, Integer userId) {
        String[] questionIdArray = questionIds.split(",");
        if (userId != null) {
            QuestionQuery infoQuery = new QuestionQuery();
            infoQuery.setQuestionIds(questionIdArray);
            List<Question> QuestionList = questionMapper.selectList(infoQuery);
            List<Question> currentUserDataList = QuestionList.stream().filter(a -> !a.getCreateUserId().equals(String.valueOf(userId))).collect(Collectors.toList());
            if (!currentUserDataList.isEmpty()) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }
        questionMapper.deleteBatchByQuestionId(questionIdArray, PostStatusEnum.NO_POST.getStatus(), userId);
    }

    @Override
    public List<ImportErrorItem> importQuestion(MultipartFile file, UserAdminSessionDto userAdminDto) {
        List<Category> categoryList = categoryService.loadAllCategoryByType(CategoryTypeEnum.QUESTION.getType());
        Map<String, Category> categoryMap = categoryList.stream().collect(Collectors.toMap(Category::getCategoryName, Function.identity(), (data1, data2) -> data2));
        List<List<String>> dataList = ExcelUtils.readExcel(file, Constants.EXCEL_TITLE_QUESTION, 1);

        /**
         * 错误列
         */
        List<ImportErrorItem> errorList = new ArrayList<>();
        /**
         * 数据列
         */
        List<Question> questionList = new ArrayList<>();

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

            Question Question = new Question();
            Question.setTitle(title);
            Question.setCategoryId(category.getCategoryId());
            Question.setCategoryName(category.getCategoryName());
            Question.setDifficultyLevel(diffficultyLevelInt);
            Question.setQuestion(question);
            Question.setAnswerAnalysis(answerAnalysis);
            Question.setCreateTime(new Date());
            Question.setStatus(PostStatusEnum.NO_POST.getStatus());
            Question.setCreateUserId(String.valueOf(userAdminDto.getUserId()));
            Question.setCreateUserName(userAdminDto.getUserName());
            questionList.add(Question);
        }
        if (questionList.isEmpty()) {
            return errorList;
        }
        questionMapper.insertBatch(questionList);
        return errorList;
    }

    @Override
    public Question showDetailNext(QuestionQuery query, Integer nextType, Integer currentId, Boolean updateReadCount) {
        if (nextType == null) {
            query.setQuestionId(currentId);
        } else {
            query.setNextType(nextType);
            query.setCurrentId(currentId);
        }
        Question Question = questionMapper.showDetailNext(query);
        if (Question == null && nextType == null) {
            throw new BusinessException("内容不存在");
        } else if (Question == null && nextType == -1) {
            throw new BusinessException("已经是第一条了");
        } else if (Question == null && nextType == 1) {
            throw new BusinessException("已经是最后一条了");
        }
        if (updateReadCount && Question != null) {
            commonMapper.updateCount(Constants.TABLE_NAME_QUESTION_INFO, 1, null, currentId);
            Question.setReadCount((Question.getReadCount() == null ? 0 : Question.getReadCount()) + 1);
        }
        return Question;
    }
}