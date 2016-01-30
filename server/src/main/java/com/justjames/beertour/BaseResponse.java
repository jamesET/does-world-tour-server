package com.justjames.beertour;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BaseResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String errorMsg;


	@XmlElement
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	

}
