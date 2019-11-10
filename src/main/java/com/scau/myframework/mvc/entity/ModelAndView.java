package com.scau.myframework.mvc.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lipan
 * @time: 2019/10/26 13:23
 */
public class ModelAndView {


    private String path;

    private Map<String, Object> models;

    public ModelAndView() {
        this.models = new HashMap<String, Object>();
    }

    public ModelAndView(String path) {
        this.path = path;
        this.models = new HashMap<String, Object>();
    }

    public ModelAndView addModel(String key, Object value){
        models.put(key,value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModels() {
        return models;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setModels(Map<String, Object> models) {
        this.models = models;
    }
}
