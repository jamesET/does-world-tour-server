package com.justjames.beertour.beerlist;

import java.util.Collection;

import javax.transaction.Transactional;

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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.justjames.beertour.shiro.AbstractShiroTest;
import com.justjames.beertour.BeerTourApplication;
import com.justjames.beertour.Brewception;
import com.justjames.beertour.NotAuthorizedException;
import com.justjames.beertour.security.ActiveUser;
import com.justjames.beertour.security.LoginSvc;
import com.justjames.beertour.security.Role;
import com.justjames.beertour.security.UserRealm;
import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserSvc;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BeerTourApplication.class)
public class BeerListSvcTest extends AbstractShiroTest {
	
	@Autowired BeerListSvc listSvc;
	
	@Autowired UserSvc userSvc;

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

	public User addTestUser(String name) {
		User user = new User();
		user.setEmail( String.format("%s@just-james.com", name));
		user.setName(name);
		user.setPassword("1234");
		user.setRole(Role.CUSTOMER);
		user = userSvc.addUser(user);
		loginSvc.login(user.getEmail(), user.getPassword());
		return user;
	}
	
	@Test
	@Transactional
	public void getOneList() {
		User testUser = addTestUser("getOneList");
		ActiveUser user = new ActiveUser(testUser);
		BeerList list = listSvc.startList(user);
		BeerList foundList = listSvc.getBeerList(list.getId());
		Assert.assertTrue(list == foundList);
	}
	
	@Test
	@Transactional()
	public void getAllLists() {
		User testUser1 = addTestUser("getOneList1");
		User testUser2 = addTestUser("getOneList2");
		ActiveUser user1 = new ActiveUser(testUser1);
		ActiveUser user2 = new ActiveUser(testUser2);
		listSvc.startList(user1);
		listSvc.startList(user2);
		Collection<BeerList> allLists = listSvc.findAll();
		Assert.assertTrue(allLists.size() > 0);
	}
	
	@Test
	@Transactional
	public void findCustomerBeerList() {
		User testUser = addTestUser("findCustomerBeerList");
		ActiveUser user = new ActiveUser(testUser);
		listSvc.startList(user);
		BeerList myBeerList = listSvc.findActiveBeerListByEmail(user.getEmail());
		Assert.assertNotNull(myBeerList);
		Assert.assertNotNull(myBeerList.getId());
	}
	
	@Test
	@Transactional
	public void testStartList() {
		User testUser = addTestUser("testStartList");
		ActiveUser user = new ActiveUser(testUser);
		BeerList newList = listSvc.startList(user);
		Assert.assertNotNull(newList);
	}
	
	/* A user shouldn't have more than one list at a time
	 * 
	 */
	@Test(expected=Brewception.class)
	@Transactional
	public void startDuplicateEmailListFails() {
		User testUser = addTestUser("oneListRule");
		ActiveUser user = new ActiveUser(testUser);
		listSvc.startList(user);
		listSvc.startList(user);
	}
	
	@Test
	@Transactional
	public void getMyListLoggedIn() {
		addTestUser("getMyListLoggedIn");
		BeerList myList = listSvc.getMyBeerList();
		Assert.assertNotNull(myList);
		Assert.assertNotNull(myList.getBeerOnList());
		Assert.assertTrue(myList.getBeerOnList().size() > 0);
	}
	
	@Test(expected=Brewception.class)
	public void getMyListNotLoggedIn() {
		listSvc.getMyBeerList();
	}
	
	@Test
	@Transactional
	public void validAdminCrossOff() {
		loginSvc.login("james@just-james.com", "admin");
		BeerList myList = listSvc.getMyBeerList();
		Integer beforeRemaining = myList.getNumberRemaining();
		Integer beforeOrdered = myList.getNumberOrderedOnList();
		Integer beforeCompleted = myList.getNumberCompletedOnList();

		// Cross off first beer
		BeerOnList beer = myList.getBeerOnList().iterator().next();
		listSvc.crossOffMyBeer(myList.getId(),beer.getId());

		myList = listSvc.getMyBeerList();
		Integer afterRemaining = myList.getNumberRemaining();
		Integer afterOrdered = myList.getNumberOrderedOnList();
		Integer afterCompleted = myList.getNumberCompletedOnList();
		Assert.assertTrue(afterRemaining.equals(beforeRemaining));
		Assert.assertTrue(afterOrdered.equals(beforeOrdered+1));
		Assert.assertTrue(afterCompleted.equals(beforeCompleted));
	}

