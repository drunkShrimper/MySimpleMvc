package com.scau.myframework.orm.core;

import com.scau.myframework.orm.convertor.impl.MySqlTypeConvertor;
import com.scau.myframework.orm.entity.ColumnInfo;
import com.scau.myframework.orm.entity.TableInfo;
import com.scau.myframework.orm.generator.JavaBeanFileGenerator;
import com.scau.myframework.orm.util.PropertiesUtils;
import com.scau.myframework.orm.util.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构。
 *
 * @author lipan
 */
public class TableContext {

    /**
     * 表名为key，表信息对象为value
     */
    public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();

    /**
     * 将po的class对象和表信息对象关联起来，便于重用！
     */
    public static Map<Class, TableInfo> poClassTableMap = new HashMap<Class, TableInfo>();

    private TableContext() {
    }

    static {
        try {
            //初始化获得表的信息
            Connection con = DBManager.getConn();
            DatabaseMetaData dbmd = con.getMetaData();

            ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});

            while (tableRet.next()) {
                String tableName = (String) tableRet.getObject("TABLE_NAME");

                TableInfo tableInfo = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String, ColumnInfo>());
                tables.put(tableName, tableInfo);
                //查询表中的所有字段
                ResultSet set = dbmd.getColumns(null, "%", tableName, "%");
                while (set.next()) {
                    String columnName = set.getString("COLUMN_NAME");
                    ColumnInfo columnInfo = new ColumnInfo(columnName, set.getString("TYPE_NAME"), 0);
                    tableInfo.getColumns().put(columnName, columnInfo);
                }
                //查询t_user表中的主键
                ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);
                while (set2.next()) {
                    ColumnInfo ci2 = (ColumnInfo) tableInfo.getColumns().get(set2.getObject("COLUMN_NAME"));
                    //设置为主键类型
                    ci2.setKeyType(1);
                    tableInfo.getPrimaryKeys().add(ci2);
                }
                //取唯一主键。。方便使用。如果是联合主键。则为空！
                if (tableInfo.getPrimaryKeys().size() > 0) {
                    tableInfo.setOnlyPrimaryKey(tableInfo.getPrimaryKeys().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //每次启动都自动将JavaBean同与数据库表同步
        generateJavaBeanFiles();

        //加载po包下面所有的类，便于重用，提高效率！
        doJavaBeanToTableMapping();
    }

    /**
     * 根据表结构，更新配置的po包下面的java类
     * 实现了从表结构转化到类结构
     */
    public static void generateJavaBeanFiles() {
        Map<String, TableInfo> map = TableContext.tables;
        for (TableInfo t : map.values()) {
            JavaBeanFileGenerator.generateJavaBeanFile(t, new MySqlTypeConvertor());
        }
    }

    /**
     * 将po的class对象和表信息对象关联起来，便于重用！
     */
    public static void doJavaBeanToTableMapping() {

        for (TableInfo tableInfo : tables.values()) {
            try {
                Class c = Class.forName(PropertiesUtils.getConfiguration().getPoPackage()
                        + "." + StringUtils.firstChar2UpperCase(tableInfo.getTableName()));
                poClassTableMap.put(c, tableInfo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
