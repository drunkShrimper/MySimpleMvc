package com.scau.myframework.mvc.annotation;

import java.lang.annotation.*;

/**
 * @description: 依赖注入
 * @author: lipan
 * @time: 2019/10/26 13:23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAutowired {
    String value() default "";
}