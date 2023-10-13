package com.myshop.admin.category;

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
	public Optional<Category> findById(Integer id);
	public Optional<Category> findByName(String name);
	public Optional<Category> findByAlias(String alias);
	@Query(value="SELECT u FROM Category u WHERE u.name LIKE %:keyword% OR u.alias LIKE %:keyword%  ")
	public Page< Category> findKey(@Param("keyword") String keyword, Pageable pageable);
	@Query(value="SELECT u FROM Category u WHERE u.parent.id IS NULL   ")
	public List<Category> findRootCategories(Sort sort);
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is null")
	public Page<Category> findRootCategories(Pageable pageable);
	
	@Modifying
	@Query(value="UPDATE categories u SET u.enabled=:enabled WHERE u.id=:id", nativeQuery = true)
	public void updateEnable(@Param("id") Integer id,@Param("enabled") boolean enabled);
}
