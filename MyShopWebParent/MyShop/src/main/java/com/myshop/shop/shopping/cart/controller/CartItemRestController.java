package com.myshop.shop.shopping.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.common.entity.Customer;
import com.myshop.shop.Utility;
import com.myshop.shop.customer.CustomerNotFoundException;
import com.myshop.shop.customer.CustomerService;
import com.myshop.shop.shopping.cart.CartItemService;

@RestController
public class CartItemRestController {

	@Autowired
	CartItemService	cartItemService;
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProduct(@PathVariable("productId") Integer productId, @PathVariable("quantity") Integer quantity,
			HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartItemService.addProduct(customer, productId, quantity);
			
			return "Đã thêm "+quantity+" sản phẩm này vào giỏ hàng, tổng số lượng sản phẩm này trong giỏ hàng là: "+updatedQuantity;
		} catch (CustomerNotFoundException e) {
			return "Bạn cần đăng nhập để có thể thêm sản phẩm vào giỏ hàng";
		}
		
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public List<Double> updateProduct(@PathVariable("productId") Integer productId, @PathVariable("quantity") Integer quantity,
			HttpServletRequest request) throws CustomerNotFoundException {
		
		
			Customer customer = getAuthenticatedCustomer(request);
		
			List<Double> priceAndDiscountValue = cartItemService.updateProduct(customer, productId, quantity);
			return priceAndDiscountValue;
		
		
	}
	
	private Customer getAuthenticatedCustomer( HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailCustomerFromAuthenticated(request);
		if(email==null) {
			throw new CustomerNotFoundException(email);
		}
		return customerService.getCustomerByEmail(email);
	}
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId,HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartItemService.removeProduct(customer, productId);
			
			return "Sản phẩm này đã được xóa khỏi giỏ hàng";
		} catch (CustomerNotFoundException e) {
			return "Bạn cần đăng nhập thực hiện";
		}
	}
}
