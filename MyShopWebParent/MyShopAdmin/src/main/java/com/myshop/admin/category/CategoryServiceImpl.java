package com.myshop.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.myshop.common.entity.Category;

@Component
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> listAll(CategoryPageInfo categoryPageInfo,  int pageNum, String sortDir,String keyword) {
		Sort sort= Sort.by("name");
		if(sortDir==null) {
			sort=sort.ascending();
		
		}else if(sortDir.equals("asc")) {
			sort=sort.ascending();
		}else if(sortDir.equals("desc")){
			sort=sort.descending();
		}
		Pageable pageable=PageRequest.of(pageNum-1, CATEGORY_PER_PAGE,sort);
		
		
		Page<Category>rootPageCategories=categoryRepository.findRootCategories(pageable);
		if(keyword!=null) {
			rootPageCategories=categoryRepository.findKey(keyword, pageable);
		}
		List<Category> root= new ArrayList<>();
		List<Category>rootCategories=rootPageCategories.getContent();
		for(Category r :rootCategories) {
			if (r.getParent()==null) root.add(r);
		}
		System.out.println(root);
		System.out.println(rootCategories);
		System.out.println(rootPageCategories);
		categoryPageInfo.setTotalElements(rootPageCategories.getTotalElements());
		categoryPageInfo.setTotalPages(rootPageCategories.getTotalPages());
		return listHierarchicalCategories(root,sortDir);
	}
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories,String sortDir){
		List<Category> listhierarchicalCategories=new ArrayList<>()	;
		
		for(Category rootcategory:rootCategories) {
			listhierarchicalCategories.add(Category.copyFull(rootcategory));
			
			Set<Category>children= sortSubCategories(rootcategory.getChildren(),sortDir) ;
			
			for(Category subCategory:children) {
				String name="--"+subCategory.getName();
				listhierarchicalCategories.add(Category.copyFull(subCategory, name));
				listSubHierarchicalCategories(listhierarchicalCategories,subCategory,1,sortDir);
			}
		}
		System.out.println(listhierarchicalCategories);
		return listhierarchicalCategories;
	}
	
	private void listSubHierarchicalCategories(List<Category>listhierarchicalCategories,Category parent, int subLevel,String sortDir) {
		int newSubLevel=subLevel+1;
		Set<Category>children= sortSubCategories(parent.getChildren(),sortDir) ;
		for(Category categoryChild: children) {
			String name="";
			for(int i=0;i<newSubLevel;i++ ) {
				name+="--";
			}	
			name+=categoryChild.getName();
			listhierarchicalCategories.add(Category.copyFull(categoryChild, name));
			listSubHierarchicalCategories(listhierarchicalCategories, categoryChild, newSubLevel,sortDir);	
			
			
		}
	}

	@Override
	public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort=Sort.by(sortField);
		sort=sortDir.equals("asc")?sort.ascending():sort.descending();
		Pageable pageable=PageRequest.of(pageNum-1, CATEGORY_PER_PAGE,sort);
		if (keyword.equals("null")||keyword==null) return categoryRepository.findAll(pageable);
		return categoryRepository.findKey(keyword, pageable);
	}

	@Override
	public List<Category> listCategoryInForm() {
		List<Category>categoryUseInForm=new ArrayList<>();
		Iterable<Category>categoryInDB=categoryRepository.findRootCategories(Sort.by("name").ascending())	;
		for(Category category:categoryInDB) {
			if(category.getParent()==null) {
				categoryUseInForm.add(Category.copyFull(category));
				Set<Category> children=  sortSubCategories(category.getChildren()) ;
				for(Category categoryChild: children) {
					String name="--"+categoryChild.getName();
					categoryUseInForm.add(Category.copyFull(categoryChild, name));
					listChildren(categoryUseInForm,categoryChild,1);
				}
			}
		}
		
		return categoryUseInForm;
	}
	private void listChildren(List<Category>categoryUseInForm,Category parent, int subLevel) {
		int newSubLevel=subLevel+1;
		Set<Category>children= sortSubCategories(parent.getChildren()) ;
		for(Category categoryChild: children) {
			String name="";
			for(int i=0;i<newSubLevel;i++ ) {
				name+="--";
				}
			name+=categoryChild.getName();
			categoryUseInForm.add(Category.copyFull(categoryChild, name));
			listChildren(categoryUseInForm, categoryChild, newSubLevel);	
			
			
		}
	}

	@Override
	public Category saveCategory(Category category) throws CategoryNotFoundException {
		boolean isUpdateCategory=(category.getId()!=null);
		if(isUpdateCategory) {
			Category existingCategory=getCategoryById(category.getId());
			if(category.getImage()==null) {
				category.setImage(existingCategory.getImage());
			}
		}
		Category parent=category.getParent();
		if(parent!=null) {
			String allParentsId=parent.getAllParentId()==null ? "-" : parent.getAllParentId();
			allParentsId += String.valueOf(parent.getId()+"-");
			category.setAllParentId(allParentsId);
		}
		return categoryRepository.save(category);
	}

	@Override
	public Category getCategoryById(Integer id)throws CategoryNotFoundException {
		Category category=categoryRepository.findById(id).orElseThrow(()-> new CategoryNotFoundException("Không tìm thấy danh mục sản phẩm có id:"+id));
		return category;
	}

	@Override
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreating=(id==null||id==0);
		Category getCategoryByName=categoryRepository.findByName(name).orElse(null);
		Category getCategoryByAlias=categoryRepository.findByAlias(alias).orElse(null);
		if(isCreating) {
			if(getCategoryByName!=null) {
				return "DuplicateName";
			}else if (getCategoryByAlias!=null) {
				return "DuplicateAlias";
			}
		}else {
			if(getCategoryByName!=null&&getCategoryByName.getId()!=id) {
				return "DuplicateName";
			}else if (getCategoryByAlias!=null && getCategoryByAlias.getId()!=id) {
				return "DuplicateAlias";
			}
		}
		return "Ok";
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> children){
		
		return sortSubCategories(children,"asc");	
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> children,String sortDir){
		SortedSet<Category>sortedChildren=new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				if(sortDir==null) {
					return o1.getName().compareTo(o2.getName());
				}else if (sortDir.equals("asc")) {
					return o1.getName().compareTo(o2.getName());
				}else if(sortDir.equals("desc")){
					return o2.getName().compareTo(o1.getName());
				}else {
					return o1.getName().compareTo(o2.getName());
				}
				
			
			}
		});
		sortedChildren.addAll(children);
		return sortedChildren;	
	}

	@Override
	public void updateEnble(Integer id, boolean enabled) {
		categoryRepository.updateEnable(id, enabled);
		
	}

	@Override
	public void deleteById(Integer id) throws CategoryNotFoundException {
		Category category=getCategoryById(id);
		categoryRepository.delete(category);
	}	
}
