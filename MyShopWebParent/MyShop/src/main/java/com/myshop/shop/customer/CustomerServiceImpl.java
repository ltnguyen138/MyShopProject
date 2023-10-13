package com.myshop.shop.customer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.AuthenticationType;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.Province;
import com.myshop.shop.province.ProvinceRepository;

import net.bytebuddy.utility.RandomString;

@Component
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	ProvinceRepository provinceRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public List<Province> listAllProvice() {
		
		return provinceRepository.findAllByOrderByNameAsc();
	}
	@Override
	public boolean isEmailUnique(String email, Integer id) throws CustomerNotFoundException {
		
		Customer customer = customerRepository.findByEmail(email).orElse(null);
		if(customer==null) {
			return true;
		}
		boolean isCreateNew= (id==null);
		if(isCreateNew) {
			return false;
		}
		else {
			if(customer.getId()!=id) {
				return false;
			}
		}
		return true;
		
	}
	@Override
	public Customer getCustomerByEmail(String email) throws CustomerNotFoundException {
		return customerRepository.findByEmail(email).orElseThrow(()->new CustomerNotFoundException("Khách hàng "+email+" không tồn tại"));
	}
	@Override
	public void registerCustomer(Customer customer) {
		endcodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		String randomCode= RandomString.make(64);
		customer.setVerificationCode(randomCode);
		System.out.println(randomCode);
		customerRepository.save(customer);
	}
	
	private void endcodePassword(Customer customer) {
		String endCodePassword =  passwordEncoder.encode(customer.getPassword());
		customer.setPassword(endCodePassword);
	}
	@Override
	public boolean verifyCustomerAccount(String verifycationCode) {
		Customer customer = customerRepository.findByVerificationCode(verifycationCode).orElse(null);
		if(customer==null||customer.isEnabled()) {
			return false;
		}else {
			customerRepository.updateEnableByVerifyTrue(customer.getId());
			return true;
		}
		
	}
	@Override
	public Customer getCustomerByEmail2(String email) {
		return customerRepository.findByEmail(email).orElse(null);
	}
	
	@Override
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if(!customer.getAuthenticationType().equals(type)) {
			customerRepository.updateAuthenticationTypeById(customer.getId(), type);
		}
		
	}
	@Override
	public void addNewCustomerFromOAuth(String name, String email) {
		Customer customer = new Customer();
		
		customer.setEmail(email);
		setName(name, customer);
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setPassword("");
		customer.setAddressLine("");
		customer.setDistrict("");
		customer.setPhoneNumber("");
		customer.setProvince(null);
		customer.setVerificationCode("");
		customer.setAuthenticationType(AuthenticationType.GOOGLE);
		customer.setResetPasswordToken(null);
		customerRepository.save(customer);
		
	}
	private void setName(String name, Customer customer) {
		String[] nameArray = name.split(" ");
		if(nameArray.length<2) {
			customer.setFirstName(name);
			customer.setLastName("");
		}else {
			String firstName= nameArray[0];
			customer.setFirstName(firstName);
			customer.setLastName(name.replaceFirst(firstName, ""));
		}
		
	}
	@Override
	public Customer updateAccount(Customer customer) {
		Customer customerInDB = getCustomerByIdOrNull(customer.getId());
		if(customer.getPassword()==null) {
			customer.setPassword(customerInDB.getPassword());
		
		}else if(!customer.getPassword().isEmpty()) {
			endcodePassword(customer);
		}else {
			customer.setPassword(customerInDB.getPassword());
		}
		
		customer.setEnabled(customerInDB.isEnabled());
		customer.setCreatedTime(customerInDB.getCreatedTime());
		customer.setVerificationCode(customerInDB.getVerificationCode());
		customer.setResetPasswordToken(customerInDB.getResetPasswordToken());
		return customerRepository.save(customer);
	}
	@Override
	public Customer getCustomerByIdOrNull(Integer id) {

		return customerRepository.findById(id).orElse(null);
	}
	@Override
	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = getCustomerByEmail(email);
		if(customer!=null) {
			String token = RandomString.make(30);
			customer.setResetPasswordToken(token);
			customerRepository.save(customer);
			return token;
		}
		return null;
	}
	@Override
	public Customer getCustomerByResetPasswordToken(String token) throws CustomerNotFoundException {

		return customerRepository.findByResetPasswordToken(token).orElseThrow(()-> new CustomerNotFoundException("Cấp lại mật khẩu không thanh công"));
	}
	@Override
	public void resetPassword(String token, String password) throws CustomerNotFoundException {
		 
		Customer customer = getCustomerByResetPasswordToken(token);
		customer.setPassword(password);
		endcodePassword(customer);
		customerRepository.save(customer);
		
	}
}
