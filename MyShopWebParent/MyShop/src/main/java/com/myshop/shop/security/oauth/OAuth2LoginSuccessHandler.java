package com.myshop.shop.security.oauth;

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
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	@Autowired
	CustomerService customerService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		customerOauth2User oauth2User = (customerOauth2User) authentication.getPrincipal();
		
		String name= oauth2User.getFullname();
		String email=oauth2User.getEmail();
		
		Customer customer = customerService.getCustomerByEmail2(email);
		if(customer==null) {
			customerService.addNewCustomerFromOAuth(name, email);
		}else {
			oauth2User.setFullName(customer.getFullName());
			customerService.updateAuthenticationType(customer, AuthenticationType.GOOGLE);
			
		
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
