package com.myshop.shop.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.myshop.common.entity.Category;
@Component
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired CategoryRepository categoryRepository;
	
	@Override
	public List<Category> listNoChildCategories() {
		List<Category>listNoChildCategories=new ArrayList<>();
		
		List<Category>categories=categoryRepository.findAllEnabled();
		categories.forEach(cat -> {
			Set<Category> children=cat.getChildren();
			if(children==null||children.size()==0) listNoChildCategories.add(cat);
		});
		return listNoChildCategories;
	}

	@Override
	public Category getCategoryByAlias(String alias) throws CategoryNotFoundException {

		return categoryRepository.findByAliasEnabled(alias).orElseThrow(()->new CategoryNotFoundException("Không tìm thấy danh mục "+alias));
	}

	@Override
	public List<Category> listParentCategories(Category child) {
		List<Category> listParent=new ArrayList<>();
		
		Category parent=child.getParent();
		while (parent!=null) {
			listParent.add(0,parent);
			parent=parent.getParent();
		}
		listParent.add(child);
		return listParent;
	}

}
