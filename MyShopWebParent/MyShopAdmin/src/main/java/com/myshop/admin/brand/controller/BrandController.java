package com.myshop.admin.brand.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.myshop.admin.brand.BrandNotFoundException;
import com.myshop.admin.brand.BrandService;
import com.myshop.admin.brand.export.BrandCsvExporter;
import com.myshop.admin.category.CategoryNotFoundException;
import com.myshop.admin.category.CategoryService;
import com.myshop.admin.category.export.CategoryCsvExporter;
import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Category;

@Controller
public class BrandController {
	
	@Autowired
	BrandService brandService;
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/brands")
	public String firstList(@Param("sortDir") String sortDir,Model model,String keyword) {
	
		return listByPage(1, "asc", model, "");
	}
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,@RequestParam("sortDir") String sortDir,
			Model model,@Param("keyword")String keyword) {
		Page<Brand> pageBrand=brandService.listByPage(pageNum-1, sortDir, keyword);
		List<Brand>listBrand=pageBrand.getContent();
		
		String reverseSortDir="";
		if(sortDir==null) {
			reverseSortDir="desc";
		
		}else if(sortDir.equals("asc")) {
			reverseSortDir="desc";
		}else if (sortDir.equals("desc")) {
			reverseSortDir="asc";
		}	
		List<?> es= new ArrayList<>();
		
		Brand brand = new Brand();
		es.add((Object)brand);
		
		long startCount=(pageNum-1)*brandService.BRAND_PER_PAGE+1;
		long endCount=startCount+brandService.BRAND_PER_PAGE-1;
		if(endCount>pageBrand.getTotalElements())endCount=pageBrand.getTotalElements();
		model.addAttribute("totalPages", pageBrand.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageBrand.getTotalElements());
		model.addAttribute("listAllBrands", listBrand);
		model.addAttribute("sortField",null);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "brands/brands";
	}
		
	@GetMapping("/brands/new")
	public String newBrands(Model model) {
		List<Category> listCategory=categoryService.listCategoryInForm()	;
		Brand brand = new Brand();
		brand.setId(null);
		model.addAttribute("listCategory",listCategory);
		model.addAttribute("brand",brand);
		model.addAttribute("pageTitle", "Thêm mới thương hiệu");
		return "/brands/brands_form";
	}
	@PostMapping("/brands/save")
	public String saveBrand( Brand brand,@RequestParam("images") MultipartFile multipartfile  ,
			RedirectAttributes redirectattributes) throws BrandNotFoundException, IOException {
		if(!multipartfile.isEmpty()) {
			String filename=StringUtils.cleanPath(multipartfile.getOriginalFilename());
			brand.setLogo(filename);
			Brand savedBrand= brandService.saveBrand(brand);
			String uploadDir="../brand-logo/"+savedBrand.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, filename, multipartfile);
		}else {
			brandService.saveBrand(brand);
		}
		redirectattributes.addFlashAttribute("message", "Lưu thương hiệu thành công.");
		return "redirect:/brands";
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model) {
		try {
			Brand brand=brandService.getBrandById(id);
			List<Category> listCategoriesUseInForm=categoryService.listCategoryInForm();
			model.addAttribute("listCategory",listCategoriesUseInForm);
			model.addAttribute("brand",brand);
			model.addAttribute("pageTitle", "Chỉnh sửa thương hiệu: "+brand.getName());
			return "/brands/brands_form";
		} catch (BrandNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteCategory(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model) {
		try {
			Brand brand = brandService.getBrandById(id);
			brandService.deleteById(id);
			redirectattributes.addFlashAttribute("message","Xóa thương hiệu "+brand.getName()+" thành công");
			return "redirect:/brands";
		} catch (BrandNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/export/csv")
	public void  exportCSV(HttpServletResponse respone) throws IOException {
		List<Brand> listBrands=brandService.listAll();
		BrandCsvExporter brandCsvExporter = new BrandCsvExporter();
		
		
		brandCsvExporter.export(listBrands, respone);
	}
}
