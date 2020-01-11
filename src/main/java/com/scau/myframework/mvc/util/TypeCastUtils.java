package com.scau.myframework.mvc.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @description: 完成请求参数的类型转换
 * @author: lipan
 * @time: 2019/11/10 11:05
 */
public class TypeCastUtils {


    static Set<Class<?>> wrapperTypes = new HashSet<Class<?>>();

    static {
        wrapperTypes.add(Byte.class);
        wrapperTypes.add(Character.class);
        wrapperTypes.add(String.class);
        wrapperTypes.add(Short.class);
        wrapperTypes.add(Integer.class);
        wrapperTypes.add(Long.class);
        wrapperTypes.add(Float.class);
        wrapperTypes.add(Double.class);
    }

    public static Object getBasicInstanceByString(Class<?> cls,String value){
        if (!cls.equals(String.class) && (value == null || "".equals(value))){
            throw new RuntimeException("convert value is null");
        }
        if (wrapperTypes.contains(cls)){
            Object instance = null;
            try {
                Constructor constructor = cls.getConstructor(String.class);
                instance = constructor.newInstance(value);
            }catch (Exception e){
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

    public static Object getPojoInstance(Class<?> clazz, Map<String,String[]> parameterMap) {
        Object instance = ReflectionUtils.newInstance(clazz);

        Method[] methods = clazz.getMethods();
        for (Method method : methods){

            String methodName = method.getName();
            String paramName = null;
            //取得setxxx或是isxxx的方法
            if (methodName.startsWith("set")||methodName.startsWith("is")){
                if(methodName.startsWith("set")){
                    paramName = methodName.replace(methodName.substring(0,4),methodName.substring(3,4).toLowerCase());
                }else {
                    paramName = methodName.replace(methodName.substring(0,3),methodName.substring(2,3).toLowerCase());
                }
                //获取该方法的参数类型
                Class<?>[] paramTypes = method.getParameterTypes();

                String paramStr = parameterMap.get(paramName)[0];

                Object param = getBasicInstanceByString(paramTypes[0],paramStr );
                ReflectionUtils.invokeMethod(instance,method,param);
            }
        }
        return instance;
    }
}
