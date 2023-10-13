package com.myshop.common.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="brands")
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false , length = 45, unique = true)
	private String name;
	
	@Column(length = 125)
	private String logo;
	
	@ManyToMany
	@JoinTable(
			name = "brands_categories",
			joinColumns = @JoinColumn(name="brand_id"),
			inverseJoinColumns = @JoinColumn(name="category_id")
			)
	private Set<Category> categories=new HashSet<>();

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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Brand(Integer id, String name, String logo, Set<Category> categories) {
		
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.categories = categories;
	}

	public Brand() {
		
	}

	public Brand(String name) {
	
		this.name = name;
		this.logo = "logo.png";
	}
	
	@Transient
	public String getImagePath() {
		return "/brand-logo/"+this.id+"/"+this.logo;
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", logo=" + logo + ", categories=" + categories + "]";
	}
	@Transient
	public List<String> categoriesName(){
		List<Category> categories=(List<Category>) this.categories;
		List<String>categoriesName=new ArrayList<>();
		for(Category c:categories) categoriesName.add(c.getName());
		return categoriesName;
	}
	
}
