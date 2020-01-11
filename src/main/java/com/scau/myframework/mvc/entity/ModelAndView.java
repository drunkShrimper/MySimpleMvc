package com.scau.myframework.mvc.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lipan
 * @time: 2019/10/26 13:23
 */
public class ModelAndView {


    private String viewName;
    private Map<String, Object> model;

    public ModelAndView() {
        this.model = new HashMap<String, Object>();
    }

    public ModelAndView(String viewName) {
        this.viewName = viewName;
        this.model = new HashMap<String, Object>();
    }

    public ModelAndView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public ModelAndView addObject(String key, Object value){
        model.put(key,value);
        return this;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
