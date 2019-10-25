package com.scau.myframework.ioc.entity;

import java.util.HashMap;
import java.util.Map;

public class BeanInfo {
    private String id;
    private String type;
    private Map<String,Object> properties = new HashMap<String,Object>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void addProperty(String name,Object value){
        this.properties.put(name,value);
    }
}


