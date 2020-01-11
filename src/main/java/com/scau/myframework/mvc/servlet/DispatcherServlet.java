package com.scau.myframework.mvc.servlet;

import com.google.gson.Gson;
import com.scau.myframework.mvc.annotation.MyRequestMapping;
import com.scau.myframework.mvc.annotation.MyRequestParam;
import com.scau.myframework.mvc.entity.ModelAndView;
import com.scau.myframework.mvc.helper.ClassHelper;
import com.scau.myframework.mvc.helper.IocHelper;
import com.scau.myframework.mvc.util.TypeCastUtils;
import com.scau.myframework.mvc.util.PropertiesUtils;
import com.scau.myframework.mvc.util.ReflectionUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: mvc的核心，完成 请求路径与处理方法映射、视图解析
 * @author: lipan
 * @time: 2019/10/26 12:59
 */
public class DispatcherServlet extends HttpServlet {

    /**
     * ioc容器
     */
    private Map<String,Object> beanMap;
    /**
     * key:请求url
     * value:处理方法
     */
    Map<String,Object> handlerMap ;


    @Override
    public void init(ServletConfig config) throws ServletException {

        //TODO 获取IOC容器，后期应该通过ioc模块完成此功能而不是IocHelper（IocHelper只能完成controller，service的依赖注入逻辑）
        beanMap =  IocHelper.getBeanMap();

        //将请求路径与对应的处理方法映射起来(即：解析url和Method的关联关系)
        initHandlerMappings();
    }

    private  void initHandlerMappings() {
        handlerMap = new HashMap<String,Object>();
        for (Class<?> clazz : ClassHelper.getControllerClass()) {
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
        handle(req, resp, requestPath);
    }

    /**
     * 分发请求
     * @param req
     * @param resp
     * @param requestPath
     * @throws ServletException
     * @throws IOException
     */
    private  void handle(HttpServletRequest req, HttpServletResponse resp, String  requestPath) throws ServletException, IOException {

        Method method = (Method) handlerMap.get(requestPath);
        //没有找到这个
        if(null == method) {
            resp.getWriter().println("error----->mapping not found!");
            return;
        }

        Object instance = beanMap.get("/" + requestPath.split("/")[1]);
        if(null == instance) {
            return;
        }

        Object[] args = getArgs(method,req,resp);

        Object result = ReflectionUtils.invokeMethod(instance,method,args);

        if(null == result) {
            return;
        }

        if (result instanceof String) {

            result = result.toString();
            try {
                if (((String) result).startsWith("redirect:")){
                    resp.sendRedirect(req.getContextPath()+"/"+((String) result).replace("redirect:","").trim());
                }else {
                    req.getRequestDispatcher(PropertiesUtils.getViewPrefix() + (String) result + PropertiesUtils.getViewSuffix()).forward(req, resp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else if (result instanceof ModelAndView){

            ModelAndView mv = (ModelAndView) result;
            if (mv.getPath() != null) {
                try {
                    if (mv.getPath().startsWith("redirect:")){
                        resp.sendRedirect(req.getContextPath()+mv.getPath().replace("redirect:","").trim());
                        return;
                    }
                    Map<String, Object> model = mv.getModels();
                    for(Map.Entry<String, Object> entry:model.entrySet()){
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                    req.getRequestDispatcher(PropertiesUtils.getViewPrefix()+mv.getPath()+PropertiesUtils.getViewSuffix()).forward(req, resp);
                } catch (Exception e) {

                }
            }
        } else{//其他情况统一返回json数据，（当然包括标注了@MyResponseBody注解的）
            try {
                Gson gson = new Gson();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter writer = resp.getWriter();
                writer.write(gson.toJson(result));
                writer.flush();
                writer.close();
            } catch (Exception e) {
            }
            return;
        }
    }

    private Object[] getArgs(Method method,HttpServletRequest req, HttpServletResponse resp){
        Class<?>[] paramClazzs = method.getParameterTypes();
        Parameter[] parameters = method.getParameters();

        Object[] args = new Object[paramClazzs.length];

        int idx = 0;
        for (int i=0; i<paramClazzs.length;i++){
            //如果方法的参数中写有原生的request，response
            if(ServletRequest.class.isAssignableFrom(paramClazzs[i])){
                args[idx++] = req;
            }else if(ServletResponse.class.isAssignableFrom(paramClazzs[i])){
                args[idx++] = resp;
            } else{
                //一般只在基本类型上使用@MyRequestParam，对于POJO类型并不用使用注解也会自动注入
                if(parameters[i].isAnnotationPresent(MyRequestParam.class)){
                    MyRequestParam requestParam = parameters[i].getAnnotation(MyRequestParam.class);
                    args[idx++] = TypeCastUtils.getBasicInstanceByString(paramClazzs[i],req.getParameter(requestParam.value()));
                }else{
                    //没有标注解的，如果是POJO类型，请求参数的name和POJO的属性名要相同才能为其注入。这也是springmvc的约定...(貌似)
                    //TODO 但是复杂类型如何处理呢？ 貌似spring mvc也是可以自动注入的（即：支持级联属性的注入）
                    args[idx++] = TypeCastUtils.getPojoInstance(paramClazzs[i],req.getParameterMap());
                }
            }
        }
        return args;
    }
}
