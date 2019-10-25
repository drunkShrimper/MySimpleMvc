package com.scau.myframework.ioc.context;


import com.scau.myframework.ioc.factory.AbstractBeanFactory;
import com.scau.myframework.ioc.reader.SourceReader;
import com.scau.myframework.ioc.reader.impl.XMLSourceReader;

public class XMLContext extends AbstractBeanFactory {
    public XMLContext(String filePath) {
        super(filePath);
        this.setSourceReader(new XMLSourceReader());
        this.registerBeans();
    }

    @Override
    protected void setSourceReader(SourceReader reader) {
        this.reader = reader;
    }
}
