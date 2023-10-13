package com.myshop.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myshop.admin.FileUploadUtil;
import com.myshop.admin.user.UserNotFoundException;
import com.myshop.admin.user.UserService;
import com.myshop.admin.user.export.UserCsvExporter;
import com.myshop.admin.user.export.UserExcelExporter;
import com.myshop.admin.user.export.UserPdfExporter;
import com.myshop.common.entity.Role;
import com.myshop.common.entity.User;






@Controller
public class UserController {
	
	@Autowired
	UserService userservice;
	
	@GetMapping("/users")
	public String listFirst(Model model) {
		
		return listByPage(1, model,"firstName","asc","");
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,Model model,@Param("sortField")String sortField,
			@Param("sortDir")String sortDir,@Param("keyword")String keyword ) {
		Page<User> pageUser=userservice.listByPage(pageNum,sortField,sortDir,keyword);
		List<User> listAllUsers=pageUser.getContent();
		long startCount=(pageNum-1)*userservice.USERS_PER_PAGE+1;
		long endCount=startCount+userservice.USERS_PER_PAGE-1;
		if(endCount>pageUser.getTotalElements())endCount=pageUser.getTotalElements();
		String reverseSortDir=sortDir.equals("asc")?"desc" : "asc";
		model.addAttribute("totalPages", pageUser.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageUser.getTotalElements());
		model.addAttribute("listAllUsers", listAllUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "users/users";
	}
	
	@GetMapping("/users/export/csv")
	public void  exportCSV(HttpServletResponse respone) throws IOException {
		List<User> listUser=userservice.listAll();
		UserCsvExporter userCsvExporter= new UserCsvExporter();
		userCsvExporter.export(listUser, respone);
	}
	
	@GetMapping("/users/export/excel")
	public void  exportExcel(HttpServletResponse respone) throws IOException {
		List<User> listUser=userservice.listAll();
		UserExcelExporter userExcelExporter=new UserExcelExporter();
		userExcelExporter.export(listUser, respone);
	}
	
	@GetMapping("/users/export/pdf")
	public void  exportPdf(HttpServletResponse respone) throws IOException {
		List<User> listUser=userservice.listAll();
		UserPdfExporter userPdfExporter=new UserPdfExporter();
		userPdfExporter.export(listUser, respone);
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles=userservice.listRoles();
		User user=new User();
		user.setEnabled(true);
		user.setId(null);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Thêm mới người dùng");
		return "users/user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user,@RequestParam("image") MultipartFile multipartfile  ,RedirectAttributes redirectattributes) throws IOException {
		System.out.println(user);
		if(!multipartfile.isEmpty()) {
			String filename=StringUtils.cleanPath(multipartfile.getOriginalFilename());
			user.setPhotos(filename);
			User savedUser = userservice.saveUser(user);
			String uploadDir="user-photos/"+savedUser.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, filename, multipartfile);
		}else {
			userservice.saveUser(user);
		}
		
		
		redirectattributes.addFlashAttribute("message", "Lưu người dùng thành công.");
		String firstPartofEmail=user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc&keyword="+firstPartofEmail;
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model) {
		try {
			List<Role> listRoles=userservice.listRoles();
			User user=userservice.getUserById(id);
			
			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Chỉnh sửa thông tin người dùng: "+user.getEmail());
			return "/users/user_form";
		} catch (UserNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/users";
		}
		
	
	}
	
	@GetMapping("/loginn")
	public String formlogin() {
		return "loginn";
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model) {
		try {			
			userservice.deleteUser(id);	
			redirectattributes.addFlashAttribute("message", "Xóa người dùng thành công.");
											
			return "redirect:/users";
		} catch (UserNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/users";
		}
	}
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateEnbledUser(@PathVariable("id") int id,@PathVariable("status") Boolean status,
			RedirectAttributes redirectattributes) {
		userservice.updateEnble(id, status);
		redirectattributes.addFlashAttribute("message", "Cập nhật trạng thái tài khoản thành công.");
		return "redirect:/users";
	}

}
