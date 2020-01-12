package com.scau.myframework.test.vo;

import java.sql.Date;

public class EmpVO {
	
	private Integer id;
	private String name;
	private Double xinshui;
	private Date age;
	private String deptName;
	private String deptAddr;
	
	
	public EmpVO(Integer id, String name, Double xinshui, Date age,
				 String deptName, String deptAddr) {
		super();
		this.id = id;
		this.name = name;
		this.xinshui = xinshui;
		this.age = age;
		this.deptName = deptName;
		this.deptAddr = deptAddr;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getXinshui() {
		return xinshui;
	}
	public void setXinshui(Double xinshui) {
		this.xinshui = xinshui;
	}
	public Date getAge() {
		return age;
	}
	public void setAge(Date age) {
		this.age = age;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptAddr() {
		return deptAddr;
	}
	public void setDeptAddr(String deptAddr) {
		this.deptAddr = deptAddr;
	}
	
	public EmpVO() {
	}
	
}
