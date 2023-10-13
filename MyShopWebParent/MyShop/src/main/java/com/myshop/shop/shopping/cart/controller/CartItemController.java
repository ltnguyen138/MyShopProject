package com.myshop.shop.shopping.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.myshop.common.entity.CartItem;
import com.myshop.common.entity.Customer;
import com.myshop.shop.Utility;
import com.myshop.shop.customer.CustomerNotFoundException;
import com.myshop.shop.customer.CustomerService;
import com.myshop.shop.shopping.cart.CartItemService;

@Controller
public class CartItemController {

	@Autowired
	CartItemService cartItemService;
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> listCartItems = cartItemService.getCartByCustomer(customer);
		
		Double totalPrice = 0.0D;
		Double totalDiscount = 0.0D;
		
		for(CartItem item : listCartItems) {
			totalPrice+=(item.getProduct().getDiscountPrice()*item.getQuantiny());
			totalDiscount+=(item.getProduct().getDiscountValue()*item.getQuantiny());
		}
		model.addAttribute("customer", customer);
		model.addAttribute("listCartItems", listCartItems);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalDiscount", totalDiscount);
		
		return "cart/cart";
	}
	
	private Customer getAuthenticatedCustomer( HttpServletRequest request)  {
		String email = Utility.getEmailCustomerFromAuthenticated(request);
		
		return customerService.getCustomerByEmail2(email);
	}
}
