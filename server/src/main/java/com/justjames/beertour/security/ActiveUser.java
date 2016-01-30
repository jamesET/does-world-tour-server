package com.justjames.beertour.security;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;

public class ActiveUser implements Serializable {
	
	private static final long serialVersionUID = -6754723511884185662L;

	private Integer userId;
	private String email;
	private String token;
	
	public ActiveUser(Integer userId,String email) {
		this.userId = userId;
		this.email = email;
		this.token = RandomStringUtils.randomAlphanumeric(32);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return String.format("ActiveUser [userId=%s, token=%s]", userId, token);
	}

}
