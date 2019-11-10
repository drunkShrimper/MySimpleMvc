package com.scau.myframework.test.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.scau.myframework.mvc.annotation.MyAutowired;
import com.scau.myframework.mvc.annotation.MyController;
import com.scau.myframework.mvc.annotation.MyRequestMapping;
import com.scau.myframework.mvc.annotation.MyRequestParam;
import com.scau.myframework.test.entity.User;
import com.scau.myframework.test.service.UserService;

@MyController("UserController")
@MyRequestMapping("/user")
public class UserController {

    @MyAutowired("UserServiceImpl")
    private UserService userService;

    @MyRequestMapping("/findOne")
    public User findOne(HttpServletRequest request, HttpServletResponse response, User user){

        return user;
    }

    @MyRequestMapping("/show")
    public String show(HttpServletRequest request, HttpServletResponse response){

        return "redirect:/pages/show.html";
    }
}
