package com.myshop.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="provinces")
public class Province {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 45)
	private String name;
	
	@Column(nullable = false, length = 5)
	private String code;
	
	@OneToMany(mappedBy = "province")
	private Set<District> districts;

	public Province() {

	}
	
	public Province(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Province(Integer id) {
		this.id = id;
	}

	public Province(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public Province(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public Province(String name) {
		this.name = name;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	

	
	@Override
	public String toString() {
		return "Province [id=" + id + ", name=" + name + ", code=" + code ;
	}
	
	
}
