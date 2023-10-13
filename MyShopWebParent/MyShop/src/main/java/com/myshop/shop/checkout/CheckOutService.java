package com.myshop.shop.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.common.entity.CartItem;
import com.myshop.common.entity.ShippingRate;
import com.myshop.common.entity.product.Product;

@Service
public class CheckOutService {
	private static final int DIM_DIVISOR = 139;
	
	public CheckOutInfo prepareCheckOut(List<CartItem> cartItems, ShippingRate rate) {
		CheckOutInfo checkOutInfo = new CheckOutInfo();
		
		Double productCostTotal = calculateproductCost(cartItems);
		Double productPriceTotal = calculateproductPrice(cartItems);
		Double shippingCostTotal = calculateShippingCost(cartItems,rate);
		
		checkOutInfo.setProductCostTotal(productCostTotal);
		checkOutInfo.setProductPriceTotal(productPriceTotal);
		checkOutInfo.setShippingCostTotal(shippingCostTotal);
		checkOutInfo.setPaymentTotal(shippingCostTotal+productPriceTotal);
		
		checkOutInfo.setDeliverDays(rate.getDays());
		
		
		return checkOutInfo;
	}

	private Double calculateShippingCost(List<CartItem> cartItems, ShippingRate rate) {
		
		Double shippingCostTotal = 0D;
		for (CartItem item : cartItems) {
			Product product = item.getProduct();
			Double dimWeight = (double) ((product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR);
			Double finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
			Double shippingCost = finalWeight * item.getQuantiny() * rate.getCost();
			
			item.setShippingCost(shippingCost);
			
			shippingCostTotal += shippingCost;
		}
		return shippingCostTotal;
	}

	private Double calculateproductPrice(List<CartItem> cartItems) {

		Double price = 0D;
		
		for(CartItem item :cartItems) {
			price += item.getSubtotal();
		}
		return price;
	}

	private Double calculateproductCost(List<CartItem> cartItems) {
		Double cost = 0D;
		
		for(CartItem item :cartItems) {
			cost += item.getQuantiny()*item.getProduct().getCost();
		}
		return cost;
	}
}
