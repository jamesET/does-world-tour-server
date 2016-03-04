package com.justjames.beertour.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

public class PublicFilter extends PassThruAuthenticationFilter {
	
	private Log log = LogFactory.getLog(PublicFilter.class);

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		// This filter allows everything, it should be used to map resources that are public 
		
		HttpServletRequest httpReq = null;
		if (request instanceof HttpServletRequest) {
			httpReq = (HttpServletRequest) request;
			log.debug("Context Path: " + httpReq.getContextPath());
			log.debug("Request URL: " + httpReq.getRequestURL());
			log.debug("Path Info: " + httpReq.getPathInfo());
			log.debug("PathWithinApplication: " + WebUtils.getPathWithinApplication(httpReq));
		}

		return true;
	}


}
