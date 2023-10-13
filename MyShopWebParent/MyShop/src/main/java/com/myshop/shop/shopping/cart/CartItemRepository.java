package com.myshop.shop.shopping.cart;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.myshop.common.entity.CartItem;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.product.Product;

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
	public List<CartItem> findByCustomer(Customer customer);
	public Optional<CartItem>  findByCustomerAndProduct(Customer customer, Product product);
	
	@Modifying
	@Query("UPDATE CartItem c SET c.quantiny = :quantity WHERE c.customer.id = :customerId AND c.product.id= :productId")
	public void updateQuantity(@Param("quantity") Integer quantity,@Param("customerId") Integer customerId,
			@Param("productId") Integer productId);
	@Modifying
	@Query("DELETE FROM CartItem c  WHERE c.customer.id = :customerId AND c.product.id= :productId")
	public void dedeleteByCustomerAndProduct(@Param("customerId") Integer customerId, @Param("productId") Integer productId);

	@Modifying
	@Query("DELETE FROM CartItem c  WHERE c.customer.id = :customerId ")
	public void dedeleteByCustomer(@Param("customerId") Integer customerId);
}
