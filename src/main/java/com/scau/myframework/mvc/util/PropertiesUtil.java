package com.scau.myframework.mvc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created on 2018/8/8 23:07.
 *
 * @author SinKitwah
 * @Description 读取配置文件
 */
public class PropertiesUtil {



    /**
     * 用于存放配置项
     */
    private enum WebProperties{
        FILE("easyFramework.properties"),
        BASE_PACKAGE("easyFramework.app.base_package"),
        JSP_PATH("easyFramework.app.jsp_path"),
        STATIC_PATH("easyFramework.app.static");

        private String value;

        private WebProperties(String value){
            this.value = value;
        }

        public String value(){
            return value;
        }

    }
    private static Properties p;

    /**
     * 加载配置项
     * @param file 配置文件名
     * @return Properties对象
     */
    public static Properties load(String file){
        Properties p=new Properties();
        InputStream is=ClassUtil.getClassLoader().getResourceAsStream(file);
        try {
            p.load(is);
            is.close();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        return p;
    }


    /**
     * 获取需要扫描的包的路径
     * @return 包的路径
     */
    public static String getBasePackage() {
        //若没有
        if (p==null){
            synchronized (PropertiesUtil.class){
                p = load(WebProperties.FILE.value());
            }
        }
        String value = "";
        String key = WebProperties.BASE_PACKAGE.value();
        if (p.containsKey(key)) {
            value = p.getProperty(key);
        }
        return value;
    }

    /**
     * 获取有JSP页面的包的路径
     * @return JSP页面的包的路径
     */
    public static String getJspPath() {
        if (p==null){
            synchronized (PropertiesUtil.class){
                p = load(WebProperties.FILE.value());
            }
        }
        String value = "";
        String key = WebProperties.JSP_PATH.value();
        if (p.containsKey(key)) {
            value = p.getProperty(key);
        }
        return value;
    }
    /**
     * 获取有JSP页面的包的路径
     * @return JSP页面的包的路径
     */
    public static String getStaticPath() {
        if (p==null){
            synchronized (PropertiesUtil.class){
                p = load(WebProperties.FILE.value());
            }
        }
        String value = "";
        String key = WebProperties.STATIC_PATH.value();
        if (p.containsKey(key)) {
            value = p.getProperty(key);
        }
        return value;
    }
}
