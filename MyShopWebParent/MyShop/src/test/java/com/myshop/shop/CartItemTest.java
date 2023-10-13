package com.myshop.shop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.myshop.common.entity.CartItem;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.product.Product;
import com.myshop.shop.shopping.cart.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemTest {
	@Autowired
	CartItemRepository cartItemRepository;
	@Autowired
	EntityManager entityManager;

	@Test
	public void testCreateCartItem() {
		Integer productId = 3;
		Integer customerId = 2;
		
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		
		CartItem cartItem = new CartItem()	;
		cartItem.setCustomer(customer);
		cartItem.setProduct(product);
		cartItem.setQuantiny(2);
		
		CartItem cartItemSaved = cartItemRepository.save(cartItem);
		
		assertThat(cartItemSaved.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCartItem2() {
		Integer productId = 4;
		Integer customerId = 2;
		
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		
		CartItem cartItem = new CartItem()	;
		cartItem.setCustomer(customer);
		cartItem.setProduct(new Product(5));
		cartItem.setQuantiny(1);
		
		CartItem cartItemSaved = cartItemRepository.save(cartItem);
		
		assertThat(cartItemSaved.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindCartItem() {
		
		Integer customerId = 2;
		
		Customer customer = entityManager.find(Customer.class, customerId);
		
		List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);
		
		for (CartItem cartItem : cartItems) {
			System.out.println("---------"+cartItem.getId());
		}
	}
	@Test
	public void testFindCartItem2() {
		
		Integer productId = 4;
		Integer customerId = 2;
		
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer,product).orElse(null);
		
		assertThat(cartItem).isNotNull();
	}
	
	@Test
	public void testUpdateCartItem() {
		
		Integer productId = 4;
		Integer customerId = 2;
		
		Customer customer = new Customer(customerId);
		Product product = new Product(productId);
		
		cartItemRepository.updateQuantity(2, customerId, productId);
		
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer,product).orElse(null);
		
		assertThat(cartItem.getQuantiny()).isEqualTo(2);
	}
	
	
	@Test
	public void testDeleteCartItem() {
		
		Integer productId = 4;
		Integer customerId = 2;
		
		Customer customer = new Customer(customerId);
		Product product = new Product(productId);
		
		cartItemRepository.dedeleteByCustomerAndProduct( customerId, productId);
		
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer,product).orElse(null);
		
		assertThat(cartItem).isNull();;
	}
	
}
