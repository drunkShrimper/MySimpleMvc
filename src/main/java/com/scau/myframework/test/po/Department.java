package com.scau.myframework.test.po;

public class Department {

	private String department_name;
	private Integer id;


	public String getDepartment_name(){
		return this.department_name;
	}
	public Integer getId(){
		return this.id;
	}
	public void setDepartment_name(String department_name){
		this.department_name=department_name;
	}
	public void setId(Integer id){
		this.id=id;
	}
}
