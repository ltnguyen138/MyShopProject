package com.myshop.admin.brand.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.admin.brand.BrandNotFoundException;
import com.myshop.admin.brand.BrandService;
import com.myshop.admin.brand.CategoryDTO;
import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Category;

@RestController
public class BrandRestController {

	@Autowired
	BrandService brandService;
	
	@PostMapping("/brands/check_unique")
	public String checkUnique(@Param("id")Integer id,@Param("name")String name ) {
		
		return brandService.checkUnique(id, name);
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable("id") Integer brandId ) throws BrandNotFoundException{
		List<CategoryDTO> listCategoryDTOs = new ArrayList<>();
		try {
			Brand brand =brandService.getBrandById(brandId);
			Set<Category> categories=brand.getCategories();
			for(Category category:categories) {
				CategoryDTO categoryDTO= new CategoryDTO(category.getId(),category.getName());
				listCategoryDTOs.add(categoryDTO);
			}
			return listCategoryDTOs;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundException("Không tin thấy thương hiệu");
			
		}
	}
}
