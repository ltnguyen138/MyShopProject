package com.myshop.admin.setting.province;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.common.entity.Province;

@RestController
public class ProvinceRestController {

	@Autowired
	private ProvinceService provinceService;
	
	@GetMapping("/provinces/list")
	public List<Province> viewListProvinces() {
		
		return provinceService.getAllProvice();
	}
	@PostMapping("/provinces/save")
	public String save(@RequestBody Province province) {
		Province savedProvince = provinceService.saveProvince(province);
		return String.valueOf(savedProvince.getId());
	}
	
	@DeleteMapping("/provinces/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		provinceService.deleteProvince(id);
	}
	
}
