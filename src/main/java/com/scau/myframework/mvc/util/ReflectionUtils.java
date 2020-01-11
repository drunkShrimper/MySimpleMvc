package com.scau.myframework.mvc.util;

import java.lang.reflect.Method;

/**
 * @description: 将反射的常用功能封装起来
 * @author: lipan
 * @time: 2020/1/11 10:42
 */
public class ReflectionUtils {
    public static Object newInstance(Class<?> cls){
        Object instance = null;
        try {
            instance = cls.newInstance();
        }catch (Exception e){
        }finally {
            return instance;
        }
    }
    public static Object invokeMethod(Object obj, Method method, Object...args){
        Object result = null;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (Exception e) {
        } finally {
            return result;
        }
    }
}
