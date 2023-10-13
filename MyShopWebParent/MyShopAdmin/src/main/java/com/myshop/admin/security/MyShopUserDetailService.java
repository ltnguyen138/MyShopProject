package com.myshop.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.myshop.admin.user.UserRepository;
import com.myshop.common.entity.User;

public class MyShopUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email).orElse(null);
		if(user!=null) {
			return new MyShopUserDetail(user);
		}
			throw new UsernameNotFoundException("Không tìm thấy người dùng");
		
		
	}
	

}
