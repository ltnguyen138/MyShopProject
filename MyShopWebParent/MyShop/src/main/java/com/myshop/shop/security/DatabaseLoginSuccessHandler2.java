package com.myshop.shop.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.AuthenticationType;
import com.myshop.common.entity.Customer;
import com.myshop.shop.customer.CustomerService;

@Component
public class DatabaseLoginSuccessHandler2  extends SavedRequestAwareAuthenticationSuccessHandler{

	@Autowired
	CustomerService customerService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		CustomerUserDetail customerUserDetail = (CustomerUserDetail) authentication.getPrincipal();
		Customer customer = customerUserDetail.getCustomer();
		customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);
		super.onAuthenticationSuccess(request, response, authentication);
	}

	
}
