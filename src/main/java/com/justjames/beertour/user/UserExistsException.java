package com.justjames.beertour.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {

	private static final long serialVersionUID = 1926407871399884933L;

	public UserExistsException(String msg) {
		super(msg);
	}


}
