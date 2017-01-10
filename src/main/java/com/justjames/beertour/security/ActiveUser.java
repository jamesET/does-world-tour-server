package com.justjames.beertour.security;

import java.io.Serializable;

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
		this.token =  user.getToken();
		this.setUserTO(new UserTO(user));
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
		return String.format("ActiveUser [userId=%s, email=%s, token=%s]", userId, email, token);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActiveUser other = (ActiveUser) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	

}
