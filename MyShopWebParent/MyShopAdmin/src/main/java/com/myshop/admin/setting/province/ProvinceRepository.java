package com.myshop.admin.setting.province;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.myshop.common.entity.Province;

@Transactional
@Repository
public interface ProvinceRepository extends  CrudRepository<Province, Integer>{
	public List<Province> findAllByOrderByNameAsc();
	public Optional<Province>  findById(Integer id);
}
