package com.myshop.admin.product;

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
	
	Optional<Product> findById(Integer id);
	Optional<Product>  findByName(String name);
	Optional<Product>  findByAlias(String alias);
	@Query(value="SELECT p FROM Product p WHERE p.name LIKE %:keyword%"
			+ " OR p.shortDescription LIKE %:keyword%"
			+ " OR p.fullDescription LIKE %:keyword%"
			+ " OR p.brand.name LIKE %:keyword% "
			+ "OR p.category.name LIKE %:keyword%")
	public Page< Product> findKey(@Param("keyword") String keyword, Pageable pageable);
	
	@Query(value="SELECT p FROM Product p WHERE p.category.id = :categoryId "
			+ "OR p.category.allParentId LIKE %:categoryAllParent% ")
	public Page<Product> findAllInCategory(@Param("categoryId") Integer categoryId,@Param("categoryAllParent")String categoryAllParent, Pageable pageable);
	
	@Query(value="SELECT p FROM Product p WHERE (p.category.id = :categoryId "
			+ "OR p.category.allParentId LIKE %:categoryAllParent%) AND"
			+" ( p.name LIKE %:keyword%"
			+ " OR p.shortDescription LIKE %:keyword%"
			+ " OR p.fullDescription LIKE %:keyword%"
			+ " OR p.brand.name LIKE %:keyword% "
			+ "OR p.category.name LIKE %:keyword%)")
	public Page<Product> findCategoryWithKeyword(@Param("categoryId") Integer categoryId,@Param("categoryAllParent")String categoryAllParent, 
			@Param("keyword") String keyword, Pageable pageable);
	
	@Modifying
	@Query(value="UPDATE products u SET u.enabled=:enabled WHERE u.id=:id", nativeQuery = true)
	public void updateEnable(@Param("id") Integer id,@Param("enabled") boolean enabled);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public Page<Product> searchProductsByName(String keyword, Pageable pageable);
}
