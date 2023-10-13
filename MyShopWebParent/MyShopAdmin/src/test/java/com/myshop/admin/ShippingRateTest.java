package com.myshop.admin;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.myshop.admin.shippingrate.ShippingRateRepository;
import com.myshop.common.entity.Province;
import com.myshop.common.entity.ShippingRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateTest {
	
	@Autowired
	ShippingRateRepository shippingRateRepository;
	@Autowired
	EntityManager entityManager;
	
	@Test
	public void testCreat() {
		Integer pId= 1;
		Province province=entityManager.find(Province.class, pId);
		
		ShippingRate shippingRate = new ShippingRate();
		
		shippingRate.setProvince(province);
		shippingRate.setDistrict("cao lanh");
		shippingRate.setCost(50000D);
		shippingRate.setDays(2);
		shippingRateRepository.save(shippingRate);
	}
}
