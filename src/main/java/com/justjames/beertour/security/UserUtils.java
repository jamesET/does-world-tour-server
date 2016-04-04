package com.justjames.beertour.security;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.justjames.beertour.Brewception;

public abstract class UserUtils {
	
	public static boolean isAdmin() {
		Subject subject = SecurityUtils.getSubject();
		if (subject == null) {
			throw new NotAuthenticatedException("User is not logged in!");
		}
		return subject.hasRole(Role.ADMIN.toString());
	}
	
	
	/**
	 * @return User object for the current logged-in user
	 */
	public static ActiveUser getActiveUser() {
		Subject subject = SecurityUtils.getSubject();
		if (!subject.isAuthenticated()) {
			throw new Brewception("User is not logged in!");
		} 

		Collection<ActiveUser> users = subject.getPrincipals().byType(ActiveUser.class);
		ActiveUser activeUser = users.iterator().next(); 
		return activeUser;
	}


}
