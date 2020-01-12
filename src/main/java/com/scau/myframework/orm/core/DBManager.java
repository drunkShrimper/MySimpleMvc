package com.scau.myframework.orm.core;



import com.scau.myframework.orm.entity.Configuration;
import com.scau.myframework.orm.util.PropertiesUtils;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息，维持连接对象的管理(增加连接池功能)
 *
 * @author lipan
 */
public class DBManager {

    public static Connection getConn() {
        try {
            Configuration configuration = PropertiesUtils.getConfiguration();
            Class.forName(configuration.getDriver());
            //直接建立连接，后期增加连接池处理，提高效率！！！
            return DriverManager.getConnection(configuration.getUrl(), configuration.getUser(), configuration.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(ResultSet rs, Statement ps, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(ps,conn);
    }

    public static void close(Statement ps, Connection conn) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(conn);
    }

    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
