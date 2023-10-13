package com.myshop.shop.customer;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.common.entity.AuthenticationType;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.Province;

@Service
public interface CustomerService {
	
	public List<Province> listAllProvice();
	public Customer getCustomerByEmail(String email) throws CustomerNotFoundException;
	public boolean isEmailUnique(String email, Integer id) throws CustomerNotFoundException;
	public void registerCustomer(Customer customer) ;
	public boolean verifyCustomerAccount(String verifycationCode);
	public Customer getCustomerByEmail2(String email);
	public void updateAuthenticationType(Customer customer, AuthenticationType type);
	public void addNewCustomerFromOAuth(String name, String email);
	public Customer updateAccount(Customer customer);
	public Customer getCustomerByIdOrNull(Integer id) ;
	public Customer getCustomerByResetPasswordToken(String token) throws CustomerNotFoundException;
	public String updateResetPasswordToken(String email) throws CustomerNotFoundException;
	public void resetPassword(String token, String Password) throws CustomerNotFoundException;
}
