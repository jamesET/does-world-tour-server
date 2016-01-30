package com.justjames.beertour.security;

import org.apache.shiro.authc.AuthenticationToken;

public class SecurityToken implements AuthenticationToken {

	private static final long serialVersionUID = -8619329136025361325L;
	
	private String token;
	
	public SecurityToken(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

}
