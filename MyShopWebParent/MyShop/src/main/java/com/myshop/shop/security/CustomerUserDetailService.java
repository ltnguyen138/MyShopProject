package com.myshop.shop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.myshop.common.entity.Customer;
import com.myshop.shop.customer.CustomerRepository;

public class CustomerUserDetailService implements UserDetailsService {

	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer =customerRepository.findByEmail(username).orElse(null);
		if(customer==null) {
			throw new UsernameNotFoundException("Không tìm thấy tài khoản khách hàng");
		}
		return new CustomerUserDetail(customer);
	}

}
