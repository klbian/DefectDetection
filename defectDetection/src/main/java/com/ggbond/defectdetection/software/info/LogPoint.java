package com.ggbond.defectdetection.software.info;


import java.lang.annotation.*;

/**
 * 定义系统日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogPoint {
    OpEnum value() ;
    Class mainRole() default Object.class;
    Class target() default Object.class;
    String info() default "";
}