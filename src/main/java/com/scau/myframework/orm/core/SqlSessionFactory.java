package com.scau.myframework.orm.core;

/**
 * @description:
 * @author: lipan
 * @time: 2020/1/12 18:30
 */
public interface SqlSessionFactory {

	/**
	 *
	 * @return
	 */
	public SqlSession openSession();

}
