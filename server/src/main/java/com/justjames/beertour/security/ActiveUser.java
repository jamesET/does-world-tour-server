package com.justjames.beertour.security;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;

import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserTO;

public class ActiveUser implements Serializable {
	
	private static final long serialVersionUID = -6754723511884185662L;

	private Integer userId;
	private String email;
	private String token;
	private UserTO userTO;
	
	public ActiveUser(User user) {
		this.userId = user.getId();
		this.email = user.getEmail();
		this.setUserTO(new UserTO(user));
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

	public UserTO getUserTO() {
		return userTO;
	}

	public void setUserTO(UserTO userTO) {
		this.userTO = userTO;
	}

	@Override
	public String toString() {
		return String.format("ActiveUser [userId=%s, token=%s]", userId, token);
	}

}
