package com.myshop.admin.customers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myshop.admin.customers.CustomerNotFoundException;
import com.myshop.admin.customers.CustomerService;
import com.myshop.admin.product.ProductNotFoundException;
import com.myshop.admin.user.UserNotFoundException;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.Province;
import com.myshop.common.entity.Role;
import com.myshop.common.entity.User;
import com.myshop.common.entity.product.Product;


@Controller
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	@GetMapping("/customers")
	public String listFirst(Model model) {
		
		return listByPage(1, model,"firstName","asc","");
	}
	
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,Model model,@Param("sortField")String sortField,
			@Param("sortDir")String sortDir,@Param("keyword")String keyword ) {
		Page<Customer> pageCustomer=customerService.listByPage(pageNum,sortField,sortDir,keyword);
		List<Customer> listAllCustomers=pageCustomer.getContent();
		System.out.println(pageCustomer.getTotalElements());
		long startCount=(pageNum-1)*customerService.Customer_PER_PAGE+1;
		long endCount=startCount+customerService.Customer_PER_PAGE-1;
		if(endCount>pageCustomer.getTotalElements())endCount=pageCustomer.getTotalElements();
		String reverseSortDir=sortDir.equals("asc")?"desc" : "asc";
		model.addAttribute("totalPages", pageCustomer.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageCustomer.getTotalElements());
		model.addAttribute("listAllCustomers", listAllCustomers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "customers/customers";
	}
	
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model)  {
		try {			
			customerService.deleteById(id);
			redirectattributes.addFlashAttribute("message", "Xóa khách hàng thành công.");
											
			return "redirect:/customers";
		} catch (CustomerNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/customers";
		}
	}
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateEnbledCustomer(@PathVariable("id") int id,@PathVariable("status") Boolean status,
			RedirectAttributes redirectattributes) {
		customerService.updateEnble(id, status);
		redirectattributes.addFlashAttribute("message", "Cập nhật trạng thái khách hàng thành công.");
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editUser(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model) {
		try {
			List<Province> listProvincies = customerService.listAllProvice();
			Customer customer= customerService.getCustomerById(id);
			
			model.addAttribute("customer", customer);
			model.addAttribute("listProvincies", listProvincies);
			model.addAttribute("pageTitle", "Chỉnh sửa thông tin người dùng: "+customer.getEmail());
			return "/customers/customer_form";
		} catch (CustomerNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/customers";
		}
		
	
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, RedirectAttributes redirectattributes) throws CustomerNotFoundException {
		customerService.saveCustomer(customer);
		redirectattributes.addFlashAttribute("message", "Lưu thông tin khách hàng thành công.");
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/detail/{id}")
	public String detailProduct(@PathVariable("id") int id, Model model, RedirectAttributes redirectattributes) {
		
		try {
			Customer customer= customerService.getCustomerById(id);
			
			
			model.addAttribute("customer",customer);
			
			model.addAttribute("pageTitle", "Chi tiết thông tin khách hàng: "+customer.getEmail());
			return "/customers/customers_detail_modal";
			
		} catch (CustomerNotFoundException e) {
			redirectattributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/customers";
		}
		
		
	}
}
