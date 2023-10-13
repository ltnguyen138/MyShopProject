package com.myshop.admin.shippingrate;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.myshop.admin.customers.CustomerNotFoundException;

import com.myshop.common.entity.Province;
import com.myshop.common.entity.ShippingRate;

@Service
public interface ShippingRateService {
	public static final int SHIPPING_RATE_PER_PAGE=6;
	
	public ShippingRate getShippingRateById(Integer id) throws ShippingRateNotFoundException;
	public Page<ShippingRate> listByPage(int pageNum, String sortField,String sortDir,String keyword);
	public ShippingRate saveShippingRate(ShippingRate shippingRate) throws ShippingRateNotFoundException, ShippingRateExistingException;
	public List<Province> listAllProvice();
	public void deleteById(Integer id) throws ShippingRateNotFoundException;
	
	public Double calculateShippingCost(Integer productId, Integer provinceId, String district) 
			throws ShippingRateNotFoundException;
}
