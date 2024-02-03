package com.frontendAttic.controller;

import com.frontendAttic.entity.enums.ResponseCodeEnum;
import com.frontendAttic.entity.vo.ResponseVO;
import com.frontendAttic.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandlerController extends ABaseController {


    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    public Object globalExceptionHandling(Exception exception, HttpServletRequest request) {
        LOGGER.error("Error in request: URL , Message: ", request.getRequestURL(), exception);
        return buildErrorResponse(exception);
    }

    private ResponseVO<Object> buildErrorResponse(Exception exception) {
        if (exception instanceof NoHandlerFoundException) {
            return createErrorResponse(ResponseCodeEnum.CODE_404.getCode(), "Not Found");
        } else if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
            Integer errorCode = businessException.getCode() != null ? businessException.getCode() : ResponseCodeEnum.CODE_600.getCode();
            return createErrorResponse(errorCode, businessException.getMessage());
        } else if (exception instanceof BindException || exception instanceof MethodArgumentTypeMismatchException) {
            return createErrorResponse(ResponseCodeEnum.CODE_600.getCode(), "Invalid method argument");
        } else if (exception instanceof DuplicateKeyException) {
            return createErrorResponse(ResponseCodeEnum.CODE_601.getCode(), "Duplicate key error");
        } else {
            return createErrorResponse(ResponseCodeEnum.CODE_500.getCode(), "Internal Server Error");
        }
    }

    private ResponseVO<Object> createErrorResponse(Integer code, String message) {
        return new ResponseVO<>(STATUS_ERROR, code, message, null);
    }
}
