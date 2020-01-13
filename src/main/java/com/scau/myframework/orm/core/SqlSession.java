package com.scau.myframework.orm.core;

import com.scau.myframework.orm.entity.ColumnInfo;
import com.scau.myframework.orm.entity.TableInfo;
import com.scau.myframework.orm.util.JDBCUtils;
import com.scau.myframework.orm.util.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 负责装配、执行sql
 *
 * @author lipan
 */
@SuppressWarnings("all")
public abstract class SqlSession implements Cloneable {

    /**
     * 直接执行一个DML语句
     *
     * @param sql    sql语句
     * @param params 参数
     * @return 执行sql语句后影响记录的行数
     */
    public int executeDML(String sql, Object[] params) {
        Connection conn = DBManager.getConnection();
        int count = 0;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);

            //给sql设参
            JDBCUtils.handleParams(ps, params);
            String logMsg = ps.toString().substring(ps.toString().indexOf(":") + 1, ps.toString().length());

            System.out.println("orm log----->" + logMsg);
            count = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(ps, conn);
        }

        return count;
    }

    /**
     * 将一个对象存储到数据库中
     * 把对象中不为null的属性往数据库中存储！如果数字为null则放0.
     *
     * @param obj 要存储的对象
     */
    public void insert(Object obj) {
        Class c = obj.getClass();
        //存储sql的参数对象
        List<Object> params = new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        StringBuilder sql = new StringBuilder("insert into " + tableInfo.getTableName() + " (");
        //计算不为null的属性值
        int countNotNullField = 0;
        Field[] fs = c.getDeclaredFields();
        for (Field f : fs) {
            String fieldName = f.getName();
            Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);

            if (fieldValue != null) {
                countNotNullField++;
                sql.append(fieldName + ",");
                params.add(fieldValue);
            }
        }

        sql.setCharAt(sql.length() - 1, ')');
        sql.append(" values (");
        for (int i = 0; i < countNotNullField; i++) {
            sql.append("?,");
        }
        sql.setCharAt(sql.length() - 1, ')');

        executeDML(sql.toString(), params.toArray());
    }

    /**
     * 删除clazz表示类对应的表中的记录(指定主键值id的记录)
     *
     * @param clazz 跟表对应的类的Class对象
     * @param id    主键的值
     */
    public void delete(Class clazz, Object id) {
        TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPrimaryKey();

        String sql = "delete from " + tableInfo.getTableName() + " where " + onlyPriKey.getColumnName() + "=? ";
        executeDML(sql, new Object[]{id});
    }

    /**
     * 删除对象在数据库中对应的记录(对象所在的类对应到表，对象的主键的值对应到记录)
     *
     * @param obj
     */
    public void delete(Object obj) {
        Class c = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPrimaryKey();

        //通过反射机制，调用属性对应的get方法或set方法
        Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getColumnName(), obj);
        delete(c, priKeyValue);
    }

    /**
     * 更新对象对应的记录，并且只更新指定的字段的值
     *
     * @param obj        所要更新的对象
     * @param fieldNames 更新的属性列表
     * @return 执行sql语句后影响记录的行数
     */
    public int update(Object obj, String[] fieldNames) {
        Class c = obj.getClass();
        List<Object> params = new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo priKey = tableInfo.getOnlyPrimaryKey();
        StringBuilder sql = new StringBuilder("update " + tableInfo.getTableName() + " set ");

        for (String fname : fieldNames) {
            Object fvalue = ReflectUtils.invokeGet(fname, obj);
            if (null != fvalue) {
                params.add(fvalue);
                sql.append(fname + "=?,");
            }

        }
        sql.setCharAt(sql.length() - 1, ' ');
        sql.append(" where ");
        sql.append(priKey.getColumnName() + "=? ");

        params.add(ReflectUtils.invokeGet(priKey.getColumnName(), obj));

        return executeDML(sql.toString(), params.toArray());
    }

    /**
     * 更新对象对应的记录
     *
     * @param obj 所要更新的对象
     * @return 执行sql语句后影响记录的行数
     */
    public int update(Object obj) {
        ArrayList<String> fieldNameList = new ArrayList<String>();
        Class c = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        Map<String, ColumnInfo> columns = tableInfo.getColumns();
        for (ColumnInfo column : columns.values()) {
            if (column.getKeyType() != 1) {
                fieldNameList.add(column.getColumnName());
            }
        }
        String[] fieldNames = fieldNameList.toArray(new String[0]);
        return update(obj, fieldNames);
    }

    /**
     * 查询返回多行记录，并将每行记录封装到clazz指定的类的对象中
     *
     * @param sql    查询语句
     * @param clazz  封装数据的javabean类的Class对象
     * @param params sql的参数
     * @return 查询到的结果
     */
    public List queryRows(String sql, Class clazz, Object[] params) {

        Connection conn = DBManager.getConnection();
        List list = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            //给sql设参
            JDBCUtils.handleParams(ps, params);
            String logMsg = ps.toString().substring(ps.toString().indexOf(":") + 1, ps.toString().length());
            System.out.println("orm log----->" + logMsg);
            rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next()) {
                if (list == null) {
                    list = new ArrayList();
                }
                Object rowObj = clazz.newInstance();

                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(i + 1);

                    //调用rowObj对象的setUsername(String uname)方法，将columnValue的值设置进去
                    ReflectUtils.invokeSet(rowObj, columnName, columnValue);
                }

                list.add(rowObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(ps, conn);
        }

        return list;
    }

    /**
     * 查询返回一行记录，并将该记录封装到clazz指定的类的对象中
     *
     * @param sql    查询语句
     * @param clazz  封装数据的javabean类的Class对象
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
        List list = queryRows(sql, clazz, params);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    /**
     * 根据id查询记录
     *
     * @param clazz 封装数据的javabean类的Class对象
     * @param id    主键的值
     * @return 查询到的结果
     */
    public Object queryById(Class clazz, Object id) {
        TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPrimaryKey();
        String sql = "select * from " + tableInfo.getTableName() + " where " + onlyPriKey.getColumnName() + "=? ";
        return queryUniqueRow(sql, clazz, new Object[]{id});
    }

    /**
     * 查询返回一个值(即：一行一列)，并将该值返回
     *
     * @param sql    查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Object queryValue(String sql, Object[] params) {
        Connection conn = DBManager.getConnection();
        Object value = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            //给sql设参
            JDBCUtils.handleParams(ps, params);
            String logMsg = ps.toString().substring(ps.toString().indexOf(":") + 1, ps.toString().length());
            System.out.println("orm log----->" + logMsg);
            rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(ps, conn);
        }

        return value;
    }

    /**
     * 查询返回一个数字(一行一列)，并将该值返回
     *
     * @param sql    查询语句
     * @param params sql的参数
     * @return 查询到的数字
     */
    public Number queryNumber(String sql, Object[] params) {
        return (Number) queryValue(sql, params);
    }

    /**
     * 分页查询
     *
     * @param pageNum 第几页数据
     * @param size    每页显示多少记录
     * @return
     */
    public abstract Object queryPagenate(int pageNum, int size);


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
