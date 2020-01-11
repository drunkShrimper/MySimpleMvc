package com.scau.myframework.mvc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @description: 读取配置文件 mvc.properties
 * @author: lipan
 * @time: 2020/1/11 10:42
 */
public class PropertiesUtils {

    private static Properties properties;

    static {
        load();
    }

    private PropertiesUtils() {
    }

    private static void load() {

        String propertiesFileName = "mvc.properties";
        properties = new Properties();
        InputStream is = ClassUtils.getClassLoader().getResourceAsStream(propertiesFileName);
        try {
            properties.load(is);
            is.close();
        } catch (IOException e) {
        }
    }

    public static String getBasePackage() {
        return properties.getProperty("base_package");
    }

    public static String getViewPrefix() {
        return properties.getProperty("view.prefix");
    }

    public static String getViewSuffix() {
        return properties.getProperty("view.suffix");
    }
}
