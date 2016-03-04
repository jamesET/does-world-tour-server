package com.justjames.beertour.security;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

@Component
public class LoginSvc {
	
	private Log log = LogFactory.getLog(LoginSvc.class);
	
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
		
		currentUser.login(up);
		activeUser = (ActiveUser) currentUser.getPrincipal();
		log.info("Login: " + activeUser);
		return activeUser;
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
