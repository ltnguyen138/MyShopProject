package com.myshop.shop.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.myshop.common.entity.Category;
import com.myshop.common.entity.product.Product;
import com.myshop.shop.category.CategoryNotFoundException;
import com.myshop.shop.category.CategoryService;
import com.myshop.shop.product.ProductNotFoundException;
import com.myshop.shop.product.ProductService;

@Controller
public class ProductController {
	@Autowired
	ProductService productService;
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/c/{c_alias}")
	public String viewFirstPageProductByCategory(@PathVariable("c_alias") String alias,Model model) {
		return viewPageProductByCategory(alias, model, 1); 
	}
	
	@GetMapping("/c/{c_alias}/page/{pageNum}")
	public String viewPageProductByCategory(@PathVariable("c_alias") String alias,Model model,
			@PathVariable("pageNum") int pageNum) {
		
		try {
			Category category= categoryService.getCategoryByAlias(alias);
			List<Category>listCategoriesParents = categoryService.listParentCategories(category);
			
			Page<Product> pageProducts=productService.listByCategory(pageNum, category.getId());
			List<Product>listProducts=pageProducts.getContent();
			
			
			long startCount=(pageNum-1)*productService.PRODUCTS_PER_PAGE+1;
			long endCount=startCount+productService.PRODUCTS_PER_PAGE-1;
			if(endCount>pageProducts.getTotalElements())  endCount=pageProducts.getTotalElements();
						
			model.addAttribute("totalPages",pageProducts.getTotalPages());
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", pageProducts.getTotalElements());
			model.addAttribute("listAllProducts", listProducts);
			model.addAttribute("sortDir", "");
			model.addAttribute("pageTitle", "Danh sách sản phẩm "+category.getName());
			model.addAttribute("listCategoriesParents", listCategoriesParents);
			model.addAttribute("listProducts", listProducts);
			model.addAttribute("category", category);
			return "product/products_by_category";
		} catch (CategoryNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/p/{p_alias}")
	public String viewProductDetail(@PathVariable("p_alias") String alias,Model model) {
		
		try {
			Product product = productService.getProductByAlias(alias);
			List<Category>listCategoriesParents = categoryService.listParentCategories(product.getCategory());
			
			model.addAttribute("listCategoriesParents", listCategoriesParents);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getName());
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/search")
	public String viewFirstPageSearchProduct(@Param("keyword") String keyword, Model model) {
		return viewPageSearchProduct(keyword, model, 1);
	}
	
	
	@GetMapping("/search/page/{pageNum}")
	public String viewPageSearchProduct(@Param("keyword") String keyword, Model model , @PathVariable("pageNum") int pageNum) {
		
		Page<Product> pageProducts= productService.searchProduct(pageNum, keyword);
		List<Product> listProducts=pageProducts.getContent();
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("listProducts", listProducts);
		long startCount=(pageNum-1)*productService.PRODUCTS_PER_PAGE+1;
		long endCount=startCount+productService.PRODUCTS_PER_PAGE-1;
		if(endCount>pageProducts.getTotalElements())  endCount=pageProducts.getTotalElements();
					
		model.addAttribute("totalPages",pageProducts.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("listAllProducts", listProducts);
		model.addAttribute("sortDir", "");
		model.addAttribute("pageTitle", "Kết quả tìm kiếm: "+keyword);
		
		
		return "product/search_product";
	}
}
