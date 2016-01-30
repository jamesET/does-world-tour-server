package com.justjames.beertour.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserSvc;

@Named
public class TokenRealm extends AuthorizingRealm {
	
	@Inject UserSvc userSvc;
	
	private Log log = LogFactory.getLog(TokenRealm.class);
	
	private Cache<String,ActiveUser> tokenCache = 
			CacheBuilder.newBuilder()
				.maximumSize(100)
				.expireAfterAccess(60,TimeUnit.MINUTES) // tokens will expire after not being used for 60 minutes
				.build();
	
	/**
	 * Add new token to cache
	 * @param user
	 */
	public void addActiveUser(ActiveUser user) {
		log.debug("Adding token to cache" + user);
		tokenCache.put(user.getToken(), user);
		log.debug("Token cache entries=" + tokenCache.size());
	}
	
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
		
		SecurityToken secToken = (SecurityToken) token;
		
		log.debug("Looking for token: '" + secToken.getCredentials() + "'");
		ActiveUser activeUser = tokenCache.getIfPresent(secToken.getCredentials());
		
		if (activeUser == null) {
			throw new AuthenticationException(String.format("Token not valid %s", secToken.getCredentials()));
		}

		log.debug("Token found " + activeUser);
	
		return new SimpleAuthenticationInfo(activeUser, secToken.getCredentials(), getName());
	}
	
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof SecurityToken;
	}


}
