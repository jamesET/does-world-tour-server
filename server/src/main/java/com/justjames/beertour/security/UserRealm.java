package com.justjames.beertour.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserSvc;

@Named
public class UserRealm extends AuthorizingRealm {
	
	private Log log = LogFactory.getLog(UserRealm.class);
	
	@Inject
	UserSvc userSvc;
	
	@Inject
	TokenRealm tokenRealm;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
	
		Set<String>			roles			= new HashSet<String>();
		Set<String>			permissions		= new HashSet<String>(); 
		Collection<ActiveUser>	principalsList	= principals.byType(ActiveUser.class);
		
		if (principalsList.isEmpty()) {
			throw new AuthorizationException("Empty principals list!");
		}

		for (ActiveUser userPrincipal : principalsList) {
			User user = userSvc.getUser(userPrincipal.getUserId());
			roles.add(user.getRole().toString());
			permissions.addAll(user.getRole().getPermissions());
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
		info.setRoles(roles); //fill in roles 
		info.setStringPermissions(permissions); 
		
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		
		User user = null;
		user = userSvc.findByEmail(upToken.getUsername());
		
		if (user == null) {
			throw new AuthenticationException(
				String.format("Login name [%s] not found!", upToken.getUsername()));
		}
	
		ActiveUser activeUser = new ActiveUser(user.getId(),user.getEmail()); 
		tokenRealm.addActiveUser(activeUser);
		log.debug("User found " + activeUser);
		
		return new SimpleAuthenticationInfo(activeUser, user.getPassword(), getName());
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken; 
	}
	
	

}
