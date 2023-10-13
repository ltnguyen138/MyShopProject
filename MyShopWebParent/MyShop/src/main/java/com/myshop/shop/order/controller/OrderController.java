package com.myshop.shop.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.myshop.common.entity.Customer;
import com.myshop.common.entity.order.Order;
import com.myshop.shop.Utility;
import com.myshop.shop.customer.CustomerService;
import com.myshop.shop.order.OrderNotFoundException;
import com.myshop.shop.order.OrderService;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;
	@Autowired 
	CustomerService customerService;
	
	@GetMapping("/orders")
	public String listFirst(Model model, HttpServletRequest request) {
		
		return listByPage(1, model,"orderTime","asc","",request);
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,Model model,@Param("sortField")String sortField,
			@Param("sortDir")String sortDir,@Param("keyword")String keyword ,HttpServletRequest request) {
		
		Customer customer = getAuthenticatedCustomer(request);
		Page<Order> pageOrder = orderService.listByPage(customer ,pageNum, sortField, sortDir, keyword);
		List<Order> listAllOrders = pageOrder.getContent();
		
		long startCount=(pageNum-1)*orderService.ORDERS_PER_PAGE+1;
		long endCount=startCount+orderService.ORDERS_PER_PAGE-1;
		if(endCount>pageOrder.getTotalElements())endCount=pageOrder.getTotalElements();
		String reverseSortDir=sortDir.equals("asc")?"desc" : "asc";
		model.addAttribute("totalPages", pageOrder.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageOrder.getTotalElements());
		model.addAttribute("listAllOrders", listAllOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "orders/orders";
	}
	
	@GetMapping("/orders/detail/{id}")
	public String detailOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request ) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Order order = orderService.getOrderById(id,customer);
			model.addAttribute("order", order);
			
			return "orders/order_detail_modal";
		} catch (OrderNotFoundException e) {			
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/orders";
		}
	}
	
	private Customer getAuthenticatedCustomer( HttpServletRequest request)  {
		String email = Utility.getEmailCustomerFromAuthenticated(request);
		
		return customerService.getCustomerByEmail2(email);
	}
}
