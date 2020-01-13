package com.scau.myframework.orm.pool;

import com.scau.myframework.orm.core.DBManager;
import com.scau.myframework.orm.entity.Configuration;
import com.scau.myframework.orm.util.PropertiesUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @description: 连接池
 * @author: lipan
 * @time: 2020/1/12 18:30
 */
public class ConnectionPool {
	/**
	 * 连接池对象
	 */
	private  List<Connection> pool;  
	
	/**
	 * 最大连接数
	 */
	private static final Integer POOL_MAX_SIZE = PropertiesUtils.getConfiguration().getPoolMaxSize();
	/**
	 * 最小连接池
	 */
	private static final Integer POOL_MIN_SIZE = PropertiesUtils.getConfiguration().getPoolMinSize();
	
	
	/**
	 * 初始化连接池，使池中的连接数达到最小值
	 */
	public void initPool() {
		if(pool==null){
			pool = new ArrayList<Connection>();
		}
		
		while(pool.size()< ConnectionPool.POOL_MIN_SIZE){
			pool.add(this.createConnection());
			System.out.println("orm log----->" +"初始化池，池中连接数："+pool.size());
		}
	}
	
	
	/**
	 * 从连接池中取出一个连接
	 * @return
	 */
	public synchronized Connection getConnection() {
		if(pool.isEmpty()){
			return createConnection();
		}else{
			int lastIndex = pool.size()-1;
			Connection conn = pool.get(lastIndex);
			pool.remove(lastIndex);
			return conn;
		}
	}
	
	/**
	 * 将连接放回池中
	 * @param conn
	 */
	public synchronized void close(Connection conn){
		
		if(pool.size()>=POOL_MAX_SIZE){
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			pool.add(conn);
		}
	}

	private  Connection createConnection() {
		try {
			Configuration configuration = PropertiesUtils.getConfiguration();
			Class.forName(configuration.getDriver());
			return DriverManager.getConnection(configuration.getUrl(), configuration.getUser(), configuration.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ConnectionPool() {
		initPool();
	}
	
}
