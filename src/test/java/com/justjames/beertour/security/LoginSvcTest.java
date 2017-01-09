package com.justjames.beertour.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.justjames.beertour.shiro.AbstractShiroTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LoginSvcTest extends AbstractShiroTest {
	
	@Autowired LoginSvc loginSvc;
	
	@Autowired UserRealm userRealm;
	
	SecurityManager securityManager = null;
	
	@Before
	public void setUp() {
		if (securityManager == null) {
			securityManager =  new DefaultSecurityManager(userRealm);
			setSecurityManager(securityManager);
		}
	}
	
	@After
	public void tearDownSubject() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		clearSubject();
	}

	
	@Test(expected = NotAuthenticatedException.class)
	public void loginInvalidUserId() {
		loginSvc.login("brent@just-james.com", "admin");
	}

	@Test(expected = NotAuthenticatedException.class)
	public void loginInvalidPassword() {
		loginSvc.login("james@just-james.com", "invalidpassword");
	}

	@Test
	public void loginSuccess() {
		ActiveUser validUser = loginSvc.login("james@just-james.com", "admin");
		Assert.assertNotNull(validUser);
	}
	


}
