package com.justjames.beertour.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED) 
public class NotAuthenticatedException extends RuntimeException {

	private static final long serialVersionUID = -3133735175330660595L;

	public NotAuthenticatedException(String msg) {
		super(msg);
	}
}
