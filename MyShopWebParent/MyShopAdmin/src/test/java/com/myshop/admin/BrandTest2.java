package com.myshop.admin;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.myshop.admin.brand.BrandRepository;
import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandTest2 {

	@Autowired
	BrandRepository brandRepository;
	
	@Test
	@Transactional
	public void testCreate() {
		Category laptop= new Category(6);
		Category d= new Category(4);
		Brand acer=new Brand("Acer");
		acer.getCategories().add(laptop);
		acer.getCategories().add(d);
		Brand save = brandRepository.save(acer);
		assertThat(save.getId()).isGreaterThan(0);
		
	}
}
