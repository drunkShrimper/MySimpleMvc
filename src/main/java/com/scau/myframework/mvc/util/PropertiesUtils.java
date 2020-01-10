package com.scau.myframework.mvc.util;

public class PropertiesUtils {
    private PropertiesUtils() {}

    public static String getBasePackage(){
        return  "com.scau";
    }

    public static String getJspPath() {
        return  "/WEB-INF/jsp/";
    }

    public static String getStaticPath() {
        return "/";
    }
}
