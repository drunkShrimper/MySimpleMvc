package com.scau.myframework.mvc.annotation;

import java.lang.annotation.*;

/**
 * @description: url映射
 * @author: lipan
 * @time: 2019/10/26 13:23
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {
    String value() default "";
}