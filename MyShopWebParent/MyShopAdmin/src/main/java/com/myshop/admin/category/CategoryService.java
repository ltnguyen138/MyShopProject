package com.myshop.admin.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.myshop.common.entity.Category;
@Service
public interface CategoryService {
	public static final int CATEGORY_PER_PAGE = 100;
	public List<Category> listAll(CategoryPageInfo categoryPageInfo,int pageNum,String sortDir,String keyword);
	public Page<Category> listByPage(int pageNum, String sortField,String sortDir,String keyword);
	public List<Category> listCategoryInForm();	
	public Category saveCategory(Category category) throws CategoryNotFoundException;
	public Category getCategoryById(Integer id) throws CategoryNotFoundException;
	public String checkUnique(Integer id, String name, String alias);
	public void updateEnble(Integer id, boolean enabled);
	public void deleteById(Integer id) throws CategoryNotFoundException;
	
}
