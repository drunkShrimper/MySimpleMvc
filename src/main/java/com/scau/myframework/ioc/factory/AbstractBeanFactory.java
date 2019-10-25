package com.scau.myframework.ioc.factory;



import com.scau.myframework.ioc.entity.BeanInfo;
import com.scau.myframework.ioc.reader.SourceReader;
import com.scau.myframework.ioc.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class AbstractBeanFactory implements BeanFactory {

    private  String filePath;
    private Map<String, BeanInfo> container;//ioc容器
    protected SourceReader reader; //后期还可以实现注解方式的依赖注入

    public AbstractBeanFactory(String filePath) {
        this.filePath = filePath;
    }

    protected abstract void setSourceReader(SourceReader reader);

    public void registerBeans(){
        this.container = this.reader.loadBeans(filePath);
    }

    @Override
    public Object getBean(String name) {
        BeanInfo beanInfo = this.container.get(name);
        if(null == beanInfo) return null;
        else return this.parseBean(beanInfo);//需要从ioc中获取示例时才使用反射创建，而不是提前创建好，貌似可以提升性能
    }

    protected Object parseBean(BeanInfo beanInfo){
        try {
            Class clazz = Class.forName(beanInfo.getType());

            Object bean  = clazz.newInstance();

            Method[] methods = clazz.getMethods();
            for (Method method:methods) {

                String methodName = method.getName();

                if (methodName.startsWith("set")||methodName.startsWith("is")){
                    if(methodName.startsWith("set")){
                        methodName = methodName.replace(methodName.substring(0,4),methodName.substring(3,4).toLowerCase());
                    }else {
                        methodName = methodName.replace(methodName.substring(0,3),methodName.substring(2,3).toLowerCase());
                    }
                    //获取该方法的参数类型
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if(paramTypes[0].equals(Boolean.class)) methodName = "is" + StringUtils.firstCharToUp(methodName);
                    Object param = StringUtils.getBasicInstanceByString(paramTypes[0], (String)beanInfo.getProperties().get(methodName));
                    method.invoke(bean,param);
                }
            }
            return  bean;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
