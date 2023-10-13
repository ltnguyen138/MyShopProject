package com.myshop.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Category;

@Component
public class BrandServiceImpl implements BrandService{

	@Autowired
	BrandRepository brandRepository;
	@Override
	public Brand getBrandById(Integer id) throws BrandNotFoundException {
		Brand brand=brandRepository.findById(id).orElseThrow(()-> new BrandNotFoundException("Không tìm thấy thương hiệu"));
		return brand;
	}

	@Override
	public Page<Brand> listByPage(int pageNum, String sortDir, String keyword) {
		Sort sort=Sort.by("name");
		if(sortDir==null) {
			sort=sort.ascending();
		
		}else if(sortDir.equals("asc")) {
			sort=sort.ascending();
		}else if(sortDir.equals("desc")){
			sort=sort.descending();
		}
		Pageable pageable=PageRequest.of(pageNum, BRAND_PER_PAGE,sort);
		if (keyword==null) return  brandRepository.findAll(pageable);
		return brandRepository.findKey(keyword, pageable);
		
	}

	@Override
	public Brand saveBrand(Brand brand) throws BrandNotFoundException {
		boolean isUpdateBrand=(brand.getId()!=null);
		if(isUpdateBrand) {
			Brand existingBrand = getBrandById(brand.getId());
			if(brand.getLogo()==null) brand.setLogo(existingBrand.getLogo());
			
		}
		return brandRepository.save(brand);
	}

	@Override
	public String checkUnique(Integer id, String name) {
		boolean isCreating=(id==null||id==0);
		Brand getBrandByName=brandRepository.findByName(name).orElse(null);
		
		if(isCreating) {
			if(getBrandByName!=null) {
				return "DuplicateName";
			}
		}else {
			if(getBrandByName!=null&&getBrandByName.getId()!=id) {
				return "DuplicateName";
			}
		}
		return "Ok";
		
		
	}

	@Override
	public void deleteById(int id) throws BrandNotFoundException {
		Brand brand=getBrandById(id);
		brandRepository.delete(brand);
		
	}

	@Override
	public List<Brand> listAll() {
		Sort sort = Sort.by("id").ascending();
		List<Brand> brand= brandRepository.findAll(sort);
		
		return brand;
	}
	
}
