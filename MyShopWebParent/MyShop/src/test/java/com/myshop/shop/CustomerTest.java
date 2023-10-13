package com.myshop.shop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.myshop.common.entity.Customer;
import com.myshop.common.entity.Province;
import com.myshop.shop.customer.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerTest {
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	EntityManager entityManager;
	
	@Test
	public void testCreat() {
		Integer pId= 1;
		Province province=entityManager.find(Province.class, pId);
		Customer customer=new Customer();
		customer.setProvince(province);
		customer.setAddressLine("tan truong");
		customer.setCreatedTime(new Date());
		customer.setDistrict("cao lanh");
		customer.setEnabled(true);
		customer.setFirstName("Nguyen");
		customer.setLastName("le");
		customer.setPassword("sacxxxxxxx");
		customer.setPhoneNumber("0901048433");
		customer.setEmail("nguyen@gmail.com");
		customer.setVerificationCode("ssscccc");
		Customer c = customerRepository.save(customer);
		assertThat(c).isNotNull();
	}
}
