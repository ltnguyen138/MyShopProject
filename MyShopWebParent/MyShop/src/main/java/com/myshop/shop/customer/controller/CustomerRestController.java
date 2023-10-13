package com.myshop.shop.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.shop.customer.CustomerNotFoundException;
import com.myshop.shop.customer.CustomerService;

@RestController
public class CustomerRestController {
	
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/customers/check_email")
	public String checkUniqueEmail(@Param("email") String email,@Param("id") Integer id) throws CustomerNotFoundException {
		
		return customerService.isEmailUnique(email,id)?"OK": "Duplicate";
	}
}
