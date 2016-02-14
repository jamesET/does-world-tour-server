package com.justjames.beertour.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

public class SecurityFilter extends PassThruAuthenticationFilter {

	private Log log = LogFactory.getLog(SecurityFilter.class);

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		
		WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
		return false;
		//return super.onAccessDenied(request, response);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {

		boolean allowed = false;

		HttpServletRequest httpReq = null;
		if (request instanceof HttpServletRequest) {
			httpReq = (HttpServletRequest) request;
			log.debug("Context Path: " + httpReq.getContextPath());
			log.debug("Request URL: " + httpReq.getRequestURL());
			log.debug("Path Info: " + httpReq.getPathInfo());
			log.debug("PathWithinApplication: " + WebUtils.getPathWithinApplication(httpReq));
		}
		
		// OPTIONS calls are always allowed
		String method = httpReq.getMethod();
		if (StringUtils.equalsIgnoreCase(method, "OPTIONS")) {
			return true;
		}

		String tokenValue = httpReq.getHeader("x-security-token");
		log.debug("x-security-token=" + tokenValue);

		SecurityToken token = new SecurityToken(tokenValue);

		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			allowed = true;
			ActiveUser au = (ActiveUser) subject.getPrincipal();
			log.debug("Token passes filter: user=" + au.getEmail());
		} catch (AuthenticationException a) {
			log.warn("Filter rejects token: " + tokenValue + a.getMessage());
			allowed = false;
		}

		return allowed;
	}
	
	
	

}
