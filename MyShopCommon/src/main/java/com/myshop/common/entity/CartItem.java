package com.myshop.common.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.myshop.common.entity.product.Product;

@Entity
@Table(name="cart_items")
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	private Integer quantiny;

	@Transient
	private Double shippingCost;
	
	public CartItem(Customer customer, Product product, Integer quantiny) {
		this.customer = customer;
		this.product = product;
		this.quantiny = quantiny;
	}

	public CartItem() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantiny() {
		return quantiny;
	}

	public void setQuantiny(Integer quantiny) {
		this.quantiny = quantiny;
	}
	
	@Transient
	public Double getSubtotal() {
		return product.getDiscountPrice() * quantiny;
	}

	@Transient
	public Double getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(Double shippingCost) {
		this.shippingCost = shippingCost;
	}
	
}
