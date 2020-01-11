package com.scau.myframework.mvc.util;



/**
 * @description: 提供类加载器
 * @author: lipan
 * @time: 2020/1/11 10:42
 */
public class ClassUtils {


    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

}
