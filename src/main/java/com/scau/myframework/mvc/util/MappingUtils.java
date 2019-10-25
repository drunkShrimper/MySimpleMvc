package com.scau.myframework.mvc.util;

import com.scau.myframework.mvc.annotation.MyController;
import com.scau.myframework.mvc.annotation.MyRequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingUtils {
    public static HashMap<String,Object>  doUrlMapping(List<String> classNames) {

        HashMap<String,Object> handlerMap = new HashMap<String,Object>();

        for (String className : classNames) {
            String cn = className.replace(".class", "");

            try {
                Class<?> clazz = Class.forName(cn);

                if (clazz.isAnnotationPresent(MyController.class)) {

                    Object instance = clazz.newInstance();
                    MyRequestMapping typeMapping = clazz.getAnnotation(MyRequestMapping.class);

                    Method[] methods = clazz.getMethods();
                    for(Method method:methods){
                        if(method.isAnnotationPresent(MyRequestMapping.class)){

                            MyRequestMapping methodMapping = method.getAnnotation(MyRequestMapping.class);
                            String requestPath = typeMapping.value() + methodMapping.value();
                            handlerMap.put(requestPath,method);
                        }else{
                            continue;
                        }
                    }
                } else {
                    continue;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return handlerMap;
    }
}
