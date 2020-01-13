package com.scau.myframework.orm.test.bean;

public class Employee {

	private Double bonus;
	private String name;
	private Integer d_id;
	private Integer id;
	private Double salary;
	private java.sql.Date age;


	public Double getBonus(){
		return this.bonus;
	}
	public String getName(){
		return this.name;
	}
	public Integer getD_id(){
		return this.d_id;
	}
	public Integer getId(){
		return this.id;
	}
	public Double getSalary(){
		return this.salary;
	}
	public java.sql.Date getAge(){
		return this.age;
	}
	public void setBonus(Double bonus){
		this.bonus=bonus;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setD_id(Integer d_id){
		this.d_id=d_id;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setSalary(Double salary){
		this.salary=salary;
	}
	public void setAge(java.sql.Date age){
		this.age=age;
	}
}
