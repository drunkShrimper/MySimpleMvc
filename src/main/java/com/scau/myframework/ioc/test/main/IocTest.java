package com.scau.myframework.ioc.test.main;


import com.scau.myframework.ioc.context.XMLContext;
import com.scau.myframework.ioc.factory.BeanFactory;
import com.scau.myframework.ioc.test.entity.Speakable;
import com.scau.myframework.ioc.test.entity.impl.Student;

public class IocTest {
    public static void main(String[] args) {

        //如果想写的完整点，还要考虑对象类型中的对象类型如何进行注入
        BeanFactory factory = new XMLContext("src/main/java/com/scau/myframework/ioc/test/resources/bean.xml");
        Speakable person = (Speakable)factory.getBean("Person");
        person.speak("Lesson one!");

        Speakable student = (Student) factory.getBean("Student");
        student.speak("");
    }
}
