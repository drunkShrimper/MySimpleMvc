package com.scau.myframework.test.service;


import com.scau.myframework.test.entity.User;

public interface UserService {
    User findOne(String name, int age);
}
