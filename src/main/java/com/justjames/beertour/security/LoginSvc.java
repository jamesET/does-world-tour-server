package com.justjames.beertour.security;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justjames.beertour.InvalidPostDataException;
import com.justjames.beertour.mail.SendEmail;
import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserSvc;

@Component
public class LoginSvc {
	
	private static final String resetMailSubject = 
			"Your Beer Tour password";
	
	private static final String resetMailBodyFmt = 
		"%s,\n" +
		"\n" + 
		"Your password is '%s'.\n" + 
		"\n" + 
		"   -Beer Tour";
	
	private Log log = LogFactory.getLog(LoginSvc.class);
	
	@Autowired private TokenRealm tokenRealm;
	
	@Autowired private UserSvc userSvc;
	
	@Autowired private SendEmail sender; 
	
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

	/**
	 * @param email Email address of user wanting password
	 */
	public void sendPassword(String email) {
		User u = userSvc.findByEmail(email);
		
		if (u == null) {
			throw new InvalidPostDataException("There is no matching user for " + email);
		}
		
		String resetMailBody = String.format(resetMailBodyFmt, u.getDisplayName(), u.getPassword());
		
		sender.sendMail(u.getEmail(), resetMailSubject, resetMailBody);
	}

}
