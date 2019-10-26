package com.scau.myframework.mvc.util;

import com.scau.myframework.mvc.annotation.MyController;
import com.scau.myframework.mvc.annotation.MyRequestMapping;
import com.scau.myframework.mvc.annotation.MyService;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
