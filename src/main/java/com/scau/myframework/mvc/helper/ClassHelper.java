package com.scau.myframework.mvc.helper;

import com.scau.myframework.mvc.annotation.MyController;
import com.scau.myframework.mvc.annotation.MyService;
import com.scau.myframework.mvc.util.PropertiesUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 类的相关操作
 * @author: lipan
 * @time: 2019/10/26 13:23
 */
public class ClassHelper {

    private static final List<String> classNames = new ArrayList<String>();

    private static final Set<Class<?>> ALL_CLASS = new HashSet<Class<?>>();
    private static final Set<Class<?>> CONTROLLER_CLASS = new HashSet<Class<?>>();
    private static final Set<Class<?>> SERVICE_CLASS = new HashSet<Class<?>>();

    static {

        String basePackage = PropertiesUtils.getBasePackage();
        scanAllClassNames(basePackage);
        loadClassByNames();
    }
    private static void scanAllClassNames(String basePackage) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("/"+basePackage.replaceAll("\\.","/"));
        String fileStr = url.getFile();
        File file = new File(fileStr);

        String[] filesStr = file.list();

        for(String path:filesStr){
            File filePath = new File(fileStr + path);
            if(filePath.isDirectory()){
                scanAllClassNames(basePackage+"."+path);
            }else{
                classNames.add(basePackage+"."+filePath.getName().replace(".class",""));
            }
        }
    }
    private static void loadClassByNames() {
        for(String className:classNames){
            try{
                Class<?> clazz = Class.forName(className);
                ALL_CLASS.add(clazz);
                if(clazz.isAnnotationPresent(MyController.class)){
                    CONTROLLER_CLASS.add(clazz);
                } else if (clazz.isAnnotationPresent(MyService.class)) {
                    SERVICE_CLASS.add(clazz);
                }else {
                    continue;
                }
            }catch (Exception e){

            }
        }
    }

    public static Set<Class<?>> getAllClass() {
        return ALL_CLASS;
    }

    public static Set<Class<?>> getControllerClass() {
        return CONTROLLER_CLASS;
    }

    public static Set<Class<?>> getServiceClass() {
        return SERVICE_CLASS;
    }
}
