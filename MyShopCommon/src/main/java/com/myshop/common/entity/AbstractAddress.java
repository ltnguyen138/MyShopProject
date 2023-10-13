package com.myshop.common.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractAddress  extends AbstractId{
	@Column(name="first_name", nullable = false, length = 64)
	protected String firstName;
	
	@Column(name="last_name", length = 64)
	protected String lastName;
	
	@Column(name="phone_number", length = 15)
	protected String phoneNumber;
	
	@Column(  length = 128)
	protected String addressLine;
	
	@Column(  length = 45)
	protected String district;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
}
