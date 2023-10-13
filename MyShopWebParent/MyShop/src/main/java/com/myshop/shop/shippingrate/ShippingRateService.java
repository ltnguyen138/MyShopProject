package com.myshop.shop.shippingrate;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;



import com.myshop.common.entity.Province;
import com.myshop.common.entity.ShippingRate;

@Service
public interface ShippingRateService {
	
	
	public ShippingRate getShippingRateByAddress(Province province, String district) throws ShippingRateNotFoundException;
	
}
