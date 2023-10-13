package com.myshop.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.myshop.admin.category.CategoryRepository;
import com.myshop.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryTest {
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void testCreateCategory() {
		Category c =new Category(2);
		Category a = new Category("iPhone",c);
		Category b=categoryRepository.save(a);
		
		assertThat(b.getId()).isGreaterThan(0);
	}
	@Test
	public void testCategory() {
		Iterable<Category>categories=categoryRepository.findAll();
		for(Category category:categories) {
			if(category.getParent()==null) {
				System.out.println(category.getName());
				Set<Category> chill=category.getChildren();
				for(Category chillC:chill) {
					System.out.println("--"+chillC.getName());
				}
			}
		}
	}
	
	
}
