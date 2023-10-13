package com.myshop.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;



import com.myshop.admin.product.ProductRepository;
import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Category;
import com.myshop.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductTest2 {
	@Autowired
	ProductRepository productRepository;
	
	@Autowired 
	private TestEntityManager entityManager;
	

	@Test
	@Transactional
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 1);
		Category category = entityManager.find(Category.class, 6);
		System.out.println(brand);
		System.out.println(category);
		Product product = new Product();
		product.setName("Bravo 22");
		product.setAlias("bravo22");
		product.setShortDescription("Short description for Acer Aspire");
		product.setFullDescription("Full description for Acer Aspire");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(678);
		product.setCost(600);
		product.setEnabled(true);
		
		product.setQuantity(1);
		
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = productRepository.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	@Transactional
	public void testSaveProductWithImage() {
		Integer id=2;
		Product product=productRepository.findById(id).orElse(null);
		
		product.setMainImage("abv.png");
		product.addExtraImage("1.png");
		product.addExtraImage("2.png");
		
		Product product2= productRepository.save(product);
		assertThat(product2.getImages().size()).isEqualTo(2);
	}
	
	@Test
	@Transactional
	public void testSaveProductWithDetail() {
		Integer id=2;
		Product product=productRepository.findById(id).orElse(null);
		
		product.addDetails("ram", "8gb");
		product.addDetails("chip", "AMD");
		;
		
		Product product2= productRepository.save(product);
		assertThat(product2.getDetails().size()).isEqualTo(2);
	}
}
