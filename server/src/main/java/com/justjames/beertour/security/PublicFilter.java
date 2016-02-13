package com.justjames.beertour.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;

public class PublicFilter extends PassThruAuthenticationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		// This filter allows everything, it should be used to map resources that are public 
		return true;
	}


}
