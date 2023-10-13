package com.myshop.admin.shippingrate;

import java.util.List;

import org.hibernate.cfg.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myshop.admin.customers.CustomerNotFoundException;
import com.myshop.admin.setting.SettingService;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.Province;
import com.myshop.common.entity.ShippingRate;




@Controller
public class ShippingRateController {
	
	@Autowired
	ShippingRateService shippingRateService;
	@Autowired
	SettingService settingService;
	
	@GetMapping("/shipping_rates")
	public String listFirst(Model model) {
		
		return listByPage(1, model,"province","asc","");
	}
	
	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,Model model,@Param("sortField")String sortField,
			@Param("sortDir")String sortDir,@Param("keyword")String keyword ) {
		Page<ShippingRate> pageShippingRate=shippingRateService.listByPage(pageNum,sortField,sortDir,keyword);
		List<ShippingRate> listAllShippingRates=pageShippingRate.getContent();
		
		long startCount=(pageNum-1)*shippingRateService.SHIPPING_RATE_PER_PAGE+1;
		long endCount=startCount+shippingRateService.SHIPPING_RATE_PER_PAGE-1;
		if(endCount>pageShippingRate.getTotalElements())endCount=pageShippingRate.getTotalElements();
		String reverseSortDir=sortDir.equals("asc")?"desc" : "asc";
		model.addAttribute("totalPages", pageShippingRate.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageShippingRate.getTotalElements());
		model.addAttribute("listAllShippingRates", listAllShippingRates);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "shipping_rates/shipping_rates";
	}
	
	@GetMapping("/shipping_rates/new")
	public String newShippingRate(Model model) {
		List<Province> listProvincies = shippingRateService.listAllProvice();
		
		ShippingRate rate = new ShippingRate();
		rate.setId(null);
		
		model.addAttribute("pageTitle", "Thêm mới tuyến vận chuyển");
		model.addAttribute("rate", rate);
		model.addAttribute("listProvincies", listProvincies);

		return "shipping_rates/shipping_rate_form";
		
	}
	
	@PostMapping("/shipping_rates/save")
	public String saveShippingRate(RedirectAttributes redirectattributes, ShippingRate rate) throws ShippingRateNotFoundException {
	
		try {
			shippingRateService.saveShippingRate(rate);
			redirectattributes.addFlashAttribute("message", "Lưu tuyến vận chuyển thành công.");
			
		} catch (ShippingRateExistingException e) {
			redirectattributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/shipping_rates";
		
	}
	
	@GetMapping("/shipping_rates/edit/{id}")
	public String editShippingRate(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model) {
		try {
			List<Province> listProvincies = shippingRateService.listAllProvice();
			ShippingRate rate = shippingRateService.getShippingRateById(id);
			
			model.addAttribute("rate", rate);
			model.addAttribute("listProvincies", listProvincies);
			model.addAttribute("pageTitle", "Chỉnh sửa tuyến vận chuyên: "+rate.getDistrict()+ ", "+rate.getProvince().getName());
			return "/shipping_rates/shipping_rate_form";
		} catch (ShippingRateNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/shipping_rates";
		}			
	}
	
	@GetMapping("/shipping_rates/delete/{id}")
	public String deleteShippingRate(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model) {
		try {
			
			shippingRateService.deleteById(id);
			
			
			
			redirectattributes.addFlashAttribute("message", "Xóa tuyến vận chuyển thành công.");
			
		} catch (ShippingRateNotFoundException ex) {
			redirectattributes.addFlashAttribute("message",ex.getMessage());
			
		}
		return "redirect:/shipping_rates";
	}
}
