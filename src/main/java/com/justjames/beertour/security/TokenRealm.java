package com.justjames.beertour.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserSvc;

@Component
public class TokenRealm extends AuthorizingRealm {
	
	@Autowired
	UserSvc userSvc;
	
	private Log log = LogFactory.getLog(TokenRealm.class);
	
	private LoadingCache<String,ActiveUser> tokenCache = 
			CacheBuilder.newBuilder()
				.maximumSize(200)
				.expireAfterWrite(30,TimeUnit.MINUTES)
				.build(
					new CacheLoader<String,ActiveUser> () {

						@Override
						public ActiveUser load(String token) throws Exception {
							User u = userSvc.findByToken(token);
							return new ActiveUser(u);
						}
					}
						
				);
	
	/**
	 * Save new token for the user 
	 * @param user
	 */
	public ActiveUser updateActiveUser(ActiveUser user) {
		log.debug("Saving token for " + user );
		User u = userSvc.getUser(user.getUserId());
		userSvc.setNewToken(u);
		u =  userSvc.getUser(user.getUserId());
		return new ActiveUser(u);
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
		String strToken = (String) secToken.getCredentials();
		
		log.debug("Looking for token: '" + strToken + "'");
		ActiveUser activeUser;
		try {
			activeUser = tokenCache.get(strToken);
		} catch (ExecutionException e) {
			log.warn(e.getMessage());
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
