package com.scau.myframework.orm.core;

import com.scau.myframework.orm.entity.ColumnInfo;
import com.scau.myframework.orm.entity.TableInfo;
import com.scau.myframework.orm.util.JDBCUtils;
import com.scau.myframework.orm.util.ReflectUtils;
import com.scau.myframework.test.vo.EmpVO;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lipan
 * @time: 2020/1/12 20:37
 */
public class MysqlSqlExecutor implements SqlExecutor {

    public static void main(String[] args) {
		Number obj = (Number)new MysqlSqlExecutor().queryValue("select count(*) from Employee where salary>?",new Object[]{500});
        //Number obj = new MysqlSqlExecutor().queryNumber("select count(*) from Employee where salary>?", new Object[]{1000});
        System.out.println(obj.longValue());

//        String sql2 = "select e.id,e.name,salary+bonus 'xinshui',age,d.department_name 'deptName',d.address 'deptAddr' from employee e "
//                +"join department d on e.d_id=d.id ";
//        List<EmpVO> list2 = new MysqlSqlExecutor().queryRows(sql2, EmpVO.class, null);
//
//        for(EmpVO e:list2){
//            System.out.println(e.getName()+"-"+e.getDeptAddr()+"-"+e.getXinshui());
//        }
    }

    @Override
    public void delete(Class clazz, Object id) {

        TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPrimaryKey();

        String sql = "delete from " + tableInfo.getTableName() + " where " + onlyPriKey.getColumnName() + "=? ";
        executeDML(sql, new Object[]{id});
    }

    @Override
    public void delete(Object obj) {
        Class c = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPrimaryKey();

        //通过反射机制，调用属性对应的get方法或set方法
        Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getColumnName(), obj);
        delete(c, priKeyValue);
    }

    @Override
    public int executeDML(String sql, Object[] params) {
        Connection conn = DBManager.getConn();
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

    @Override
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

    @Override
    public Number queryNumber(String sql, Object[] params) {
        return (Number) queryValue(sql, params);
    }

    @Override
    public List queryRows(String sql, Class clazz, Object[] params) {

        Connection conn = DBManager.getConn();
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

    @Override
    public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
        List list = queryRows(sql, clazz, params);
        return (list == null && list.size() > 0) ? null : list.get(0);
    }

    @Override
    public Object queryValue(String sql, Object[] params) {
        Connection conn = DBManager.getConn();
        Object value = null;    //存储查询结果的对象
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

    @Override
    public int update(Object obj, String[] fieldNames) {

        Class c = obj.getClass();
        List<Object> params = new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo priKey = tableInfo.getOnlyPrimaryKey();
        StringBuilder sql = new StringBuilder("update " + tableInfo.getTableName() + " set ");

        for (String fname : fieldNames) {
            Object fvalue = ReflectUtils.invokeGet(fname, obj);
            params.add(fvalue);
            sql.append(fname + "=?,");
        }
        sql.setCharAt(sql.length() - 1, ' ');
        sql.append(" where ");
        sql.append(priKey.getColumnName() + "=? ");

        params.add(ReflectUtils.invokeGet(priKey.getColumnName(), obj));

        return executeDML(sql.toString(), params.toArray());
    }

    @Override
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
}
