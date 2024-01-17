package com.frontendAttic.controller;

import com.frontendAttic.entity.enums.ResponseCodeEnum;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.exception.BusinessException;

public class ABaseController {

    protected static final String STATUS_SUCCESS = "success";
    protected static final String STATUS_ERROR = "error";

    protected <T> ResponseVO<T> createSuccessResponse(T data) {
        return new ResponseVO<>(STATUS_SUCCESS, ResponseCodeEnum.CODE_200.getCode(), ResponseCodeEnum.CODE_200.getMsg(), data);
    }

    protected <T> ResponseVO<T> createErrorResponseForBusinessException(BusinessException exception, T data) {
        Integer errorCode = (exception.getCode() == null) ? ResponseCodeEnum.CODE_600.getCode() : exception.getCode();
        String errorMessage = exception.getMessage();
        return new ResponseVO<>(STATUS_ERROR, errorCode, errorMessage, data);
    }

    protected <T> ResponseVO<T> createServerErrorResponse(T data) {
        return new ResponseVO<>(STATUS_ERROR, ResponseCodeEnum.CODE_500.getCode(), ResponseCodeEnum.CODE_500.getMsg(), data);
    }
}
