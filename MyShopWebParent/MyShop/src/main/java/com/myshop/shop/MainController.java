package com.myshop.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

import com.myshop.common.entity.Category;
import com.myshop.shop.category.CategoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
@Controller
public class MainController {
	
	@Autowired 
	CategoryService categoryService;
	
	@GetMapping("")
	public String viewHomePage(Model model) {
		List< Category> listCategories=categoryService.listNoChildCategories();
		
		model.addAttribute("listAllCategoties", listCategories);
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null||authentication instanceof AnonymousAuthenticationToken) {
			
			return "login";
		}
			return "redirect:/";
	
		
	}
}