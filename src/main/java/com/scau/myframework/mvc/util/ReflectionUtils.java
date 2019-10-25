package com.scau.myframework.mvc.util;

import com.scau.myframework.mvc.annotation.MyController;
import com.scau.myframework.mvc.annotation.MyRequestMapping;
import com.scau.myframework.mvc.annotation.MyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtils {

    public static HashMap<String,Object> doInstance(List<String> classNames) {
        HashMap<String,Object> beans = new HashMap<String,Object>();
        for(String className:classNames){

            String cn = className.replace(".class","");

            try{
                Class<?> clazz = Class.forName(cn);

                if(clazz.isAnnotationPresent(MyController.class)){

                    Object instance = clazz.newInstance();
                    MyRequestMapping myRequestMapping = clazz.getAnnotation(MyRequestMapping.class);
                    String key = myRequestMapping.value();
                    beans.put(key,instance);
                } else if (clazz.isAnnotationPresent(MyService.class)) {

                    Object instance = clazz.newInstance();
                    MyService MyService = clazz.getAnnotation(MyService.class);
                    String key = MyService.value();
                    beans.put(key,instance);
                }else {
                    continue;
                }
            }catch (Exception e){
                return null;
            }


        }
        return beans;
    }
}
