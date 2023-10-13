package com.myshop.admin.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.admin.category.CategoryService;

@RestController
public class CategoryRestController {

	@Autowired
	CategoryService categoryService;
	
	@PostMapping("/categories/check_unique")
	public String checkUnique(@Param("id")Integer id,@Param("name")String name,@Param("alias")String alias ) {
		
		return categoryService.checkUnique(id, name, alias);
	}
}
