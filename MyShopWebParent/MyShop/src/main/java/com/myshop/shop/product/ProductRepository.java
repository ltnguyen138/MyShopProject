package com.myshop.shop.product;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.myshop.common.entity.product.Product;

@Transactional
public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {
	
	
	@Query(value="SELECT p FROM Product p WHERE p.enabled=true "
			+ "AND (p.category.id = :categoryId "
			+ "OR p.category.allParentId LIKE %:categoryAllParent%) ORDER BY p.name ASC ")
	public Page<Product> listByCategory(@Param("categoryId") Integer categoryId,@Param("categoryAllParent")String categoryAllParent, Pageable pageable);
	
	public Optional<Product> findByAlias(String alias);
	
	@Query(value="SELECT p FROM Product p WHERE p.enabled=true  AND (p.name LIKE %:keyword% "
			+ "OR p.shortDescription LIKE %:keyword% "
			+ "OR p.fullDescription LIKE %:keyword%)" )
	public Page< Product> findKey(@Param("keyword") String keyword, Pageable pageable);
}
