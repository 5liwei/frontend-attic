package com.frontendAttic.controller;


import com.frontendAttic.annotation.GlobalInterceptor;
import com.frontendAttic.annotation.VerifyParam;
import com.frontendAttic.entity.dto.ImportErrorItem;
import com.frontendAttic.entity.dto.UserAdminSessionDto;
import com.frontendAttic.entity.enums.PermissionCodeEnum;
import com.frontendAttic.entity.enums.PostStatusEnum;
import com.frontendAttic.entity.enums.QuestionTypeEnum;
import com.frontendAttic.entity.enums.ResponseCodeEnum;
import com.frontendAttic.entity.po.ExamQuestion;
import com.frontendAttic.entity.po.ExamQuestionItem;
import com.frontendAttic.entity.query.ExamQuestionItemQuery;
import com.frontendAttic.entity.query.ExamQuestionQuery;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.exception.BusinessException;
import com.frontendAttic.service.ExamQuestionItemService;
import com.frontendAttic.service.ExamQuestionService;
import com.frontendAttic.utils.JsonUtils;
import com.frontendAttic.utils.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 考试题目 Controller
 */
@RestController("examQuestionController")
@RequestMapping("/examQuestion")
public class ExamQuestionController extends BaseController {

    @Resource
    private ExamQuestionService examQuestionService;

    @Resource
    private ExamQuestionItemService examQuestionItemService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.LIST_EXAM_QUESTION)
    public ResponseVO loadDataList(ExamQuestionQuery query) {
        query.setOrderBy("question_id desc");
        query.setQueryAnswer(true);
        return createSuccessResponse(examQuestionService.findListByPage(query));
    }

    @RequestMapping("/saveExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EDIT_EXAM_QUESTION)
    public ResponseVO saveExamQuestion(HttpSession session, @VerifyParam(required = true) ExamQuestion examQuestion,
                                       String questionItemListJson) {
        UserAdminSessionDto userAdminDto = getUserAdminFromSession(session);
        examQuestion.setCreateUserId(String.valueOf(userAdminDto.getUserId()));
        examQuestion.setCreateUserName(userAdminDto.getUserName());
        List<ExamQuestionItem> examQuestionItemList = new ArrayList<>();
        if (!QuestionTypeEnum.TRUE_FALSE.getType().equals(examQuestion.getQuestionType())) {
            if (StringUtil.isEmpty(questionItemListJson)) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            examQuestionItemList = JsonUtils.convertJsonArray2List(questionItemListJson, ExamQuestionItem.class);
        }
        examQuestionService.saveExamQuestion(examQuestion, examQuestionItemList, userAdminDto.getSuperAdmin());
        return createSuccessResponse(null);
    }

    @RequestMapping("/loadQuestionItem")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EDIT_EXAM_QUESTION)
    public ResponseVO loadQuestionItem(@VerifyParam(required = true) Integer questionId) {
        ExamQuestionItemQuery itemQuery = new ExamQuestionItemQuery();
        itemQuery.setQuestionId(questionId);
        itemQuery.setOrderBy("sort asc");
        return createSuccessResponse(examQuestionItemService.findListByParam(itemQuery));
    }

    @RequestMapping("/delExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EDIT_EXAM_QUESTION)
    public ResponseVO delExamQuestion(HttpSession session, @VerifyParam(required = true) Integer questionId) {
        UserAdminSessionDto userAdminDto = getUserAdminFromSession(session);
        examQuestionService.delExamQuestionBatch(String.valueOf(questionId), userAdminDto.getSuperAdmin() ? null : userAdminDto.getUserId());
        return createSuccessResponse(null);
    }

    @RequestMapping("/delExamQuestionBatch")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.BATCH_DELETE_EXAM_QUESTION)
    public ResponseVO delExamQuestionBatch(@VerifyParam(required = true) String questionIds) {
        examQuestionService.delExamQuestionBatch(questionIds, null);
        return createSuccessResponse(null);
    }

    @RequestMapping("/postExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.POST_EXAM_QUESTION)
    public ResponseVO postExamQuestion(@VerifyParam(required = true) String questionIds) {
        updateStatus(questionIds, PostStatusEnum.POST.getStatus());
        return createSuccessResponse(null);
    }

    @RequestMapping("/cancelPostExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.POST_EXAM_QUESTION)
    public ResponseVO cancelPostExamQuestion(@VerifyParam(required = true) String questionIds) {
        updateStatus(questionIds, PostStatusEnum.NO_POST.getStatus());
        return createSuccessResponse(null);
    }

    private void updateStatus(String questionIds, Integer status) {
        ExamQuestionQuery query = new ExamQuestionQuery();
        query.setQuestionIds(questionIds.split(","));

        ExamQuestion question = new ExamQuestion();
        question.setStatus(status);
        examQuestionService.updateByParam(question, query);
    }

    @RequestMapping("/showExamQuestionDetailNext")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.LIST_EXAM_QUESTION)
    public ResponseVO showExamQuestionDetailNext(ExamQuestionQuery query,
                                                 Integer nextType,
                                                 @VerifyParam(required = true) Integer currentId) {
        ExamQuestion examQuestion = examQuestionService.showDetailNext(query, nextType, currentId);
        return createSuccessResponse(examQuestion);
    }

    @RequestMapping("/importExamQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.IMPORT_EXAM_QUESTION)
    public ResponseVO importExamQuestion(HttpSession session, MultipartFile file) {
        UserAdminSessionDto userAdminDto = getUserAdminFromSession(session);
        List<ImportErrorItem> errorList = examQuestionService.importExamQuestion(file, userAdminDto);
        return createSuccessResponse(errorList);
    }
}