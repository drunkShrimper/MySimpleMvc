package com.scau.myframework.test.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.scau.myframework.mvc.annotation.MyAutowired;
import com.scau.myframework.mvc.annotation.MyController;
import com.scau.myframework.mvc.annotation.MyRequestMapping;
import com.scau.myframework.mvc.annotation.MyRequestParam;
import com.scau.myframework.test.service.UserService;

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
