package com.scau.myframework.test.controller;


import com.scau.myframework.mvc.annotation.MyAutowired;
import com.scau.myframework.mvc.annotation.MyController;
import com.scau.myframework.mvc.annotation.MyRequestMapping;
import com.scau.myframework.mvc.entity.ModelAndView;
import com.scau.myframework.test.entity.User;
import com.scau.myframework.test.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MyController("UserController")
@MyRequestMapping("/user")
public class UserController {

    @MyAutowired("UserServiceImpl")
    private UserService userService;

    @MyRequestMapping("/findOne")
    public User findOne(HttpServletRequest request, HttpServletResponse response){
        User user = new User();
        user.setAge(123);
        user.setName("sdfds");
        return user;
    }

    @MyRequestMapping("/showmv")
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response){

        ModelAndView mv = new ModelAndView();
        mv.setPath("show.jsp");
        return mv;
    }

    @MyRequestMapping("/showpage")
    public String showPage(HttpServletRequest request, HttpServletResponse response){

        return "show.jsp";
    }

    @MyRequestMapping("/showjpg")
    public String showjpg(HttpServletRequest request, HttpServletResponse response){

        return "1.jpg";
    }
}
