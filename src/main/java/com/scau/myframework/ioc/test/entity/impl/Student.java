package com.scau.myframework.ioc.test.entity.impl;


import com.scau.myframework.ioc.test.entity.Speakable;

public class Student implements Speakable {
    private String name;
    private Integer address;

    public Student() {
    }

    public Student(String name, Integer address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    @Override
    public void speak(String message) {
        System.out.println(name + " " + address);
    }
}
