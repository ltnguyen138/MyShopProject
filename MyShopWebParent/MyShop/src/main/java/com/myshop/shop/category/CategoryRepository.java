package com.myshop.shop.category;

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

import com.myshop.common.entity.Category;

@Transactional
@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>,PagingAndSortingRepository<Category, Integer>  {
	
	@Query("SELECT c FROM Category c WHERE c.enabled=true ORDER BY c.name ASC")
	public List<Category> findAllEnabled();
	
	@Query("SELECT c FROM Category c WHERE c.enabled=true AND c.alias=:alias ORDER BY c.name ASC")
	public Optional<Category> findByAliasEnabled(@Param("alias") String alias);
}
