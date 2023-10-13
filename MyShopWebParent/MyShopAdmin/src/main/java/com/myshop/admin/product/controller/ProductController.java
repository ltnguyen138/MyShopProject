package com.myshop.admin.product.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.myshop.admin.brand.BrandNotFoundException;
import com.myshop.admin.brand.BrandService;
import com.myshop.admin.category.CategoryService;
import com.myshop.admin.product.ProductNotFoundException;
import com.myshop.admin.product.ProductSaveHelper;
import com.myshop.admin.product.ProductService;
import com.myshop.admin.security.MyShopUserDetail;
import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Category;
import com.myshop.common.entity.product.Product;
import com.myshop.common.entity.product.ProductImage;

@Controller
public class ProductController {

	@Autowired
	ProductService productService;
	@Autowired
	BrandService brandService;
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/products")
	public String firstPage(Model model){
		return listByPage(1, model, "name", "asc", "",0);
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,Model model,@Param("sortField")String sortField,
			@Param("sortDir")String sortDir,@Param("keyword")String keyword,@Param("categoryId")Integer categoryId ) {
		
		System.out.print(categoryId);
		Page<Product> pageProducts = productService.listByPage(pageNum, sortField, sortDir, keyword,categoryId);
		List<Product> listProducts=pageProducts.getContent();
		long startCount=(pageNum-1)*productService.PRODUCTS_PER_PAGE+1;
		long endCount=startCount+productService.PRODUCTS_PER_PAGE-1;
		if(endCount>pageProducts.getTotalElements())endCount=pageProducts.getTotalElements();
		String reverseSortDir=sortDir.equals("asc")?"desc" : "asc";
		
		List<Category> listcategories=categoryService.listCategoryInForm();
		if(categoryId>=0)model.addAttribute("categoryId", categoryId);
		System.out.print(categoryId);
		model.addAttribute("listcategories", listcategories);
		model.addAttribute("totalPages",pageProducts.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("listAllProducts", listProducts);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProducts(Model model) {
		List<Brand> listBrand=brandService.listAll()	;
		Product product = new Product();
		Integer numberExitsExtraImages=product.getImages().size();
		product.setId(null);
		product.setEnabled(true);
		product.setQuantity(1);
		model.addAttribute("numberExitsExtraImages",numberExitsExtraImages);
		model.addAttribute("listBrand",listBrand);
		model.addAttribute("product",product);
		model.addAttribute("pageTitle", "Thêm mới sản phẩm");
		return "/products/products_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product,RedirectAttributes redirectattributes
			,@RequestParam(name="fileImage", required = false) MultipartFile mainImageMultipart
			,@RequestParam(name="extraFileImage", required = false) MultipartFile[] extraImageMultipart
			,@RequestParam(name="detailId", required = false) String[] detailId
			,@RequestParam(name="detailNames", required = false) String[] detailNames
			,@RequestParam(name="detailValues", required = false) String[] detailValues
			,@RequestParam(name="extraImagesId", required = false) String[] extraImagesId
			,@RequestParam(name="extraImagesName", required = false) String[] extraImagesName
			,@AuthenticationPrincipal MyShopUserDetail loggedUser) 
					throws BrandNotFoundException, IOException, ProductNotFoundException {
		if(loggedUser.hasRole("Salesperson")) {
			productService.saveProductPrice(product);
			redirectattributes.addFlashAttribute("message", "Lưu sản phẩm thành công.");
			return  "redirect:/products";
		}
		ProductSaveHelper.setMainImageName(product, mainImageMultipart);
		ProductSaveHelper.setExistExtraImages(product,extraImagesId,extraImagesName);
		ProductSaveHelper.setNewExtraImageName(product, extraImageMultipart);
		ProductSaveHelper.setProductDetails(product,detailId,detailNames,detailValues)	;
		
		Product saveProduct =productService.saveProduct(product);
		ProductSaveHelper.saveUploadImages(saveProduct, mainImageMultipart, extraImageMultipart);
		
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
		
		redirectattributes.addFlashAttribute("message", "Lưu sản phẩm thành công.");
		return  "redirect:/products";
	}
	
	
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateEnbledCategory(@PathVariable("id") int id,@PathVariable("status") Boolean status,
			RedirectAttributes redirectattributes) {
		productService.updateEnble(id, status);
		redirectattributes.addFlashAttribute("message", "Cập nhật trạng thái danh mục thành công.");
		return "redirect:/products";
	}
	
	@GetMapping("products/delete/{id}")
	public String deleteProduct(@PathVariable("id") int id,RedirectAttributes redirectattributes)  {
		try {
			productService.deleteProduct(id);
			
			
			String extraImageDir="../product-images/"+id+"/extras";
			FileUploadUtil.cleanDir(extraImageDir);
			FileUploadUtil.removeDir(extraImageDir);
			
			String mainImageDir="../product-images/"+id;
			FileUploadUtil.cleanDir(mainImageDir);
			FileUploadUtil.removeDir(mainImageDir);
			
			redirectattributes.addFlashAttribute("message", "Xóa sản phẩm có id "+id+" thành công");
			
		} catch (ProductNotFoundException e) {
			redirectattributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") int id, Model model, RedirectAttributes redirectattributes,
			@AuthenticationPrincipal MyShopUserDetail loggedUser ) {
		
		try {
			Product product= productService.getProductById(id);
			List<Brand> listBrand=brandService.listAll()	;
			Integer numberExitsExtraImages=product.getImages().size();
			
			boolean isReadOnlyForSales = false;
			if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
				if(loggedUser.hasRole("Salesperson")) {
					isReadOnlyForSales = true;
				}
			}
			
			model.addAttribute("isReadOnlyForSales",isReadOnlyForSales);
			model.addAttribute("product",product);
			model.addAttribute("listBrand",listBrand);
			model.addAttribute("numberExitsExtraImages",numberExitsExtraImages);
			model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm: "+product.getName());
			
		} catch (ProductNotFoundException e) {
			redirectattributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		
		return "/products/products_form";
	}
	

	@GetMapping("/products/detail/{id}")
	public String detailProduct(@PathVariable("id") int id, Model model, RedirectAttributes redirectattributes) {
		
		try {
			Product product= productService.getProductById(id);
			
			
			model.addAttribute("product",product);
			
			model.addAttribute("pageTitle", "Chi tiết sản phẩm: "+product.getName());
			return "/products/products_detail_modal";
			
		} catch (ProductNotFoundException e) {
			redirectattributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/products";
		}
		
		
	}
}








