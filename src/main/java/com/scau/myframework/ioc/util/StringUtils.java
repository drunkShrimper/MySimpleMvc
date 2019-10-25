package com.scau.myframework.ioc.util;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

public class StringUtils {
    private StringUtils(){}

    static Set<Class<?>> classSet = new HashSet<Class<?>>();

    static Set<Class<?>> classSet2 = new HashSet<Class<?>>();

    static {
        classSet.add(Byte.class);
        classSet.add(Character.class);
        classSet.add(String.class);
        classSet.add(Short.class);
        classSet.add(Integer.class);
        classSet.add(Boolean.class);
        classSet.add(Long.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet2.add(int.class);
        classSet2.add(byte.class);
        classSet2.add(char.class);
        classSet2.add(short.class);
        classSet2.add(long.class);
        classSet2.add(float.class);
        classSet2.add(double.class);
    }

    public static String getSimpleName(String str){
        return str.substring(str.lastIndexOf(".")+1);
    }

    public static String firstCharToUp(String property) {
        return (property.substring(0, 1).toUpperCase()) + property.substring(1);
    }

    public static Object getBasicInstanceByString(Class<?> cls,String value){
        if (!cls.equals(String.class) && (value == null || "".equals(value))){
            throw new RuntimeException("convert value is null");
        }
        if (classSet.contains(cls)){
            Object instance;
            try {
                Constructor constructor = cls.getConstructor(String.class);
                instance = constructor.newInstance(value);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            return instance;
        }else {
            Object data;
            if (cls.equals(int.class)) {
                data = Integer.parseInt(value);
            } else if (cls.equals(float.class)) {
                data = Float.parseFloat(value);
            } else if (cls.equals(byte.class)) {
                data = Byte.parseByte(value);
            } else if (cls.equals(char.class)) {
                data = value.toCharArray()[0];
            } else if (cls.equals(long.class)) {
                data = Long.parseLong(value);
            } else if (cls.equals(double.class)) {
                data = Double.parseDouble(value);
            } else {
                data = Short.parseShort(value);
            }
            return data;
        }
    }
}
