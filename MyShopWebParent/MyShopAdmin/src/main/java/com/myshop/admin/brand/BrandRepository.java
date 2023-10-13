package com.myshop.admin.brand;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Category;

@Transactional
@Repository
public interface BrandRepository extends CrudRepository<Brand, Integer>, PagingAndSortingRepository<Brand, Integer>{
	public Optional<Brand>  findById(Integer id);
	public Optional<Brand>  findByName(String name);
	
	@Query(value="SELECT u FROM Brand u WHERE u.name LIKE %:keyword% ")
	public Page< Brand> findKey(@Param("keyword") String keyword, Pageable pageable);
	
	@Query(value="SELECT u FROM Brand u  ")
	public List<Brand> findAll(Sort sort);
	
	
}
