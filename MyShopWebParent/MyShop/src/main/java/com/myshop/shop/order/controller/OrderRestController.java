package com.myshop.shop.order.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.common.entity.Customer;
import com.myshop.shop.Utility;
import com.myshop.shop.customer.CustomerNotFoundException;
import com.myshop.shop.customer.CustomerService;
import com.myshop.shop.order.OrderNotFoundException;
import com.myshop.shop.order.OrderReturnRequest;
import com.myshop.shop.order.OrderReturnResponse;
import com.myshop.shop.order.OrderService;

@RestController
public class OrderRestController {
	@Autowired
	OrderService orderService;
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/orders/return")
	public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,
			HttpServletRequest servletRequest) {
		
		System.out.println("Order ID: " + returnRequest.getOrderId());
		System.out.println("Reason: " + returnRequest.getReason());
		System.out.println("Note: " + returnRequest.getNote());
		
		Customer customer = null;
		
		try {
			customer = getAuthenticatedCustomer(servletRequest);
		} catch (CustomerNotFoundException ex) {
			return new ResponseEntity<>("Bạn chưa đăng nhập", HttpStatus.BAD_REQUEST);
		}
		
		try {
			orderService.setOrderReturnRequested(returnRequest, customer);
		} catch (OrderNotFoundException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()), HttpStatus.OK);
	}
	
	private Customer getAuthenticatedCustomer( HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailCustomerFromAuthenticated(request);
		if(email==null) {
			throw new CustomerNotFoundException("Không tìm thấy khách hàng: "+email);
		}
		return customerService.getCustomerByEmail(email);
	}
}
