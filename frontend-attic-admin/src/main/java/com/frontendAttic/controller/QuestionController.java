package com.frontendAttic.controller;


import com.frontendAttic.annotation.GlobalInterceptor;
import com.frontendAttic.annotation.VerifyParam;
import com.frontendAttic.entity.dto.ImportErrorItem;
import com.frontendAttic.entity.dto.UserAdminSessionDto;
import com.frontendAttic.entity.enums.PermissionCodeEnum;
import com.frontendAttic.entity.enums.PostStatusEnum;
import com.frontendAttic.entity.po.Question;
import com.frontendAttic.entity.query.QuestionQuery;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.service.QuestionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 问题 Controller
 */
@RestController("QuestionController")
@RequestMapping("/Question")
public class QuestionController extends BaseController {

    @Resource
    private QuestionService QuestionService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.LIST_QUESTION)
    public ResponseVO loadDataList(QuestionQuery query) {
        query.setOrderBy("question_id desc");
        query.setQueryTextContent(true);
        return createSuccessResponse(QuestionService.findListByPage(query));
    }

    @RequestMapping("/saveQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EDIT_QUESTION)
    public ResponseVO saveQuestion(HttpSession session, Question bean) {
        UserAdminSessionDto UserAdminSessionDto = getUserAdminFromSession(session);
        bean.setCreateUserId(String.valueOf(UserAdminSessionDto.getUserId()));
        bean.setCreateUserName(UserAdminSessionDto.getUserName());
        QuestionService.saveQuestion(bean, UserAdminSessionDto.getSuperAdmin());
        return createSuccessResponse(null);
    }

    @RequestMapping("/delQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.EDIT_QUESTION)
    public ResponseVO delQuestion(HttpSession session, @VerifyParam(required = true) Integer questionId) {
        UserAdminSessionDto userAdminDto = getUserAdminFromSession(session);
        QuestionService.delQuestionBatch(String.valueOf(questionId), userAdminDto.getSuperAdmin() ? null : userAdminDto.getUserId());
        return createSuccessResponse(null);
    }

    @RequestMapping("/delQuestionBatch")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.BATCH_DELETE_QUESTION)
    public ResponseVO delQuestionBatch(@VerifyParam(required = true) String questionIds) {
        QuestionService.delQuestionBatch(questionIds, null);
        return createSuccessResponse(null);
    }

    @RequestMapping("/postQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.POST_QUESTION)
    public ResponseVO postQuestion(@VerifyParam(required = true) String questionIds) {
        updateStatus(questionIds, PostStatusEnum.POST.getStatus());
        return createSuccessResponse(null);
    }

    @RequestMapping("/cancelPostQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.POST_QUESTION)
    public ResponseVO cancelPostQuestion(@VerifyParam(required = true) String questionIds) {
        updateStatus(questionIds, PostStatusEnum.NO_POST.getStatus());
        return createSuccessResponse(null);
    }

    private void updateStatus(String questionIds, Integer status) {
        QuestionQuery params = new QuestionQuery();
        params.setQuestionIds(questionIds.split(","));
        Question Question = new Question();
        Question.setStatus(status);
        QuestionService.updateByParam(Question, params);
    }

    @RequestMapping("/importQuestion")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.IMPORT_QUESTION)
    public ResponseVO importQuestion(HttpSession session, MultipartFile file) {
        UserAdminSessionDto userAdminDto = getUserAdminFromSession(session);
        List<ImportErrorItem> errorList = QuestionService.importQuestion(file, userAdminDto);
        return createSuccessResponse(errorList);
    }

    @RequestMapping("/showQuestionDetailNext")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.LIST_QUESTION)
    public ResponseVO showQuestionDetailNext(QuestionQuery query, Integer nextType,
                                             @VerifyParam(required = true) Integer currentId) {
        Question Question = QuestionService.showDetailNext(query, nextType, currentId, false);
        return createSuccessResponse(Question);
    }
}