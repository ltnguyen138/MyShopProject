package com.myshop.shop.shopping.cart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.CartItem;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.product.Product;
import com.myshop.shop.product.ProductRepository;
import com.myshop.shop.product.ProductService;

@Component
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	CartItemRepository cartItemRepository;
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public CartItem getCartItemByCustomerAndProduct1(Customer customer, Product product) throws CartItemNotFoundException {
		return cartItemRepository.findByCustomerAndProduct(customer, product).orElseThrow(()-> new CartItemNotFoundException("Không tìm thấy sản phẩm này trong giỏ hàng"));
	}

	@Override
	public CartItem getCartItemByCustomerAndProduct2( Customer customer, Product product) {
		return cartItemRepository.findByCustomerAndProduct(customer, product).orElse(null);
	}

	@Override
	public Integer addProduct(Customer customer, Integer productId, Integer quantity) {
		
		Integer updateQuantity = quantity;		
		Product product= new Product(productId);
		
		CartItem cartItem = getCartItemByCustomerAndProduct2(customer, product);
		if(cartItem!=null) {
			updateQuantity= cartItem.getQuantiny()+updateQuantity;
			cartItem.setQuantiny(updateQuantity);
		}else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}		
		cartItem.setQuantiny(updateQuantity);
		
		cartItemRepository.save(cartItem);
		
		return updateQuantity;
		
	}

	@Override
	public List<CartItem> getCartByCustomer(Customer customer) {
		return cartItemRepository.findByCustomer(customer);
	}

	@Override
	public List<Double> updateProduct(Customer customer, Integer productId, Integer quantity) {
		
		cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepository.findById(productId).orElse(null);
	
		Double subTotalPrice = product.getDiscountPrice()*quantity;
		Double subTotalDiscount = product.getDiscountValue()*quantity;
		List<Double> priceAndDiscountValue = new ArrayList<>();
		priceAndDiscountValue.add(subTotalPrice);
		priceAndDiscountValue.add(subTotalDiscount);
		return priceAndDiscountValue;
	}

	@Override
	public void removeProduct(Customer customer, Integer productId) {
		cartItemRepository.dedeleteByCustomerAndProduct(customer.getId(), productId);
		
	}

	@Override
	public void deleteByCustomer(Customer customer) {
		cartItemRepository.dedeleteByCustomer(customer.getId());
		
	}

}
