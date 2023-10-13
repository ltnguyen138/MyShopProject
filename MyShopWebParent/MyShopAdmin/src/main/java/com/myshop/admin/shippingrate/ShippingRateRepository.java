package com.myshop.admin.shippingrate;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


import com.myshop.common.entity.ShippingRate;

@Transactional
public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer>, PagingAndSortingRepository<ShippingRate, Integer>{
	@Query("SELECT s FROM ShippingRate s WHERE s.province.id = :provinceId AND s.district= :district" )
	public ShippingRate findByProvinceAndDistrict(@Param("provinceId") Integer provinceId,@Param("district") String districtId);
	
	@Query("SELECT s FROM ShippingRate s WHERE s.province.name LIKE %:keyword% OR s.district LIKE %:keyword% ")
	public Page<ShippingRate> findKey(@Param("keyword") String keyword, Pageable pageable);
	
	public Optional<ShippingRate>  findById(Integer id);
}
