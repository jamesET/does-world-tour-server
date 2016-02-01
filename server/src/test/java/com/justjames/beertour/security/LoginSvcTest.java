package com.justjames.beertour.security;

import javax.inject.Inject;

import org.apache.shiro.authc.AuthenticationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.justjames.beertour.BeerTourApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BeerTourApplication.class)
public class LoginSvcTest {
	
	@Inject LoginSvc loginSvc;
	
	@Test(expected = AuthenticationException.class)
	public void loginInvalidUserId() {
		loginSvc.login("brent@just-james.com", "admin");
	}

	@Test(expected = AuthenticationException.class)
	public void loginInvalidPassword() {
		loginSvc.login("james@just-james.com", "invalidpassword");
	}

	@Test
	public void loginSuccess() {
		ActiveUser validUser = loginSvc.login("james@just-james.com", "admin");
		Assert.assertNotNull(validUser);
	}
	


}
