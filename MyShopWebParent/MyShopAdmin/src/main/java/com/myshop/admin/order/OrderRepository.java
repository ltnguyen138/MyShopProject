package com.myshop.admin.order;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myshop.common.entity.Customer;
import com.myshop.common.entity.order.Order;
import com.myshop.common.entity.product.Product;

@Repository
@Transactional
public interface OrderRepository extends CrudRepository<Order, Integer>, PagingAndSortingRepository<Order, Integer> {
	public Optional<Order> findById(Integer id);
	
	@Query(value="SELECT o FROM Order o WHERE o.firstName LIKE %:keyword%"
			+ " OR o.lastName LIKE %:keyword%"
			+ " OR o.phoneNumber LIKE %:keyword%"
			+ " OR o.addressLine LIKE %:keyword%"
			+ " OR o.district LIKE %:keyword%"
			+ " OR o.province LIKE %:keyword%" )
	public Page< Order> findKey(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("SELECT NEW com.myshop.common.entity.order.Order(o.id, o.orderTime, o.productCost,"
			+ " o.subtotal, o.total) FROM Order o WHERE"
			+ " o.orderTime BETWEEN ?1 AND ?2 ORDER BY o.orderTime ASC")
	public List<Order> findByOrderTimeBetween(Date startTime, Date endTime);
}
