package com.myshop.admin.customers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.myshop.admin.brand.BrandNotFoundException;
import com.myshop.admin.setting.province.ProvinceRepository;
import com.myshop.common.entity.AuthenticationType;
import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.Province;
import com.myshop.common.entity.User;


@Component
public class CustomerServiceImpl implements CustomerService {

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
	public Customer getCustomerById(Integer id) throws  CustomerNotFoundException {
		return customerRepository.findById(id).orElseThrow(()-> new CustomerNotFoundException("Không tìm thấy khách hàng có id "+id));
	}

	@Override
	public Page<Customer> listByPage(int pageNum, String sortField,String sortDir,String keyword) {
		Sort sort=Sort.by(sortField);
		if(sortDir==null) {
			sort=sort.ascending();
		
		}else if(sortDir.equals("asc")) {
			sort=sort.ascending();
		}else if(sortDir.equals("desc")){
			sort=sort.descending();
		}
		List<Customer> a = (List<Customer>) customerRepository.findAll();
		System.out.print(a.size());
		
		Pageable pageable=PageRequest.of(pageNum-1, Customer_PER_PAGE,sort);
		
		
		if (keyword==null) {
			
			return  customerRepository.findAll1(pageable);
		}
		return customerRepository.findKey(keyword, pageable);
	}

	@Override
	public Customer saveCustomer(Customer customer) throws CustomerNotFoundException {
		boolean isUpdateCustomer=(customer.getId()!=null);
		if(isUpdateCustomer) {
			Customer existingCustomer = getCustomerById(customer.getId());
			customer.setCreatedTime(existingCustomer.getCreatedTime());
			customer.setEnabled(true);
			customer.setAuthenticationType(AuthenticationType.DATABASE);
			if(customer.getPassword()==null) {
				customer.setPassword(existingCustomer.getPassword());
			}else {
				endcodePassword(customer);
			}
			
		}
		return customerRepository.save(customer);
	}
	
	private void endcodePassword(Customer customer) {
		String endcodedPassword=passwordEncoder.encode(customer.getPassword());
		customer.setPassword(endcodedPassword);
	}
	
	@Override
	public String checkUnique(Integer id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(int id) throws CustomerNotFoundException {
		Customer customer = getCustomerById(id);
		customerRepository.delete(customer);
		
	}

	@Override
	public List<Customer> listAll() {
		return null;
	}

	@Override
	public void updateEnble(Integer id, boolean enabled) {
		customerRepository.updateEnable(id, enabled);
		
	}

	@Override
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if(!customer.getAuthenticationType().equals(type)) {
			customerRepository.updateAuthenticationTypeById(customer.getId(), type);
		}
		
	}

}
