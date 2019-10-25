package com.scau.myframework.ioc.reader.impl;

import com.scau.myframework.ioc.entity.BeanInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class BeanInfoHandler extends DefaultHandler {

    private List<BeanInfo> beanInfos;
    private BeanInfo beanInfo;
    private String tag;
    private String propertyKey;

    public List<BeanInfo> getBeanInfos() {
        return beanInfos;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        beanInfos = new ArrayList<BeanInfo>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(qName != null){
            tag = qName;
        }
        if(qName != null && qName.equals("bean")){
            beanInfo = new BeanInfo();
            beanInfo.setId(attributes.getValue("id"));
            beanInfo.setType(attributes.getValue("type"));
        }
        if(qName != null && qName.equals("property")){
            propertyKey = attributes.getValue("name");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (null != tag && tag.equals("property")){
            beanInfo.addProperty(propertyKey,new String(ch,start,length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(qName.equals("bean")){
            beanInfos.add(beanInfo);
        }
        tag = null;
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
