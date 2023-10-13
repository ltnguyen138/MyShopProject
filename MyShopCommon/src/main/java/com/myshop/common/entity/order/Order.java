package com.myshop.common.entity.order;

import java.beans.Transient;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.myshop.common.entity.AbstractAddress;
import com.myshop.common.entity.Customer;

@Entity
@Table(name="orders")
public class Order extends AbstractAddress{
	
	
	@Column( nullable = false, length = 45)
	private String province;
	
	@Column(name="order_time")
	private Date orderTime ;
	
	@Column(name="shipping_cost")
	private Double shippingCost ;
	@Column(name="product_cost")
	private Double productCost;
	private Double subtotal ; 
	private Double tax;
	private Double total ; 
	
	@Column(name="deliver_days")
	private Integer deliverDays;	
	@Column(name="deliver_date")
	private Date deliverDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name="payment_method")
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	@Column(name="order_status")
	private OrderStatus orderStatus;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<>();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)	
	@OrderBy("updatedTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();
	
	public String getProvince() {
		return province;
	}
	
	

	public Order() {

	}



	public Order(Integer id,Date orderTime, Double productCost, Double subtotal, Double total) {
		this.id=id;
		this.orderTime = orderTime;
		this.productCost = productCost;
		this.subtotal = subtotal;
		this.total = total;
	}



	public void setProvince(String province) {
		this.province = province;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
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

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(Integer deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	
	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}



	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}



	public void copyAndressFromCustomer() {
		setLastName(customer.getLastName());
		setFirstName(customer.getFirstName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine(customer.getAddressLine());
		setDistrict(customer.getDistrict());
		setProvince(customer.getProvince().getName());
	}
	
	@Transient
	public String getFullName() {
		return lastName+" "+ firstName;
	}
	
	@Transient
	public String getDestination() {
		return  district+ ", "+province;
	}
	
	@Transient
	public String getDeliverDateOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter.format(this.deliverDate);
	}	
	
	public void setDeliverDateOnForm(String dateString) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
 		
		try {
			this.deliverDate = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		} 		
	}
	
	@Transient
	public String getRecipientName() {
		String name = lastName;
		if (firstName != null && !firstName.isEmpty()) name += " " + firstName;
		return name;
	}
	@Transient
	public String getAddress() {
		String address ="";
		
		
		if (!addressLine.isEmpty()) address +=   addressLine;
		
		
		
		if (district!=null && !district.isEmpty()) address += ", " + district;
		
		if (province != null ) address += ", " + province;
		
		
				
		
		
		return address;
	}
	
	@Transient
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}
	
	@Transient
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}
	
	@Transient
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}
	
	@Transient
	public boolean isDelivered() {
		return hasStatus(OrderStatus.DELIVERED);
	}

	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RETURN_REQUESTED);
	}	
	
	@Transient
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}	
	
	public boolean hasStatus(OrderStatus status) {
		for (OrderTrack aTrack : orderTracks) {
			if (aTrack.getStatus().equals(status)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Transient
	public String getProductNames() {
		String productNames = "";
		
		productNames = "<ul>";
		
		for (OrderDetail detail : orderDetails) {
			productNames += "<li>" + detail.getProduct().getShortName() + "</li>";			
		}
		
		productNames += "</ul>";
		
		return productNames;
	}	
}
