package com.myshop.common.entity;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer extends AbstractAddress{
	
	
	
	@Column(nullable = false, unique = true, length = 45)
	private String email;
	
	@Column(nullable = false, length = 64)
	private String password;
	
	
	
	@Column(name="verification_code", length = 64)
	private String verificationCode;
	
	
	private boolean enabled;
	
	@Column(name="created_time")
	private Date createdTime;
	
	@ManyToOne
	@JoinColumn(name="province_id")
	private Province province;
	
	

	@Enumerated(EnumType.STRING)
	@Column(name="authentication_type", length = 10)
	private AuthenticationType authenticationType;
	
	@Column(name="reset_password_token", length = 30)
	private String resetPasswordToken;
	
	
	public Customer(Integer id) {
		this.id = id;
	}

	public Customer() {
	}

	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	
	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	
	
	
	
	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	@Transient
	public String getFullName() {
		return lastName+" "+ firstName;
	}
	
	@Transient
	public boolean isEmptyAddress() {
		if(addressLine.isEmpty()||addressLine == null) {
			return true;
		}else if (district.isEmpty() ||district==null) {
			return true;
		}else if (province==null) {
			return true;
		}else if (phoneNumber.isEmpty()||phoneNumber==null) {
			return true;
		}
		return false;
	}
	@Transient
	public String getAddress() {
		String address ="";
		if (lastName != null && !lastName.isEmpty()) address += " " + lastName;
		
		if (firstName != null && !firstName.isEmpty()) address += " " + firstName;
		
		address+= ", địa chỉ: ";
		
		if (!addressLine.isEmpty()) address +=   addressLine;
		
		
		
		if (district!=null && !district.isEmpty()) address += ", " + district;
		
		if (province != null ) address += ", " + province.getName();
		
		
				
		if (!phoneNumber.isEmpty()) address += ". Số điện thoại: " + phoneNumber;
		
		return address;
	}
}
