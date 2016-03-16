package com.justjames.beertour;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED) 
public class NotAuthorizedException extends RuntimeException {

	private static final long serialVersionUID = -3133735175330660595L;

	public NotAuthorizedException(String msg) {
		super(msg);
	}
}
