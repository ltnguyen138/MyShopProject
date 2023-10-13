package com.myshop.shop.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.common.entity.Category;

@Service
public interface CategoryService {
	public static final int CATEGORY_PER_PAGE = 6;
	public List<Category> listNoChildCategories();
	public Category getCategoryByAlias(String alias) throws CategoryNotFoundException;
	public List<Category> listParentCategories(Category child);
}
