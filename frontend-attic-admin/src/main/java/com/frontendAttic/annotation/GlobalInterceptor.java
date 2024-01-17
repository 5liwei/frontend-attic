package com.frontendAttic.annotation;

import com.frontendAttic.entity.enums.PermissionCodeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @return
 * @Description 定义注解
 * @Author LiWei
 * @Date 2023/1/6 16:28
 * @ClassName
 * @MethodName
 * @Params
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {

    boolean checkLogin() default true;

    PermissionCodeEnum permissionCode() default PermissionCodeEnum.PERMISSION_NONE;

    boolean checkParams() default true;
}
