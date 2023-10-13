package com.myshop.shop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.myshop.common.entity.Category;
import com.myshop.shop.category.CategoryRepository;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryTest {
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void testQueryCategoryEnable() {
		List<Category> categories = categoryRepository.findAllEnabled();
		categories.forEach(cat -> {
			System.out.println(cat.getName()+"--"+cat.isEnabled());
		});
	}
	@Test
	public void testQueryCategoryByAlias() {
		Category categories = categoryRepository.findByAliasEnabled("m").orElse(null);
		assertThat(categories).isNotNull();
	}
}
