package com.scau.myframework.test.service.impl;


import com.scau.myframework.mvc.annotation.MyService;
import com.scau.myframework.test.entity.User;
import com.scau.myframework.test.service.UserService;

@MyService("UserServiceImpl")
public class UserServiceImpl implements UserService {


    @Override
    public User findOne(String name, int age) {
        return new User(name,age);
    }
}
