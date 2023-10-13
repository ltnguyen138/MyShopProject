package com.myshop.common.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.myshop.common.entity.Category;
import com.myshop.common.entity.product.Product;

@Entity
@Table(name="order_details")
public class OrderDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private int quantity;
	
	@Column(name="shipping_cost")
	private Double shippingCost ;
	
	@Column(name="product_cost")
	private Double productCost;
	
	@Column(name="unit_price")
	private Double unitPrice;
	
	private Double subtotal ; 
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;

	public OrderDetail() {

	}
	
	public OrderDetail( String categoryName,int quantity, Double productCost, Double shippingCost, Double unitPrice, Double subtotal) {
		this.product = new Product();
		this.product.setCategory(new Category(categoryName));
		this.quantity = quantity;
		this.shippingCost = shippingCost;
		this.productCost = productCost;
		this.unitPrice = unitPrice;
		this.subtotal = subtotal;
	}
	public OrderDetail(int quantity, String productName, Double productCost, Double shippingCost, Double unitPrice, Double subtotal) {
		this.product = new Product(productName);
		
		this.quantity = quantity;
		this.shippingCost = shippingCost;
		this.productCost = productCost;
		this.unitPrice = unitPrice;
		this.subtotal = subtotal;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(Double shippingCost) {
		this.shippingCost = shippingCost;
	}

	public Double getProductCost() {
		return productCost;
	}

	public void setProductCost(Double productCost) {
		this.productCost = productCost;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
}
