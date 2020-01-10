package com.scau.myframework.mvc.util;



/**
 * Created on 2018/8/8 22:03.
 *
 * @author SinKitwah
 * @Description 类加载器-获取基础包下的所有类
 */
public class ClassUtil {


    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

}
