package com.scau.myframework.ioc.reader.impl;


import com.scau.myframework.ioc.entity.BeanInfo;
import com.scau.myframework.ioc.reader.SourceReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO 此处要实现xml文件的读取，目前只会sax解析
public class XMLSourceReader implements SourceReader {
    @Override
    public Map<String, BeanInfo> loadBeans(String filePath) {



        try {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            BeanInfoHandler handler = new BeanInfoHandler();
            saxParser.parse(filePath,handler);

            List<BeanInfo> beanInfos = handler.getBeanInfos();


            Map<String,BeanInfo> beans = new HashMap<String,BeanInfo>();

            for (BeanInfo b:beanInfos) {
                beans.put(b.getId(),b);
            }

            return beans;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return null;
    }
}
