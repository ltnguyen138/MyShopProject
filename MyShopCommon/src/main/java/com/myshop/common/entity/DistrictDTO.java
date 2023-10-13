package com.myshop.common.entity;

public class DistrictDTO {
	private Integer id;
	private String name;
	public DistrictDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	public DistrictDTO() {
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
	
	
}
