package com.myshop.shop.district;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.common.entity.District;
import com.myshop.common.entity.Province;

@Service
public interface DistrictService {
	public List<District> listDistrictByProvince(Province province);
	
}
