package com.scau.lipan.controller;

import com.scau.lipan.annotation.MyAutowired;
import com.scau.lipan.annotation.MyController;
import com.scau.lipan.annotation.MyRequestMapping;
import com.scau.lipan.annotation.MyRequestParam;
import com.scau.lipan.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@MyController("UserController")
@MyRequestMapping("/user")
public class UserController {

    @MyAutowired("UserServiceImpl")
    private UserService userService;

    @MyRequestMapping("/findOne")
    public void findOne(HttpServletRequest request, HttpServletResponse response,
                        @MyRequestParam("name")String name, @MyRequestParam("age")String age) throws IOException {

        PrintWriter pw = response.getWriter();

        String user = userService.findOne(name, age);

        pw.write(user);

    }
}
