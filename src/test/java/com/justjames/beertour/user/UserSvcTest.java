package com.justjames.beertour.user;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;

import com.justjames.beertour.shiro.AbstractShiroTest;
import com.justjames.beertour.InvalidPostDataException;
import com.justjames.beertour.security.LoginSvc;
import com.justjames.beertour.security.NotAuthorizedException;
import com.justjames.beertour.security.Role;
import com.justjames.beertour.security.UserRealm;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserSvcTest extends AbstractShiroTest {
	
	@Autowired UserSvc userSvc;

	@Autowired UserRealm userRealm;
	
	@Autowired LoginSvc loginSvc;
	
	SecurityManager securityManager = null;
	
	@Before
	public void setUp() {
		if (securityManager == null) {
			securityManager =  new DefaultSecurityManager(userRealm);
			setSecurityManager(securityManager);
		}
	}
	
	/**
	 * Utility method to help test with different permissions
	 * @param name
	 * @param role
	 * @return
	 */
	public User addTestUser(String name,Role role) {
		User user = new User();
		user.setEmail( String.format("%s@just-james.com", name));
		user.setName(name);
		user.setPassword("1234");
		user.setRole(role);
		user = userSvc.addUser(user);
		loginSvc.login(user.getEmail(), user.getPassword());
		return user;
	}
	
	
	@Test
	public void getOneUser() {
		User user = userSvc.getUser(1);
		Assert.assertNotNull(user);
	}
	
	@Test
	@Transactional
	public void getAllUsersAsAdmin() {
		addTestUser("getAllUsersAsAdmin",Role.ADMIN);
		Collection<User> allUsers = userSvc.getAll();
		Assert.assertNotNull(allUsers);
	}

	@Test(expected=NotAuthorizedException.class)
	@Transactional
	public void getAllUsersAsOther() {
		addTestUser("getAllUsersAsOther",Role.CUSTOMER);
		userSvc.getAll();
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

	@Test(expected=InvalidPostDataException.class)
	public void testAddUserWithoutEmail() {
		User u = new User();
		u.setEmail(null);
		u.setPassword("X");
		u.setName("James T");
		User savedUser = userSvc.addUser(u);
		Assert.assertNull(savedUser);
	}

	@Test(expected=InvalidPostDataException.class)
	public void testAddUserWithoutPassword() {
		User u = new User();
		u.setEmail("testAddUserWithoutPassword@just-james.com");
		u.setPassword("");
		u.setName("James T");
		User savedUser = userSvc.addUser(u);
		Assert.assertNull(savedUser);
	}

	@Test(expected=InvalidPostDataException.class)
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
	
	@Test(expected=UserExistsException.class)
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
	
	@Test
	@Transactional
	public void testUpdateUserAsAdmin() {
		addTestUser("testUpdateUserAsAdmin",Role.ADMIN);
		User owner = userSvc.findByEmail("james@just-james.com");
		owner.setNumListsCompleted(999);
		User updatedUser = userSvc.selfUpdate(owner);
		Assert.assertTrue(updatedUser.getNumListsCompleted()==999);
	}

	@Test
	@Transactional
	public void testUpdateUserAsOwner() {
		loginSvc.login("james@just-james.com", "admin");
		User owner = userSvc.findByEmail("james@just-james.com");
		owner.setNumListsCompleted(997);
		User updatedUser = userSvc.selfUpdate(owner);
		Assert.assertTrue(updatedUser.getNumListsCompleted()==997);
	}

	@Test(expected=NotAuthorizedException.class)
	@Transactional
	public void testUpdateUserAsOther() {
		addTestUser("testUpdateUserAsOther",Role.CUSTOMER);
		User owner = userSvc.findByEmail("james@just-james.com");
		userSvc.selfUpdate(owner);
	}

	@Test(expected=InvalidPostDataException.class)
	@Transactional
	public void testUpdateInvalidUser() {
		addTestUser("testUpdateInvalidUser",Role.ADMIN);
		User user = new User(); 
		user.setId(-1);
		user.setEmail("nobody@nowhere.com");
		userSvc.selfUpdate(user);
	}
	
	@Test
	@Transactional
	public void testAdminEditAsAdmin() {
		addTestUser("testAdminEditAsAdmin",Role.ADMIN);
		User owner = userSvc.findByEmail("james@just-james.com");
		UserEditTO userEdit = new UserEditTO(owner);
		userEdit.setRole(Role.CUSTOMER);
		User updatedUser = userSvc.adminEdit(owner.getId(),userEdit);
		Assert.assertTrue(updatedUser.getRole()==Role.CUSTOMER);
	}


	@After
	public void tearDownSubject() {
		clearSubject();
	}

}
