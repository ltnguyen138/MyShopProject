package com.myshop.shop.shopping.cart;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.common.entity.CartItem;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.product.Product;

@Service
public interface CartItemService {
	
	public CartItem getCartItemByCustomerAndProduct1(Customer customer, Product product) throws CartItemNotFoundException;
	public CartItem getCartItemByCustomerAndProduct2(Customer customer, Product product);
	public Integer addProduct(Customer customer, Integer productId, Integer quantity);
	public List<Double> updateProduct(Customer customer, Integer productId, Integer quantity);
	public List<CartItem> getCartByCustomer(Customer customer);
	public void removeProduct(Customer customer, Integer productId);
	public void deleteByCustomer(Customer customer);
}
