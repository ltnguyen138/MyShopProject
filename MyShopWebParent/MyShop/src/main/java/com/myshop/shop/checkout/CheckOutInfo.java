package com.myshop.shop.checkout;

import java.util.Calendar;
import java.util.Date;

public class CheckOutInfo {
	private Double productCostTotal;
	private Double productPriceTotal;
	private Double shippingCostTotal;
	private Double paymentTotal;
	private int deliverDays;
	private Date deliverDate;
	
	
	public Double getProductCostTotal() {
		return productCostTotal;
	}
	public void setProductCostTotal(Double productCostTotal) {
		this.productCostTotal = productCostTotal;
	}
	public Double getProductPriceTotal() {
		return productPriceTotal;
	}
	public void setProductPriceTotal(Double productPriceTotal) {
		this.productPriceTotal = productPriceTotal;
	}
	public Double getShippingCostTotal() {
		return shippingCostTotal;
	}
	public void setShippingCostTotal(Double shippingCostTotal) {
		this.shippingCostTotal = shippingCostTotal;
	}
	public Double getPaymentTotal() {
		return paymentTotal;
	}
	public void setPaymentTotal(Double paymentTotal) {
		this.paymentTotal = paymentTotal;
	}
	public int getDeliverDays() {
		return deliverDays;
	}
	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}
	public Date getDeliverDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.DATE, deliverDays);
		return calendar.getTime();
	}
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}
		
}
