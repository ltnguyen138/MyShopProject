package com.myshop.admin.shippingrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.myshop.admin.product.ProductRepository;
import com.myshop.admin.setting.province.ProvinceRepository;
import com.myshop.common.entity.Province;
import com.myshop.common.entity.ShippingRate;
import com.myshop.common.entity.product.Product;

@Component
public class ShippingRateServiceImpl implements ShippingRateService{

	@Autowired
	ShippingRateRepository shippingRateRepository;
	@Autowired
	ProvinceRepository provinceRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	private static final int DIM_DIVISOR = 139;
	
	@Override
	public ShippingRate getShippingRateById(Integer id) throws ShippingRateNotFoundException {
		return shippingRateRepository.findById(id).orElseThrow(()->new ShippingRateNotFoundException("Không tìm thấy tuyến vận chuyển này"));
	}

	@Override
	public Page<ShippingRate> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort=Sort.by(sortField);
		if(sortDir==null) {
			sort=sort.ascending();
		
		}else if(sortDir.equals("asc")) {
			sort=sort.ascending();
		}else if(sortDir.equals("desc")){
			sort=sort.descending();
		}
		
		Pageable pageable=PageRequest.of(pageNum-1, SHIPPING_RATE_PER_PAGE,sort);
		if (keyword==null) {
			return shippingRateRepository.findAll(pageable);
		}
		return shippingRateRepository.findKey(keyword, pageable);
	}

	@Override
	public ShippingRate saveShippingRate(ShippingRate shippingRateInFrom) throws ShippingRateNotFoundException, ShippingRateExistingException {
		
		ShippingRate shippingRateInDb = shippingRateRepository.findByProvinceAndDistrict(shippingRateInFrom.getProvince().getId(), shippingRateInFrom.getDistrict());
		boolean isExistingInNewMode = shippingRateInFrom.getId()== null && shippingRateInDb != null;
		boolean isExistingInEditMode = shippingRateInFrom.getId()!= null && shippingRateInDb != null && shippingRateInFrom.getId()!= shippingRateInDb.getId();
		
		if(isExistingInEditMode || isExistingInNewMode) {
			throw new ShippingRateExistingException("Tuyến vận chuyển đi " + shippingRateInFrom.getDistrict() + ", " 
					+ shippingRateInFrom.getProvince().getName() + " đã tồn tại");
		}
		return shippingRateRepository.save(shippingRateInFrom);
	}

	@Override
	public List<Province> listAllProvice() {
		return provinceRepository.findAllByOrderByNameAsc();
	}

	@Override
	public void deleteById(Integer id) throws ShippingRateNotFoundException {

		ShippingRate rate = getShippingRateById(id);
		shippingRateRepository.delete(rate);
	}

	@Override
	public Double calculateShippingCost(Integer productId, Integer provinceId, String district)
			throws ShippingRateNotFoundException {
		
		ShippingRate shippingRate= shippingRateRepository.findByProvinceAndDistrict(productId, district);
		if (shippingRate == null) {
			throw new ShippingRateNotFoundException("Không tìm thấy tuyến vận chuyển");
		}
		
		Product product = productRepository.findById(productId).orElse(null);
		
		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
		
		return finalWeight * shippingRate.getCost();
	}

}
