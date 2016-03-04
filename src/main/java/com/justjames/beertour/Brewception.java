package com.justjames.beertour;

//TODO source messages from a resource bundle 
public class Brewception extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public Brewception() { }
	
	public Brewception(String msgKey) {
		
		super(msgKey);
	}
	
	public Brewception(String msgKey,Exception ex) { 
		super(msgKey,ex);
	}
	

}
