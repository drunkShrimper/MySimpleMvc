package com.scau.myframework.orm.util;

import com.scau.myframework.orm.entity.Configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * @description: 读取配置文件 orm.properties
 * @author: lipan
 * @time: 2020/1/11 10:42
 */
public class PropertiesUtils {

    private static Configuration configuration;
    private static Properties properties = new Properties();

    static {

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration = new Configuration();
        configuration.setDriver(properties.getProperty("driver"));
        configuration.setPoPackage(properties.getProperty("poPackage"));
        configuration.setPassword(properties.getProperty("password"));
        configuration.setSrcPath(properties.getProperty("srcPath"));
        configuration.setUrl(properties.getProperty("url"));
        configuration.setUser(properties.getProperty("user"));
        configuration.setUsingDB(properties.getProperty("usingDB"));
    }

    public static Configuration getConfiguration() {
        return configuration;
    }
}
