package com.myshop.admin.setting.district;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.myshop.common.entity.District;
import com.myshop.common.entity.Province;

@Transactional
@Repository
public interface DistrictRepostiory extends CrudRepository<District, Integer>{
	public List<District> findByProvinceOrderByNameAsc(Province province);

}
