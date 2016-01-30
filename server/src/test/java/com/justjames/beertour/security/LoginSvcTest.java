package com.justjames.beertour.security;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.justjames.beertour.BeerTourApplication;
import com.justjames.beertour.Brewception;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BeerTourApplication.class)
public class LoginSvcTest {
	
	@Inject LoginSvc loginSvc;
	
	@Test(expected = Brewception.class)
	public void loginInvalidUserId() {
		loginSvc.login("brent@just-james.com", "admin");
	}

	@Test(expected = Brewception.class)
	public void loginInvalidPassword() {
		loginSvc.login("james@just-james.com", "invalidpassword");
	}

	@Test
	public void loginSuccess() {
		ActiveUser validUser = loginSvc.login("james@just-james.com", "admin");
		Assert.assertNotNull(validUser);
	}
	


}
