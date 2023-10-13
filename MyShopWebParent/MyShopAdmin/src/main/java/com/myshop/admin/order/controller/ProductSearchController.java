package com.myshop.admin.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.myshop.admin.product.ProductService;
import com.myshop.common.entity.product.Product;

@Controller
public class ProductSearchController {
	@Autowired
	ProductService productService;
		
	@GetMapping("/orders/search_product")
	public String showSearchProductPage() {
		return "orders/search_product";
	}
	
	@PostMapping("/orders/search_product")
	public String searchProducr(String keyword) {
		return "redirect:/orders/search_product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}
	
	@GetMapping("/orders/search_product/page/{pageNum}")
	public String searchProductsByPage(@PathVariable("pageNum") int pageNum,Model model,@Param("sortField")String sortField,
			@Param("sortDir")String sortDir,@Param("keyword")String keyword) {
		Page<Product> pageProducts= productService.searchProduct(pageNum, sortField, sortDir, keyword);
		List<Product> listProducts=pageProducts.getContent();
		long startCount=(pageNum-1)*productService.PRODUCTS_PER_PAGE+1;
		long endCount=startCount+productService.PRODUCTS_PER_PAGE-1;
		if(endCount>pageProducts.getTotalElements())endCount=pageProducts.getTotalElements();
		String reverseSortDir=sortDir.equals("asc")?"desc" : "asc";
		
		model.addAttribute("totalPages",pageProducts.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "orders/search_product";
	}
}
