package com.scau.myframework.mvc.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里逻辑好乱，先将就着用
 */
public class ClassNameScanner {

    ArrayList<String> classNames = null;

    public   ArrayList<String> scan(String basePackage){

        classNames = new ArrayList<String>();
        doScan(basePackage);

        return classNames;
    }

    private void doScan(String basePackage){
        URL url = this.getClass().getClassLoader().getResource("/"+basePackage.replaceAll("\\.","/"));
        String fileStr = url.getFile();
        File file = new File(fileStr);

        String[] filesStr = file.list();

        for(String path:filesStr){
            File filePath = new File(fileStr + path);
            if(filePath.isDirectory()){
                doScan(basePackage+"."+path);
            }else{
                classNames.add(basePackage+"."+filePath.getName());
            }
        }
    }
}
