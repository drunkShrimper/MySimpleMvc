package com.scau.myframework.orm.test.bean;

public class Department {

	private String address;
	private String department_name;
	private Integer id;


	public String getAddress(){
		return this.address;
	}
	public String getDepartment_name(){
		return this.department_name;
	}
	public Integer getId(){
		return this.id;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setDepartment_name(String department_name){
		this.department_name=department_name;
	}
	public void setId(Integer id){
		this.id=id;
	}
}
