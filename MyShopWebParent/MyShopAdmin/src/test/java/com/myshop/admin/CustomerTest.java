package com.myshop.admin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.myshop.admin.customers.CustomerRepository;
import com.myshop.common.entity.AuthenticationType;
import com.myshop.common.entity.Customer;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerTest {
	@Autowired
	CustomerRepository customerRepository;
	
	@Test
	public void testUpdateAuthenticationType() {
		customerRepository.updateAuthenticationTypeById(1,AuthenticationType.GOOGLE );
		Customer customer = customerRepository.findById(1).orElse(null);
		assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.GOOGLE);
	}
	
	@Test
	public void test() {
		Sort sort=Sort.by("first_name");
		Pageable pageable=PageRequest.of(0, 6,sort);
		Page<Customer> a= customerRepository.findAll1(pageable);
		
		System.out.println("---------------------"+a.getTotalElements());
			}
}
