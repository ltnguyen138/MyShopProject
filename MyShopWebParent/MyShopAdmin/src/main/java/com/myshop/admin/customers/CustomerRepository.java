package com.myshop.admin.customers;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myshop.common.entity.AuthenticationType;
import com.myshop.common.entity.Customer;

@Repository
@Transactional
public interface CustomerRepository extends CrudRepository<Customer, Integer>, PagingAndSortingRepository<Customer, Integer> {
	public Optional<Customer>  findById(Integer id);
	@Query(value="SELECT c FROM Customer c WHERE c.email LIKE %:keyword% "
			+ " OR c.firstName LIKE %:keyword%"
			+ " OR c.lastName LIKE %:keyword%"
			+ " OR c.phoneNumber LIKE %:keyword%"
			+ " OR c.addressLine LIKE %:keyword%"
			+ " OR c.district LIKE %:keyword%"
			+ " OR c.province.name LIKE %:keyword%" )
	public Page< Customer> findKey(@Param("keyword") String keyword, Pageable pageable);
	
	@Modifying
	@Query(value="UPDATE customers c SET c.enabled=:enabled WHERE c.id=:id", nativeQuery = true)
	public void updateEnable(@Param("id") Integer id,@Param("enabled") boolean enabled);
	
	@Modifying
	@Query(value="UPDATE Customer c SET c.authenticationType = :type WHERE c.id=:id")
	public void updateAuthenticationTypeById(@Param("id") Integer id, @Param("type") AuthenticationType type);
	
	@Query(value="SELECT * FROM customers", nativeQuery = true)
	public Page<Customer> findAll1( Pageable pageable);
}
