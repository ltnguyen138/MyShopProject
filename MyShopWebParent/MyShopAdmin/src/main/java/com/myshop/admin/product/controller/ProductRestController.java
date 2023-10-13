package com.myshop.admin.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.admin.product.ProductService;

@RestController
public class ProductRestController {

	@Autowired
	ProductService productService;
	
	@PostMapping("/products/check_unique")
	public String checkUnique(@Param("id")Integer id,@Param("name")String name,@Param("alias")String alias ) {
		
		return productService.checkUnique(id, name, alias);
	}
	}
