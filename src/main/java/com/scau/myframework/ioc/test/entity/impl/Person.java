package com.scau.myframework.ioc.test.entity.impl;


import com.scau.myframework.ioc.test.entity.Speakable;

public class Person implements Speakable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void speak(String message) {
        System.out.println(this.name + " say: " + message);
    }
}
