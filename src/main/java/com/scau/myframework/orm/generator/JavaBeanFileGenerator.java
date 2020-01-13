package com.scau.myframework.orm.generator;

import com.scau.myframework.orm.convertor.TypeConvertor;
import com.scau.myframework.orm.convertor.impl.MySqlTypeConvertor;
import com.scau.myframework.orm.core.DBManager;
import com.scau.myframework.orm.core.TableContext;
import com.scau.myframework.orm.entity.ColumnInfo;
import com.scau.myframework.orm.entity.TableInfo;
import com.scau.myframework.orm.util.PropertiesUtils;
import com.scau.myframework.orm.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


/**
 * 生成JavaBean文件(源代码)
 *
 * @author lipan
 */
public class JavaBeanFileGenerator {

    private JavaBeanFileGenerator() {
    }

    /**
     * 根据表信息生成javabean的源代码并写入配置文件db.properties中PoPackage属性指定的包下
     *
     */
    public static void generate() {

        Map<String, TableInfo> map = TableContext.tables;
        for (TableInfo t : map.values()) {
            generateJavaBeanFile(t, new MySqlTypeConvertor());
        }
    }

    /**
     * 根据表信息生成java类的源代码并写入配置文件db.properties中PoPackage属性指定的包下
     *
     * @param tableInfo 数据库中的表信息
     * @param convertor 数据类型转化器
     */
    private static void generateJavaBeanFile(TableInfo tableInfo, TypeConvertor convertor) {

        String srcCodeStr = createJavaSrcCodeStr(tableInfo, convertor);

        String srcPath = PropertiesUtils.getConfiguration().getSrcPath() + "\\";
        String packagePath = PropertiesUtils.getConfiguration().getPoPackage().replaceAll("\\.", "/");

        File f = new File(srcPath + packagePath);

        //如果指定目录不存在，则帮助用户建立
        if (!f.exists()) {
            f.mkdirs();
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(f.getAbsoluteFile() + "/" + StringUtils.firstChar2UpperCase(tableInfo.getTableName()) + ".java"));
            bw.write(srcCodeStr);
            System.out.println("orm log----->更新表" + tableInfo.getTableName() + "对应的java类：" + StringUtils.firstChar2UpperCase(tableInfo.getTableName()) + ".java");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据表信息生成java类的源代码
     *
     * @param tableInfo 表信息
     * @param convertor 数据类型转化器
     * @return java类的源代码
     */
    private static String createJavaSrcCodeStr(TableInfo tableInfo, TypeConvertor convertor) {

        Map<String, ColumnInfo> columns = tableInfo.getColumns();

        StringBuilder srcCodeStr = new StringBuilder();

        //生成package语句
        srcCodeStr.append("package " + PropertiesUtils.getConfiguration().getPoPackage() + ";\n\n");
        //生成import语句
        //srcCodeStr.append("import java.sql.*;\n");
        //srcCodeStr.append("import java.util.*;\n\n");
        //生成类声明语句
        srcCodeStr.append("public class " + StringUtils.firstChar2UpperCase(tableInfo.getTableName()) + " {\n\n");

        //生成属性列表
        for (ColumnInfo c : columns.values()) {
            srcCodeStr.append(createFieldSrcCodeStr(c, convertor));
        }
        srcCodeStr.append("\n\n");
        //生成get方法列表
        for (ColumnInfo c : columns.values()) {
            srcCodeStr.append(createGetMethodSrcCodeStr(c, convertor));
        }
        //生成set方法列表
        for (ColumnInfo c : columns.values()) {
            srcCodeStr.append(createSetMethodSrcCodeStr(c, convertor));
        }

        //生成类结束符号
        srcCodeStr.append("}\n");
        return srcCodeStr.toString();
    }

    /**
     * 生成属性声明源码
     *
     * @param column    字段信息
     * @param convertor 类型转化器
     * @return 属性声明源码
     */
    private static String createFieldSrcCodeStr(ColumnInfo column, TypeConvertor convertor) {

        String fieldSrcCodeStr = "";

        String javaFieldType = convertor.databaseType2JavaType(column.getDataType());

        fieldSrcCodeStr = "\tprivate " + javaFieldType + " " + column.getColumnName() + ";\n";

        return fieldSrcCodeStr;
    }

    /**
     * 生成get方法的源代码
     *
     * @param column    字段信息
     * @param convertor 类型转化器
     * @return get方法源码
     */
    private static String createGetMethodSrcCodeStr(ColumnInfo column, TypeConvertor convertor) {

        StringBuilder getMethodSrcCodeStr = new StringBuilder();

        String javaFieldType = convertor.databaseType2JavaType(column.getDataType());

        getMethodSrcCodeStr.append("\tpublic " + javaFieldType + " get" + StringUtils.firstChar2UpperCase(column.getColumnName()) + "(){\n");
        getMethodSrcCodeStr.append("\t\treturn this." + column.getColumnName() + ";\n");
        getMethodSrcCodeStr.append("\t}\n");

        return getMethodSrcCodeStr.toString();
    }

    /**
     * 生成set方法的源代码
     *
     * @param column    字段信息
     * @param convertor 类型转化器
     * @return set方法源码
     */
    private static String createSetMethodSrcCodeStr(ColumnInfo column, TypeConvertor convertor) {

        StringBuilder setMethodSrcCodeStr = new StringBuilder();

        String javaFieldType = convertor.databaseType2JavaType(column.getDataType());

        setMethodSrcCodeStr.append("\tpublic void set" + StringUtils.firstChar2UpperCase(column.getColumnName()) + "(");
        setMethodSrcCodeStr.append(javaFieldType + " " + column.getColumnName() + "){\n");
        setMethodSrcCodeStr.append("\t\tthis." + column.getColumnName() + "=" + column.getColumnName() + ";\n");
        setMethodSrcCodeStr.append("\t}\n");

        return setMethodSrcCodeStr.toString();
    }
}
