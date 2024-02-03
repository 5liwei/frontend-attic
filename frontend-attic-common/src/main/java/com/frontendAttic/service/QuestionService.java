package com.frontendAttic.service;


import com.frontendAttic.entity.dto.ImportErrorItem;
import com.frontendAttic.entity.dto.UserAdminSessionDto;
import com.frontendAttic.entity.po.Question;
import com.frontendAttic.entity.query.QuestionQuery;
import com.frontendAttic.entity.vo.PageResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 问题 业务接口
 */
public interface QuestionService {

    /**
     * 根据条件查询列表
     */
    List<Question> findListByParam(QuestionQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(QuestionQuery param);

    /**
     * 分页查询
     */
    PageResultVO<Question> findListByPage(QuestionQuery param);

    /**
     * 新增
     */
    Integer add(Question bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<Question> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<Question> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(Question bean, QuestionQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(QuestionQuery param);

    /**
     * 根据QuestionId查询对象
     */
    Question getQuestionByQuestionId(Integer questionId);


    /**
     * 根据QuestionId修改
     */
    Integer updateQuestionByQuestionId(Question bean, Integer questionId);


    /**
     * 根据QuestionId删除
     */
    Integer deleteQuestionByQuestionId(Integer questionId);

    void saveQuestion(Question info, Boolean isAdmin);

    void delQuestionBatch(String questionIds, Integer userId);

    List<ImportErrorItem> importQuestion(MultipartFile file, UserAdminSessionDto userAdminDto);

    Question showDetailNext(QuestionQuery query, Integer nextType, Integer currentId, Boolean updateReadCount);
}