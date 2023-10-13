package com.myshop.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.myshop.admin.category.CategoryPageInfo;
import com.myshop.common.entity.Brand;


@Service
public interface BrandService {
	public static final int BRAND_PER_PAGE=6;
	public Brand getBrandById(Integer id) throws BrandNotFoundException;
	public Page<Brand> listByPage(int pageNum,String sortDir,String keyword);
	public Brand saveBrand(Brand brand) throws BrandNotFoundException;
	public String checkUnique(Integer id, String name);
	public void deleteById(int id) throws BrandNotFoundException;
	public List<Brand> listAll();
}
	