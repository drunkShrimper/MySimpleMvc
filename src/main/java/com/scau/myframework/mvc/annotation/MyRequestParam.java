package com.scau.myframework.mvc.annotation;

import java.lang.annotation.*;

/**
 * @description: 指定该对象为请求参数
 * @author: lipan
 * @time: 2019/10/26 13:23
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestParam {
    String value() default "";
}