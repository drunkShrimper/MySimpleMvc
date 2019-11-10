package com.scau.myframework.mvc.annotation;

import java.lang.annotation.*;

/**
 * @description: 以json形式返回数据
 * @author: lipan
 * @time: 2019/10/26 13:23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyResponseBody {
}
