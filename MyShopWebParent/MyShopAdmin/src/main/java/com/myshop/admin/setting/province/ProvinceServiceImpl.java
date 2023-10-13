package com.myshop.admin.setting.province;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.Province;

@Component
public class ProvinceServiceImpl implements ProvinceService {
	
	@Autowired
	ProvinceRepository provinceRepository;
	
	@Override
	public List<Province> getAllProvice() {
		
		return provinceRepository.findAllByOrderByNameAsc();
	}

	@Override
	public Province saveProvince(Province province) {
		// TODO Auto-generated method stub
		return provinceRepository.save(province);
	}

	@Override
	public void deleteProvince(Integer id) {
		provinceRepository.deleteById(id);
		
	}

}
