package com.scau.myframework.mvc.servlet;

import com.scau.myframework.mvc.annotation.*;
import com.scau.myframework.mvc.util.AutoWiredUtils;
import com.scau.myframework.mvc.util.ClassNameScanner;
import com.scau.myframework.mvc.util.MappingUtils;
import com.scau.myframework.mvc.util.ReflectionUtils;
import com.scau.myframework.test.controller.UserController;
import com.sun.xml.internal.bind.v2.TODO;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyDispatcherServlet extends HttpServlet {


    private List<String> classNames ;
    Map<String,Object> beans ;
    Map<String,Object> handlerMap ;

    @Override
    public void init(ServletConfig config) throws ServletException {

        //TODO 后期应该通过读xml配置的方式读入参数，而且应该改为静态方法更合理
        classNames = new ClassNameScanner().scan("com.scau");


        //TODO 后期应该通过ioc模块完成此功能
        beans = ReflectionUtils.doInstance(classNames);

        //TODO 后期应该通过ioc模块完成此功能
        AutoWiredUtils.doAutoWired(beans);


        handlerMap =  MappingUtils.doUrlMapping(classNames);
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
        //System.out.println("/" + requestPath.split("/")[0]);
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
