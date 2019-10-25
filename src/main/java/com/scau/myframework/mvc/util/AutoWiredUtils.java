package com.scau.myframework.mvc.util;

import com.scau.myframework.mvc.annotation.MyAutowired;
import com.scau.myframework.mvc.annotation.MyController;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AutoWiredUtils {
    public static void doAutoWired(Map<String,Object> beans){

        for(Map.Entry<String,Object> entry:beans.entrySet()){

            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();

            try{
                if(clazz.isAnnotationPresent(MyController.class)){
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field:fields){
                        if(field.isAnnotationPresent(MyAutowired.class)){
                            MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
                            String key = myAutowired.value();

                            Object bean = beans.get(key);
                            field.setAccessible(true);
                            field.set(instance,bean);

                        }else {
                            continue;
                        }
                    }
                }else {
                    continue;
                }
            }catch (Exception e){

            }
        }
    }
}
