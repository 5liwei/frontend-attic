package com.frontendAttic.annotation;

import com.frontendAttic.entity.enums.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 参数校验
 * @Author LiWei
 * @Date 2024/01/05 16:56
 * @ClassName
 * @MethodName
 * @Params
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyParam {

    /**
     * 校验正则
     *
     * @return
     */
    VerifyRegexEnum regex() default VerifyRegexEnum.NO;

    /**
     * 最小长度
     *
     * @return
     */
    int min() default -1;

    /**
     * 最大长度
     *
     * @return
     */
    int max() default -1;

    boolean required() default false;


}
