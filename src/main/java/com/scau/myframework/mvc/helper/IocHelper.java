package com.scau.myframework.mvc.helper;

import com.scau.myframework.mvc.annotation.MyAutowired;
import com.scau.myframework.mvc.annotation.MyController;
import com.scau.myframework.mvc.annotation.MyRequestMapping;
import com.scau.myframework.mvc.annotation.MyService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lipan
 * @time: 2019/10/26 12:59
 */
public class IocHelper {
    private static final Map<String, Object> BEAN_MAP = new HashMap<String, Object>();

    static {

        doInstance(); //初始化IOC容器
        doAutoWired(); //依赖注入
    }


    /**
     * TODO 目前只向IOC容器中添加了Controller和Service两种类的实例，其他情况在ioc模块中实现
     */
    private static void doInstance() {

        for (Class<?> clazz : ClassHelper.getControllerClass()) {
            try {


                Object instance = clazz.newInstance();
                MyRequestMapping myRequestMapping = clazz.getAnnotation(MyRequestMapping.class);

                String key = myRequestMapping.value();
                BEAN_MAP.put(key, instance);

            } catch (Exception e) {

            }
        }

        for (Class<?> clazz : ClassHelper.getServiceClass()) {
            try {


                Object instance = clazz.newInstance();
                MyService MyService = clazz.getAnnotation(MyService.class);
                String key = MyService.value();
                BEAN_MAP.put(key, instance);

            } catch (Exception e) {

            }
        }
    }




    /**
     *  TODO 依赖注入，目前只仅实现了向Controller注入Service
     */

    public static void doAutoWired() {

        //遍历IOC容器，查看是否需要进行依赖注入
        for (Map.Entry<String, Object> entry : BEAN_MAP.entrySet()) {

            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();

            try {
                //仅检查了Controller层是否需要依赖注入
                if (clazz.isAnnotationPresent(MyController.class)) {
                    Field[] fields = clazz.getDeclaredFields();
                    //找出标有@MyAutowired注解的属性，实现依赖注入
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(MyAutowired.class)) {
                            MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
                            String key = myAutowired.value();

                            Object bean = BEAN_MAP.get(key);
                            field.setAccessible(true);
                            field.set(instance, bean);

                        } else {
                            continue;
                        }
                    }
                } else {
                    continue;
                }
            } catch (Exception e) {

            }
        }
    }

    public static Map<String, Object> getBeanMap() {
        return BEAN_MAP;
    }
}
