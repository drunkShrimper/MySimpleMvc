package com.scau.myframework.test.service.impl;


import com.scau.myframework.mvc.annotation.MyService;
import com.scau.myframework.test.service.UserService;

@MyService("UserServiceImpl")
public class UserServiceImpl implements UserService {


    @Override
    public String findOne(String name, String age) {
        return "name=zhang  age=23";
    }
}