	@Test
	@Transactional
	public void validCustomerCrossOff() {
		
		User testCustomer = addTestUser("validCustomerCrossOff");
		ActiveUser customer = new ActiveUser(testCustomer);
		listSvc.startList(customer);

		loginSvc.login(testCustomer.getEmail(), testCustomer.getPassword());
		BeerList myList = listSvc.getMyBeerList();

		Integer beforeRemaining = myList.getNumberRemaining();
		Integer beforeOrdered = myList.getNumberOrderedOnList();
		Integer beforeCompleted = myList.getNumberCompletedOnList();

		// Cross off the first beer
		BeerOnList beer = myList.getBeerOnList().iterator().next();
		listSvc.crossOffMyBeer(myList.getId(),beer.getId());

		myList = listSvc.getMyBeerList();
		Integer afterRemaining = myList.getNumberRemaining();
		Integer afterOrdered = myList.getNumberOrderedOnList();
		Integer afterCompleted = myList.getNumberCompletedOnList();
		Assert.assertTrue(afterRemaining.equals(beforeRemaining));
		Assert.assertTrue(afterOrdered.equals(beforeOrdered+1));
		Assert.assertTrue(afterCompleted.equals(beforeCompleted));
	}
	
	
	@Test(expected=Brewception.class)
	@Transactional
	public void onlyAdminCanCrossOffOther() {
		// Only a user with the admin role should be able to work on someone else's list.
		
		User testCustomer = addTestUser("testStartList1");
		ActiveUser customer = new ActiveUser(testCustomer);
		BeerList customersList = listSvc.startList(customer);

		User hacker = addTestUser("testStartList2");
		loginSvc.login(hacker.getEmail(), hacker.getPassword());

		BeerOnList beer = customersList.getBeerOnList().iterator().next();
		listSvc.crossOffMyBeer(customersList.getId(),beer.getId());
	}
	
	@Test
	@Transactional
	public void addminCrossesOffOther() {
		User testCustomer = addTestUser("authCheckPassesOnCrossoff");
		ActiveUser customer = new ActiveUser(testCustomer);
		BeerList customersList = listSvc.startList(customer);

		loginSvc.login("james@just-james.com","admin");

		BeerOnList beer = customersList.getBeerOnList().iterator().next();
		Integer beforeNumOrdered = customersList.getNumberOrderedOnList();
		listSvc.crossOffMyBeer(customersList.getId(),beer.getId());
		
		BeerList updatedList = listSvc.getBeerList(customersList.getId());
		Integer afterNumOrdered = updatedList.getNumberOrderedOnList();
		Assert.assertTrue(afterNumOrdered == beforeNumOrdered+1);
	}
	
	@Test(expected=Brewception.class)
	@Transactional
	public void orderInvalidBeerId() {
		loginSvc.login("james@just-james.com", "admin");
		BeerList myList = listSvc.getMyBeerList();
		listSvc.crossOffMyBeer(myList.getId(),-1); 
	}

	
	@Test
	@Transactional
	public void validOrderedToComplete() {
		loginSvc.login("james@just-james.com", "admin");
		BeerList myList = listSvc.getMyBeerList();
		BeerOnList beer = myList.getBeerOnList().iterator().next();
		listSvc.crossOffMyBeer(myList.getId(), beer.getId());
		
		myList = listSvc.getMyBeerList();
		Integer beforeRemaining = myList.getNumberRemaining();
		Integer beforeOrdered = myList.getNumberOrderedOnList();
		Integer beforeCompleted = myList.getNumberCompletedOnList();

		// Complete first beer
		listSvc.completeBeer(myList.getId(),beer.getId());

		myList = listSvc.getMyBeerList();
		Integer afterRemaining = myList.getNumberRemaining();
		Integer afterOrdered = myList.getNumberOrderedOnList();
		Integer afterCompleted = myList.getNumberCompletedOnList();
		Assert.assertTrue(afterRemaining == beforeRemaining-1);
		Assert.assertTrue(afterOrdered == beforeOrdered);
		Assert.assertTrue(afterCompleted == beforeCompleted+1);
	}
	
	@Test(expected=Brewception.class)
	@Transactional
	public void completedInvalidBeerId() {
		loginSvc.login("james@just-james.com", "admin");
		BeerList myList = listSvc.getMyBeerList();
		BeerOnList beer = myList.getBeerOnList().iterator().next();
		listSvc.crossOffMyBeer(myList.getId(), beer.getId());
		
		// Complete invalid beer id 
		listSvc.completeBeer(myList.getId(),-1);
	}
	
	@Test(expected=NotAuthorizedException.class)
	@Transactional
	public void onlyAdminCanComplete() {
		// Only an admin can mark beers complete 
		User testCustomer = addTestUser("onlyAdminCanComplete");
		ActiveUser customer = new ActiveUser(testCustomer);
		BeerList customersList = listSvc.startList(customer);
		loginSvc.login(customer.getEmail(), testCustomer.getPassword());

		BeerOnList beer = customersList.getBeerOnList().iterator().next();
		listSvc.completeBeer(customersList.getId(),beer.getId());
	}
	
	@Test
	@Transactional
	public void adminRetrievesListToComplete() {
		// Setup a couiple of lists
		User testUser1 = addTestUser("adminRetrievesListToComplete1");
		User testUser2 = addTestUser("adminRetrievesListToComplete2");
		ActiveUser user1 = new ActiveUser(testUser1);
		ActiveUser user2 = new ActiveUser(testUser2);
		loginSvc.login("james@just-james.com","admin");
		BeerList listUser1 = listSvc.startList(user1);
		BeerList listUser2 = listSvc.startList(user2);
		listSvc.crossOffMyBeer(listUser1.getId(), listUser1.getBeerOnList().iterator().next().getId());
		listSvc.crossOffMyBeer(listUser2.getId(), listUser2.getBeerOnList().iterator().next().getId());
		
		Collection<BeerToComplete> allLists = listSvc.getBeersToComplete();
		Assert.assertTrue(allLists.size() > 0);
	}




}
