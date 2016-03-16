package com.justjames.beertour;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR) 
public class ResourceException extends RuntimeException {
	
	private static final long serialVersionUID = -2431755133212852894L;

	public ResourceException(String msg) {
		super(msg);
	}

}
