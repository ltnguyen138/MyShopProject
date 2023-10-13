package com.myshop.admin;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.myshop.admin.setting.province.ProvinceRepository;
import com.myshop.common.entity.Province;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProvinceTest {
	@Autowired ProvinceRepository provinceRepository;
	
	@Test
	public void testCreateProvince() {
		Province province = provinceRepository.save(new Province("Bình Dương", "60"));
		
	}
	@Test
	public void testListCountries() {
		List<Province> listCountries = provinceRepository.findAllByOrderByNameAsc();
		listCountries.forEach(System.out::println);
		
		
	}
}
