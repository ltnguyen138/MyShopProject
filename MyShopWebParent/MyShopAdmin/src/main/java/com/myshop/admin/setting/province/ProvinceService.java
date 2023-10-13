package com.myshop.admin.setting.province;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.common.entity.Province;

@Service
public interface ProvinceService {
	public List<Province> getAllProvice();
	public Province saveProvince(Province province);
	public void deleteProvince(Integer id);
}
