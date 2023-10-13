package com.myshop.admin.user.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myshop.admin.FileUploadUtil;
import com.myshop.admin.security.MyShopUserDetail;
import com.myshop.admin.user.UserNotFoundException;
import com.myshop.admin.user.UserService;
import com.myshop.common.entity.User;

@Controller
public class AccountController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/account")
	public String viewAccount(@AuthenticationPrincipal MyShopUserDetail loggedUser, Model model) {
		String email=loggedUser.getUsername();
		User user = userService.getUserByEmail(email);
		model.addAttribute("user", user);
		return "users/account_form";
		
	}
	@PostMapping("/account/edit")
	public String editAccount(User user,@RequestParam("image") MultipartFile multipartfile  ,RedirectAttributes redirectattributes, 
		@AuthenticationPrincipal MyShopUserDetail loggedUser)throws IOException, UserNotFoundException {
		if(!multipartfile.isEmpty()) {
			String filename=StringUtils.cleanPath(multipartfile.getOriginalFilename());
			user.setPhotos(filename);
			User savedUser = userService.updateAccount(user);
			String uploadDir="user-photos/"+savedUser.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, filename, multipartfile);
		}else {
			user.setPhotos(null);
			User savedUser = userService.updateAccount(user);
		}
		Collection<? extends GrantedAuthority> a=loggedUser.getAuthorities();
		
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		redirectattributes.addFlashAttribute("message", "Cập nhật thông tin thành công.");
		
		return "redirect:/account";
	}
	
}
