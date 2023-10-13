package com.myshop.admin.customers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.myshop.admin.brand.BrandNotFoundException;
import com.myshop.common.entity.AuthenticationType;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.Province;


@Service
public interface CustomerService {
	public static final int Customer_PER_PAGE=6;
	public Customer getCustomerById(Integer id) throws CustomerNotFoundException;
	
	public Page<Customer> listByPage(int pageNum, String sortField,String sortDir,String keyword);
	public Customer saveCustomer(Customer brand) throws CustomerNotFoundException;
	public String checkUnique(Integer id, String name);
	public void deleteById(int id) throws CustomerNotFoundException;
	public List<Customer> listAll();
	public void updateEnble(Integer id, boolean enabled);
	public List<Province> listAllProvice();
	public void updateAuthenticationType(Customer customer, AuthenticationType type);
}
