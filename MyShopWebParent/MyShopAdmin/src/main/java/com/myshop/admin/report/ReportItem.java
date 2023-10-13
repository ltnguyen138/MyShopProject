package com.myshop.admin.report;

import java.util.Objects;

public class ReportItem {
	private String identifier;
	private double grossSales;
	private double netSales;
	private int ordersCount;
	private int productsCount;
	
	public ReportItem() {
	}
	
	public ReportItem(String identifier) {
		this.identifier = identifier;
	}
	
	public ReportItem(String identifier, float grossSales, float netSales) {
		this.identifier = identifier;
		this.grossSales = grossSales;
		this.netSales = netSales;
	}

	public String getIdentifier() {
		return identifier;
	}

	
	
	public ReportItem(String identifier, double grossSales, double netSales, int productsCount) {

		this.identifier = identifier;
		this.grossSales = grossSales;
		this.netSales = netSales;
		this.productsCount = productsCount;
	}

	public int getProductsCount() {
		return productsCount;
	}

	public void setProductsCount(int productsCount) {
		this.productsCount = productsCount;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public double getGrossSales() {
		return grossSales;
	}

	public void setGrossSales(float grossSales) {
		this.grossSales = grossSales;
	}

	public double getNetSales() {
		return netSales;
	}

	public void setNetSales(float netSales) {
		this.netSales = netSales;
	}

	public int getOrdersCount() {
		return ordersCount;
	}

	public void setOrdersCount(int ordersCount) {
		this.ordersCount = ordersCount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportItem other = (ReportItem) obj;
		return Objects.equals(identifier, other.identifier);
	}

	public void addGrossSales(Double total) {
		this.grossSales+=total;
		
	}

	public void addNetSales(double amount) {
		this.netSales += amount;
		
	}

	public void increaseOrdersCount() {
		this.ordersCount++;
		
	}
	
	public void increaseProductsCount(int count) {
		this.productsCount += count;
	}
}
