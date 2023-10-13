package com.myshop.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.admin.user.UserService;

@RestController
public class UserRestController {
	@Autowired
	UserService  userservice;
	@PostMapping("/users/check_email")
	public String checkUniqueEmail(@Param("email") String email,@Param("id") Integer id) {
		return userservice.isEmailUnique(email,id)?"OK": "Duplicate";
	}
}
