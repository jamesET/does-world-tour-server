package com.justjames.beertour.user;

import java.util.Collection;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;

import com.justjames.beertour.AbstractShiroTest;
import com.justjames.beertour.BeerTourApplication;
import com.justjames.beertour.Brewception;
import com.justjames.beertour.security.Role;
import com.justjames.beertour.security.UserRealm;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BeerTourApplication.class)
public class UserSvcTest extends AbstractShiroTest {
	
	@Inject UserSvc userSvc;

	@Inject UserRealm userRealm;
	
	SecurityManager securityManager = null;
	
	@Before
	public void setUp() {
		if (securityManager == null) {
			securityManager =  new DefaultSecurityManager(userRealm);
			setSecurityManager(securityManager);
		}
	}
	
	@Test
	public void getOneUser() {
		User user = userSvc.getUser(1);
		Assert.assertNotNull(user);
	}
	
	@Test
	public void getAllUsers() {
		Collection<User> allUsers = userSvc.getAll();
		Assert.assertNotNull(allUsers);
	}
	
	@Test
	@Transactional
	public void testAddUser() {
		User u = new User();
		u.setEmail("testAddUser@just-james.com");
		u.setPassword("X");
		u.setName("James T");
		User savedUser = userSvc.addUser(u);
		Assert.assertNotNull(savedUser);
	}

	@Test(expected=Brewception.class)
	public void testAddUserWithoutEmail() {
		User u = new User();
		u.setEmail(null);
		u.setPassword("X");
		u.setName("James T");
		User savedUser = userSvc.addUser(u);
		Assert.assertNull(savedUser);
	}

	@Test(expected=Brewception.class)
	public void testAddUserWithoutPassword() {
		User u = new User();
		u.setEmail("testAddUserWithoutPassword@just-james.com");
		u.setPassword("");
		u.setName("James T");
		User savedUser = userSvc.addUser(u);
		Assert.assertNull(savedUser);
	}

	@Test(expected=Brewception.class)
	public void testAddUserWithoutName() {
		User u = new User();
		u.setEmail("testAddUserWithoutName@just-james.com");
		u.setPassword("X");
		u.setName(null);
		User savedUser = userSvc.addUser(u);
		Assert.assertNull(savedUser);
	}
	
	@Test
	public void testValidSignup() {
		User u = new User();
		u.setEmail("testValidSignup@just-james.com");
		u.setPassword("X");
		u.setName("Test Valid Signup");
		User newUser = userSvc.loginSignup(u);
		Assert.assertNotNull(newUser);
		Assert.assertTrue(newUser.getRole() == Role.CUSTOMER);
	}
	
	@Test(expected=Brewception.class)
	public void testDuplicateEmailSignupFails() {
		User u = new User();
		u.setEmail("james@just-james.com");
		u.setPassword("admin");
		u.setName("Duplicate Email Signup");
		userSvc.loginSignup(u);
	}
	
	@Test
	public void testLogoutUser() {
		// TODO expose logout action 
	}

	@After
	public void tearDownSubject() {
		clearSubject();
	}

}
