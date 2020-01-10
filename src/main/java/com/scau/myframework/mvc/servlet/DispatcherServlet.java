package com.scau.myframework.mvc.servlet;

import com.google.gson.Gson;
import com.scau.myframework.mvc.annotation.MyRequestMapping;
import com.scau.myframework.mvc.annotation.MyRequestParam;
import com.scau.myframework.mvc.entity.ModelAndView;
import com.scau.myframework.mvc.helper.ClassHelper;
import com.scau.myframework.mvc.helper.IocHelper;
import com.scau.myframework.mvc.util.CastUtils;
import com.scau.myframework.mvc.util.PropertiesUtil;
import com.scau.myframework.mvc.util.PropertiesUtils;
import com.scau.myframework.mvc.util.ReflectionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;


public class DispatcherServlet extends HttpServlet {



    private Map<String,Object> beanMap;
    Map<String,Object> handlerMap ;


    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        //TODO 获取IOC容器，后期应该通过ioc模块完成此功能而不是IocHelper（IocHelper只能完成mvc的依赖注入逻辑）
        IocHelper.doInstance();
        IocHelper.doAutoWired();
        beanMap =  IocHelper.getBeanMap();

        //将请求路径与对应的处理方法映射起来(即：解析url和Method的关联关系)
        initHandlerMappings();


        //TODO 处理访问静态资源、jsp页面等情况
        //获取ServletContext对象（用于注册Servlet）
        ServletContext servletContext = super.getServletContext();

        if(null == servletContext){
            System.out.println("+++++++++++++++++++++++++++++++");
        }

        //注册处理JSP的servlet
        //ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        //注册了才能识别jsp
       // jspServlet.addMapping("/WEB-INF/jsp/"+"*");
        //注册处理静态资源的默认Servlet
       // ServletRegistration defaultServlet =  servletContext.getServletRegistration("default");
       // defaultServlet.addMapping("/"+"*");

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

    //根据传入的url,利用反射，完成请求处理
    //只能为带有@MyRequestParam注解的参数赋值，但只是用Object存储，没有进行类型转换
    //后期还要考虑是否要给对象类型的参数自动注入
    private  void handle(HttpServletRequest req, HttpServletResponse resp, String  requestPath) throws ServletException, IOException {

        Method method = (Method) handlerMap.get(requestPath);


        Object instance = beanMap.get("/" + requestPath.split("/")[1]);
        if(null == instance || null == method) {
            //defaultServlet;//这里是否要看其是否为静态资源？
            //req.getRequestDispatcher(requestPath).forward(req, resp);
            resp.getWriter().println("找不到----->mapping");
            return;
        }
        Object[] args = getArgs(method,req,resp);

        Object result = ReflectionUtils.invokeMethod(instance,method,args);

        if(null == result) {
            return;
        }

        if (result instanceof String) {

            try {
                if (((String) result).startsWith("redirect:")){
                    resp.sendRedirect(req.getContextPath()+"/"+((String) result).replace("redirect:","").trim());
                }else {
                    req.getRequestDispatcher("/WEB-INF/jsp/" + (String) result).forward(req, resp);
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
                    req.getRequestDispatcher(PropertiesUtils.getJspPath()+mv.getPath()).forward(req, resp);
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
                    args[idx++] = CastUtils.getBasicInstanceByString(paramClazzs[i],req.getParameter(requestParam.value()));
                }else{
                    //没有标注解的，如果是POJO类型，请求参数的name和POJO的属性名要相同才能为其注入。这也是springmvc的约定...(貌似)
                    //TODO 但是复杂类型如何处理呢？ 貌似spring mvc也是可以自动注入的（即：支持级联属性的注入）
                    args[idx++] = CastUtils.getPojoInstance(paramClazzs[i],req.getParameterMap());
                }
            }
        }
        return args;
    }
}
