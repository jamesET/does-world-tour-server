package com.justjames.beertour.security;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginSvc {
	
	private Log log = LogFactory.getLog(LoginSvc.class);
	
	@Autowired
	TokenRealm tokenRealm;
	
	/**
	 * Return the matching user entity if the password matches
	 * @param email
	 * @param password
	 * @return authenticated user or null 
	 */
	public ActiveUser login(String username,String password) {
		ActiveUser activeUser = null;
		
		UsernamePasswordToken up = new UsernamePasswordToken(username,password);
		up.setRememberMe(true);
		Subject currentUser = SecurityUtils.getSubject();
		
		try {
			currentUser.login(up);
			activeUser = (ActiveUser) currentUser.getPrincipal();
			tokenRealm.addActiveUser(activeUser);
			log.info("Login: " + activeUser);
			return activeUser;
		} catch (AuthenticationException ae) {
			log.info(String.format("Failed login attempt for '%s'",username));
			throw new NotAuthenticatedException("Invalid login attempt");
		}
	}
	
	public void logout() {
		Subject currentUser = SecurityUtils.getSubject();
		ActiveUser activeUser = (ActiveUser) currentUser.getPrincipal();
		if (activeUser != null) {
			log.info("Logout: " + activeUser);
			currentUser.logout();
		}
	}

}
