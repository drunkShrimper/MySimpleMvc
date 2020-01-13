package com.scau.myframework.orm.test.dao;

import com.scau.myframework.orm.core.MysqlSqlExecutor;
import com.scau.myframework.orm.core.SqlExecutor;
import com.scau.myframework.orm.test.bean.Employee;
import com.scau.myframework.orm.test.vo.EmpVO;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

/**
 * @description:
 * @author: lipan
 * @time: 2020/1/13 10:37
 */
public class SqlExecutorTest {

    SqlExecutor sqlExecutor = new MysqlSqlExecutor();

    @Test
    public void deleteTest() {
        Employee employee = new Employee();
        employee.setId(3);
        sqlExecutor.delete(employee);

        sqlExecutor.delete(Employee.class, 2);
    }

    @Test
    public void insertTest() {
        Employee employee = new Employee();
        employee.setId(5);
        employee.setBonus(5000.0);
        sqlExecutor.insert(employee);
    }

    @Test
    public void updateTest() {
        Employee employee = new Employee();
        employee.setId(4);
        employee.setBonus(2000.0);
        employee.setName("zhang");
        employee.setSalary(888.5);
        sqlExecutor.update(employee, new String[]{"name", "salary"});
        sqlExecutor.update(employee);
    }

    @Test
    public void queryRowsTest() {
        String sql2 = "select e.id,e.name,salary+bonus 'xinshui',age,d.department_name 'deptName',d.address 'deptAddr' from employee e "
                + "join department d on e.d_id=d.id ";
        List<EmpVO> list2 = new MysqlSqlExecutor().queryRows(sql2, EmpVO.class, null);

        for (EmpVO e : list2) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void queryUniqueRowTest() {
        Employee e = (Employee) sqlExecutor.queryUniqueRow("select * from Employee where id=?", Employee.class, new Object[]{2});
        System.out.println(e);
    }

    @Test
    public void queryNumberTest() {
        Number obj = sqlExecutor.queryNumber("select count(*) from Employee where salary>?", new Object[]{500});
        System.out.println(obj.longValue());
    }

    @Test
    public void queryValueTest() {
        Date obj = (Date) sqlExecutor.queryValue("select age from Employee where id=?", new Object[]{4});
        System.out.println(obj);
    }
}
