package com.myshop.shop.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class customerOauth2User implements OAuth2User{

	private OAuth2User oAuth2User;
	private String fullName;
	
	public customerOauth2User(OAuth2User user) {
		this.oAuth2User = user;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oAuth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oAuth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oAuth2User.getAttribute("name");
	}
	public String getFullname() {
		return fullName!=null ? fullName : oAuth2User.getAttribute("name");
	}
	public String getEmail() {
		return oAuth2User.getAttribute("email");
	}

	

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
}
