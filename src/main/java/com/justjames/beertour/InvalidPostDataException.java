package com.justjames.beertour;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT) 
public class InvalidPostDataException extends RuntimeException {
	
	private static final long serialVersionUID = -49088353838142626L;

	public InvalidPostDataException(String msg) {
		super(msg);
	}

}
