package com.scau.myframework.orm.test.dao;

import com.scau.myframework.orm.generator.JavaBeanFileGenerator;
import org.junit.Test;

/**
 * @description:
 * @author: lipan
 * @time: 2020/1/13 14:14
 */
public class GeneratorTest {
    @Test
    public void generatorTest(){
        JavaBeanFileGenerator.generate();
    }
}
