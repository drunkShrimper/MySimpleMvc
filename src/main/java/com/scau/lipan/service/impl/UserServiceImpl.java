package com.scau.lipan.service.impl;


import com.scau.lipan.annotation.MyService;
import com.scau.lipan.service.UserService;

@MyService("UserServiceImpl")
public class UserServiceImpl implements UserService {


    @Override
    public String findOne(String name, String age) {
        return "name=zhang  age=23";
    }
}
