package com.justjames.beertour.activity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.justjames.beertour.BeerTourApplication;
import com.justjames.beertour.beer.Beer;
import com.justjames.beertour.beerlist.BeerList;
import com.justjames.beertour.beerlist.BeerListSvc;
import com.justjames.beertour.beerlist.BeerOnList;
import com.justjames.beertour.security.ActiveUser;
import com.justjames.beertour.security.Role;
import com.justjames.beertour.shiro.AbstractShiroTest;
import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BeerTourApplication.class)
public class ActivityLogSvcTest extends AbstractShiroTest {
	

	@Autowired private ActivityLogSvc activityLog;
	
	@Autowired private UserSvc userSvc;
	
	@Autowired private BeerListSvc beerListSvc;
	
	@Test
	@Transactional
	public void getGlobalMostRecent() {
		User u1 = getUser("u1",Role.CUSTOMER);
		BeerList u1List = getList(u1);
		User u2 = getUser("u2",Role.CUSTOMER);
		BeerList u2List = getList(u2);
		
		Beer u1Beer1 = getBeerFromList(u1List,1);
		activityLog.logBeer(u1List, u1Beer1);

		Beer u2Beer1 = getBeerFromList(u1List,1);
		activityLog.logBeer(u2List, u2Beer1);

		ActivityFeed feed =  activityLog.getActivityFeed(null,1);
		assertNotNull("feed should not be null", feed);
		List<ActivityLogTO> activities = feed.getActivities();
		assertTrue("There should be three activities in feed",activities.size() == 2);
		Iterator<ActivityLogTO> iterator = activities.iterator();
		ActivityLogTO activity1 = iterator.next(); 
		assertTrue("The most recent activity was last call", activity1.getBeer().equals(u2Beer1));
	}
	
	@Test
	@Transactional
	public void getUserMostRecent() {
		User u1 = getUser("u1",Role.CUSTOMER);
		BeerList u1List = getList(u1);
		User u2 = getUser("u2",Role.CUSTOMER);
		BeerList u2List = getList(u2);

		// user 1 drinks first beer on list
		Beer u1Beer1 = getBeerFromList(u1List,1);
		activityLog.logBeer(u1List, u1Beer1);

		// user 2 drinks first beer on list
		Beer u2Beer1 = getBeerFromList(u1List,1);
		activityLog.logBeer(u2List, u2Beer1);
		
		// only user 1's beer should show in this feed 
		ActivityFeed feed = activityLog.getActivityFeed(u1.getId(),1);
		List<ActivityLogTO> activities =  feed.getActivities();
		Iterator<ActivityLogTO> iterator = activities.iterator();
		ActivityLogTO activity1 = iterator.next(); 
		assertTrue("There should be one item in feed",activities.size() == 1);
		assertTrue( activity1.getBeer().equals(u1Beer1));
	}

	
	@Test
	@Transactional
	public void getEmptyUserFeed() {
		User u1 = getUser("u1",Role.CUSTOMER);

		ActivityFeed feed = activityLog.getActivityFeed(u1.getId(),1);
		assertNotNull("Feed should be empty, not null.",feed);
		assertTrue("Feed should be empty!",feed.getActivities().size() == 0);
	}

	@Test
	@Transactional
	public void getEmptyGlobalFeed() {
		ActivityFeed feed = activityLog.getActivityFeed(null,1);
		assertNotNull("Feed should be empty, not null.",feed);
		assertTrue("Feed should be empty!",feed.getActivities().size() == 0);
	}
	
	@Transactional
	public void getInvalidIdUserFeed() {
		ActivityFeed feed = activityLog.getActivityFeed(-1,1);
		assertNotNull(feed);
		assertTrue(feed.getActivities().size() == 0);
	}
	
	@Test
	public void getInvalidPageUserFeed() {
		//TODO
	}
	
	@Test
	public void getInvalidPageGlobalFeed() {
		//TODO
	}

	
	@Transactional
	private User getUser(String userName,Role role){
		User u = new User();
		u.setEmail(userName + "@email.com");
		u.setPassword("1234");
		u.setName(userName);
		u.setNickName(userName);
		u.setRole(role);
		u.setNumListsCompleted(0);
		return userSvc.addUser(u);
	}
	
	@Transactional
	private BeerList getList(User u) {
		ActiveUser au = new ActiveUser(u);
		return beerListSvc.startList(au);
	}
	
	private Beer getBeerFromList(BeerList list, int n) {
		ArrayList<BeerOnList> beers = new ArrayList<BeerOnList>(list.getBeerOnList());
		return beers.get(n-1).getBeer(); 
	}
	

}
