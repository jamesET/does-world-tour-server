package com.justjames.beertour.mail;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE) 
public class MailException extends RuntimeException {
	
	private static final long serialVersionUID = -3709540002096581254L;

	public MailException(String msg) {
		super(msg);
	}

}
