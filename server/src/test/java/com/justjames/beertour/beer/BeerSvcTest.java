package com.justjames.beertour.beer;

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

import com.justjames.beertour.BeerTourApplication;
import com.justjames.beertour.Brewception;
import com.justjames.beertour.beer.Beer;
import com.justjames.beertour.beer.BeerSvc;
import com.justjames.beertour.security.LoginSvc;
import com.justjames.beertour.security.Role;
import com.justjames.beertour.security.UserRealm;
import com.justjames.beertour.shiro.AbstractShiroTest;
import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BeerTourApplication.class)
public class BeerSvcTest extends AbstractShiroTest {
	
		@Autowired BeerSvc beerSvc;
		@Autowired UserRealm userRealm;
		@Autowired UserSvc userSvc;
		@Autowired LoginSvc loginSvc;
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
		
		/* In order to sell beer
		 * I want to see the list of all the beers I have
		 * so that I can see what beers are available. 
		 */
		@Test
		public void listBeers() {
			Collection<Beer> allBeers = beerSvc.getAll();
			Assert.assertNotNull(allBeers);
		}
		
		/* In order to sell beer
		 * I want to add a new beer
		 * so that it will be available to customers. 
		 */
		@Test
		@Transactional
		public void addBeer() {
			addTestUser("addBeer",Role.ADMIN);
			Beer newBeer = new Beer();
			newBeer.setName("Jimmy Light");
			newBeer.setBrewery("Jimmy Brewing");
			newBeer.setCountry("US");
			Beer savedBeer = beerSvc.add(newBeer);
			Assert.assertNotNull(savedBeer);
			Assert.assertTrue(savedBeer.getId() > 0);
		}
		
		@Test
		@Transactional
		public void updateExistingBeerAsAdmin() {
			addTestUser("updateExistingBeerAsAdmin",Role.ADMIN);
			Collection<Beer> allBeers = beerSvc.getAll();
			Beer beer = allBeers.iterator().next();
			beer.setOutOfStock(true);
			Beer updatedBeer = beerSvc.update(beer);
			Assert.assertTrue(updatedBeer.isOutOfStock());
		}
		
		@Test(expected=Brewception.class)
		@Transactional
		public void updateExistingBeerAsCustomer() {
			addTestUser("updateExistingBeerAsCustomer",Role.CUSTOMER);
			Collection<Beer> allBeers = beerSvc.getAll();
			Beer beer = allBeers.iterator().next();
			beer.setOutOfStock(true);
			beerSvc.update(beer);
		}

		@Test(expected=Brewception.class)
		@Transactional
		public void updateInvalidBeerAsAdmin() {
			addTestUser("updateInvalidBeerAsAdmin",Role.ADMIN);
			Beer newBeer = new Beer();
			newBeer.setId(-1);
			beerSvc.update(newBeer);
		}

		
	

}
