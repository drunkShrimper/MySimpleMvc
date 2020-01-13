package com.scau.myframework.orm.core;

import com.scau.myframework.orm.util.PropertiesUtils;

/**
 * @description:
 * @author: lipan
 * @time: 2020/1/12 18:30
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private static SqlSession prototypeObj;

    static {
        try {
            String usingDB = PropertiesUtils.getConfiguration().getUsingDB();
            if ("mysql".equals(usingDB)) {
                prototypeObj = new MysqlSqlSession();
            } else if ("orale".equals(usingDB)) {
                prototypeObj = new OracleSqlSession();
            } else {
                prototypeObj = new MysqlSqlSession();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TableContext.doJavaBeanToTableMapping();
    }

    @Override
    public SqlSession openSession() {
        try {
            return (SqlSession) prototypeObj.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
