package com.myshop.shop.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.myshop.shop.category.CategoryService;

@Controller
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	
}
