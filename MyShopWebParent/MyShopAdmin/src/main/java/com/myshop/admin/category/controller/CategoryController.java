package com.myshop.admin.category.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myshop.admin.FileUploadUtil;
import com.myshop.admin.category.CategoryNotFoundException;
import com.myshop.admin.category.CategoryPageInfo;
import com.myshop.admin.category.CategoryService;
import com.myshop.admin.category.export.CategoryCsvExporter;
import com.myshop.admin.user.export.UserCsvExporter;
import com.myshop.common.entity.Category;
import com.myshop.common.entity.User;

@Controller
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/categories")
	public String listFirst(CategoryPageInfo categoryPageInfo,@Param("sortDir") String sortDir,Model model,String keyword) {
		return listByPage(categoryPageInfo,1, "asc", model,"");
	}
	
	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(CategoryPageInfo categoryPageInfo,@PathVariable("pageNum") int pageNum,@Param("sortDir") String sortDir,
			Model model,@Param("keyword")String keyword) {
		List<Category>listCategories=categoryService.listAll(categoryPageInfo,pageNum,sortDir,keyword);
		String reverseSortDir="";
		if(sortDir==null) {
			reverseSortDir="desc";
		
		}else if(sortDir.equals("asc")) {
			reverseSortDir="desc";
		}else if (sortDir.equals("desc")) {
			reverseSortDir="asc";
		}	
		long startCount=(pageNum-1)*categoryService.CATEGORY_PER_PAGE+1;
		long endCount=startCount+categoryService.CATEGORY_PER_PAGE-1;
		model.addAttribute("sortField", null);
		
		model.addAttribute("listAllCategoties", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalPages", categoryPageInfo.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", categoryPageInfo.getTotalElements());
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", null);
		return "categories/categories";
	}
	
//	@GetMapping("/categories/page/{pageNum}")
//	public String listByPage(@PathVariable("pageNum") int pageNum,Model model,@Param("sortField")String sortField,
//			@Param("sortDir")String sortDir,@Param("keyword")String keyword ) {
//		
//		Page<Category> pageCategory=categoryService.listByPage(pageNum, sortField, sortDir, keyword);
//		List<Category>listCategory=pageCategory.getContent();
//		long startCount=(pageNum-1)*categoryService.CATEGORY_PER_PAGE+1;
//		long endCount=startCount+categoryService.CATEGORY_PER_PAGE-1;
//		if(endCount>pageCategory.getTotalElements()) endCount=pageCategory.getTotalElements();
//		String reverseSortDir=sortDir.equals("asc")?"desc" : "asc";
//		model.addAttribute("totalPages", pageCategory.getTotalPages());
//		model.addAttribute("pageNum", pageNum);
//		model.addAttribute("startCount", startCount);
//		model.addAttribute("endCount", endCount);
//		model.addAttribute("totalItems", pageCategory.getTotalElements());
//		model.addAttribute("listAllCategoties", listCategory);
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDir);
//		model.addAttribute("reverseSortDir", reverseSortDir);
//		model.addAttribute("keyword", keyword);
//		return "categories/categories";
//	}
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> listCategory=categoryService.listCategoryInForm()	;
		Category category=new Category();
		category.setEnabled(true);
		category.setId(null);
		model.addAttribute("listCategory",listCategory);
		model.addAttribute("category",category );
		model.addAttribute("pageTitle", "Thêm mới danh mục sản phẩm");
		return "categories/categories_form"	;	 
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("images") MultipartFile multipartfile  ,
			RedirectAttributes redirectattributes) throws IOException, CategoryNotFoundException {
		if(!multipartfile.isEmpty()) {
			String filename=StringUtils.cleanPath(multipartfile.getOriginalFilename());
			category.setImage(filename);
			Category savedCategory= categoryService.saveCategory(category);
			String uploadDir="../category-images/"+savedCategory.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, filename, multipartfile);
		}else {
			categoryService.saveCategory(category);
		}
		
		
		
		redirectattributes.addFlashAttribute("message", "Lưu thương hiệu thành công.");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model) {
		try {
			Category category=categoryService.getCategoryById(id);
			List<Category> listCategoriesUseInForm=categoryService.listCategoryInForm();
			model.addAttribute("listCategory",listCategoriesUseInForm);
			model.addAttribute("category",category );
			model.addAttribute("pageTitle", "Chỉnh sửa danh mục sản phẩm: "+category.getName());
			return "/categories/categories_form";
		} catch (CategoryNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/categories";
		}
	}
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateEnbledCategory(@PathVariable("id") int id,@PathVariable("status") Boolean status,
			RedirectAttributes redirectattributes) {
		categoryService.updateEnble(id, status);
		redirectattributes.addFlashAttribute("message", "Cập nhật trạng thái danh mục thành công.");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model) {
		try {
			categoryService.deleteById(id);
			redirectattributes.addFlashAttribute("message","Xóa danh mục sản phẩm thành công");
			return "redirect:/categories";
		} catch (CategoryNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/export/csv")
	public void  exportCSV(HttpServletResponse respone) throws IOException {
		List<Category> listCategories=categoryService.listCategoryInForm();
		CategoryCsvExporter categoryCsvExporter=new CategoryCsvExporter();
		categoryCsvExporter.export(listCategories, respone);
	}
}
