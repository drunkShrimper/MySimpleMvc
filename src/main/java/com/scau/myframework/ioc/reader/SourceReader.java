package com.scau.myframework.ioc.reader;

import com.scau.myframework.ioc.entity.BeanInfo;


import java.util.Map;

public interface SourceReader {
    Map<String, BeanInfo> loadBeans(String filePath);
}
