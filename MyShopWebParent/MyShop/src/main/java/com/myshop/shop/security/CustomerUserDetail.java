package com.myshop.shop.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.myshop.common.entity.Customer;

public class CustomerUserDetail implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Customer customer;
	
	
	public CustomerUserDetail(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return customer.isEnabled();
	}
	public String getFullname() {
		return this.customer.getLastName()+" "+this.customer.getFirstName();
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
	public void setFirstName(String firstName) {
		this.customer.setFirstName(firstName);
	}

	public void setLastName(String lastName) {
		this.customer.setLastName(lastName);
	}	
}
