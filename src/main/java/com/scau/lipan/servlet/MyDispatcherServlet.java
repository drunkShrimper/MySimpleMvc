package com.scau.lipan.servlet;

import com.scau.lipan.annotation.*;
import com.scau.lipan.controller.UserController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDispatcherServlet extends HttpServlet {


    private List<String> classNames = new ArrayList<String>();
    Map<String,Object> beans = new HashMap<String,Object>();
    Map<String,Object> handlerMap = new HashMap<String,Object>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        basePackageScan("com.scau");
        doInstance();
        doAutoWired();
        doUrlMapping();
    }

    private void basePackageScan(String basePackage){


        URL url = this.getClass().getClassLoader().getResource("/"+basePackage.replaceAll("\\.","/"));
        String fileStr = url.getFile();
        File file = new File(fileStr);

        String[] filesStr = file.list();

        for(String path:filesStr){
            File filePath = new File(fileStr + path);
            if(filePath.isDirectory()){
                basePackageScan(basePackage+"."+path);
            }else{
                classNames.add(basePackage+"."+filePath.getName());
            }
        }

    }

    private void doInstance() {
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

            }


        }
    }

    private void doAutoWired(){

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

    private void  doUrlMapping() {

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

            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        String requestPath = uri.replace(contextPath,"");

        Method method = (Method) handlerMap.get(requestPath);

        String[] strings = requestPath.split("/");

        UserController instance = (UserController) beans.get("/" + requestPath.split("/")[1]);
        System.out.println("/" + requestPath.split("/")[0]);
        Object[] args = hand(req, resp, method);
        try {
            method.invoke(instance,args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static Object[] hand(HttpServletRequest req, HttpServletResponse resp, Method method){

        Class<?>[] paramClazzs = method.getParameterTypes();
        Object[] args = new Object[paramClazzs.length];

        int args_i = 0;
        int index = 0;
        for (Class<?> paramClazz:paramClazzs){
            if(ServletRequest.class.isAssignableFrom(paramClazz)){
                args[args_i++] = req;
            }
            if(ServletResponse.class.isAssignableFrom(paramClazz)){
                args[args_i++] = resp;
            }
            Annotation[] paramAns = method.getParameterAnnotations()[index];
            if (paramAns.length > 0){
                for (Annotation paramAn:paramAns){
                    if(MyRequestParam.class.isAssignableFrom(paramAn.getClass())){
                        MyRequestParam myRequestParam = (MyRequestParam) paramAn;
                        args[args_i++] = req.getParameter(myRequestParam.value());
                    }
                }
            }
            index++;
        }
        return args;
    }

}
