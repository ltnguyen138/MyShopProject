package com.myshop.admin.setting.district;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myshop.common.entity.District;
import com.myshop.common.entity.DistrictDTO;
import com.myshop.common.entity.Province;

@RestController
public class DistrictRestController {
	@Autowired
	DistrictService districtService;
	
	@GetMapping("/districts/list_by_province/{id}")
	public List<DistrictDTO> viewListByProvince(@PathVariable("id") Integer provinceId){
		List<District> listDistricts = districtService.listDistrictByProvince(new Province(provinceId));
		List<DistrictDTO> listDistrictDTOs= new ArrayList<>();
		for(District district : listDistricts) {
			listDistrictDTOs.add(new DistrictDTO(district.getId(), district.getName()));
		}
		return listDistrictDTOs;
	}
	
	@PostMapping("/districts/save")
	public String saveDistrict(@RequestBody District district) {
		
		
		District saveDistrict = districtService.saveDistric(district);
		System.out.println("------------"+saveDistrict);
		return String.valueOf(saveDistrict.getId());
		
	}
	
	@DeleteMapping("/districts/delete/{id}")
	public void deleteDistrict(@PathVariable("id") Integer id){
		districtService.deleteDistricById(id);
	}
}
