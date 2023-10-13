package com.myshop.shop.shippingrate;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;


import com.myshop.common.entity.Province;
import com.myshop.common.entity.ShippingRate;

@Component
public class ShippingRateServiceImpl implements ShippingRateService{

	@Autowired
	ShippingRateRepository shippingRateRepository;

	@Override
	public ShippingRate getShippingRateByAddress(Province province, String district)
			throws ShippingRateNotFoundException {
	
		ShippingRate shippingRate = shippingRateRepository.findByProvinceAndDistrict(province.getId(), district);
		if (shippingRate==null) {
			throw new ShippingRateNotFoundException("không tìm thấy tuyến giao hàng");
		}
		return shippingRate;
	}
	
	
}
