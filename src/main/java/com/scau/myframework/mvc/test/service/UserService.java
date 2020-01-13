package com.scau.myframework.mvc.test.service;


import com.scau.myframework.mvc.test.entity.User;

public interface UserService {
    User findOne(String name, Integer age);
}
