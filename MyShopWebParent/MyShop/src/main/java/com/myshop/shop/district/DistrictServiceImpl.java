package com.myshop.shop.district;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.District;
import com.myshop.common.entity.Province;

@Component
public class DistrictServiceImpl implements DistrictService{
	@Autowired
	DistrictRepostiory districtRepostiory;

	@Override
	public List<District> listDistrictByProvince(Province province) {
		
		return districtRepostiory.findByProvinceOrderByNameAsc(province);
	}

	

	
	
	
}
