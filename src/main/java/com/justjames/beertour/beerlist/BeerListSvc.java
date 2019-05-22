package com.justjames.beertour.beerlist;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justjames.beertour.Brewception;
import com.justjames.beertour.Utils;
import com.justjames.beertour.activity.ActivityLogSvc;
import com.justjames.beertour.beer.Beer;
import com.justjames.beertour.beer.BeerSvc;
import com.justjames.beertour.security.ActiveUser;
import com.justjames.beertour.security.NotAuthorizedException;
import com.justjames.beertour.security.UserUtils;
import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserSvc;

/**
 * @author James Thomas
 *
 */
@Component
public class BeerListSvc {
	
	private Log log = LogFactory.getLog(BeerListSvc.class);
	
	@Autowired
	private BeerListRepository listRepo;
	
	@Autowired
	private BeerOnListRepository beerOnListRepo;
	
	@Autowired
	private BeerSvc beerSvc;
	
	@Autowired
	private UserSvc userSvc;
	
	@Autowired
	ActivityLogSvc activitySvc;

	BeerListCompleteSpec beerListCompleteSpec = new BeerListCompleteSpec();

	/**
	 * @param email
	 * @return
	 */
	@Transactional
	public BeerList startList(ActiveUser u) {
		
		// There should only be one active list per user 
		if ( findActiveBeerListByEmail(u.getEmail()) != null) {
			throw new Brewception("Person already has an active list.");
		}
		
		User user = userSvc.getUser(u.getUserId());
		
		// Add all the beers to the list 
		BeerList newList = new BeerList();
		newList.setUser(user);
		newList.setStartDate(Utils.getUTC());
		newList.setFinishDate(null);
		newList.setListNumber(user.getNumListsCompleted() + 1); 
		Integer beerCount = 0;
		for (Beer beer : beerSvc.getAll() ) {
			if (beer.isDiscontinued()) {
				// Discontinued beers don't go on the list
				continue;
			}
			BeerOnList listBeer= new BeerOnList(beer);
			listBeer.setOrdered(false);
			listBeer.setOrderedDate(null);
			listBeer.setCompleted(false);
			listBeer.setCompletedDate(null);
			newList.addBeer(listBeer);
			beerCount++;
		}
		
		BeerList savedList = listRepo.saveAndFlush(newList);
		
		log.info("List started: " + savedList );
		return savedList;
	}
	
	/**
	 * @param email
	 * @return
	 */
	public BeerList findActiveBeerListByEmail(String email) {
		BeerList found = null;

		User user = userSvc.findByEmail(email);
		if (user == null) {
			log.warn("User not found for '" + email + "'");
			return null;
		}
		
		found = listRepo.findByUserIdAndFinishDateIsNull(user.getId());
		
		return found;
	}
	
	/**
	 * @return
	 */
	public Collection<BeerList> findAll() {
		return listRepo.findAll();
	}

	/**
	 * @param id
	 * @return
	 */
	public BeerList getBeerList(Integer id) {
		BeerList beerList = listRepo.getOne(id);
		log.debug(beerList);
		
		return beerList; 
	}
	
	/**
	 * @return Beerlist for the logged in user
	 */
	public BeerList getMyBeerList() {
		ActiveUser currentUser = UserUtils.getActiveUser();
		
		BeerList myList = listRepo.findByUserIdAndFinishDateIsNull(currentUser.getUserId());
		
		// Every user should have a list
		if (myList == null) {
			myList = startList(currentUser);
		} 

		log.debug("Fetched beer list for " + currentUser.getEmail());
		return myList;
	}
	
	/**
	 * Mark beer as ordered for the logged in user 
	 * @param beer
	 */
	@Transactional
	public void crossOffMyBeer(Integer listId,Integer beerOnListId) {
		log.debug("crossOffMyBeer listId="+listId+" beerOnListId="+beerOnListId);

		// Get the targeted list
		BeerList beerList = getBeerList(listId);
		User listUser = beerList.getUser();
		ActiveUser loggedInUser = UserUtils.getActiveUser();
		
		// Only admin's can modify someone else's list
		if (!canChangeList(loggedInUser,beerList)) {
			String msg = String.format("User not authorized to edit list for '%s'", listUser.getEmail());
			log.warn(msg);
			throw new NotAuthorizedException(msg);
		}
		
		// Find the beer and show it as having been ordered 
		BeerOnList foundBeer = null;
		try {
			foundBeer = beerOnListRepo.getOne(beerOnListId);
			foundBeer.setOrdered(true);
			foundBeer.setOrderedDate(Utils.getUTC());

			activitySvc.logBeer(beerList, foundBeer.getBeer());
			
			beerOnListRepo.saveAndFlush(foundBeer);

		} catch (EntityNotFoundException e) {
			foundBeer = null;
			String msg = String.format("Beer (%d) not found in list for '%s'", 
					beerOnListId, listUser.getEmail());
			log.error(msg);
			// Throw an error if the beer wasn't found
			throw new Brewception(msg);
		}
		
		String msg = String.format("Ordered:  Beer '%s' for '%s' ordered by '%s'",
				foundBeer.getBeer().getName(), listUser.getEmail(), loggedInUser.getEmail());
		log.info(msg);
	}
	
	/**
	 * Administrator verifies that a beer was completed
	 * @param listId
	 * @param beer
	 */
	@Transactional
	public void completeBeer(Integer listId,Integer beerOnListId) {
		log.debug("verifyBeerCompleted listId="+listId+" beerOnListId="+beerOnListId);
		ActiveUser loggedInUser = UserUtils.getActiveUser();
		
		// Only the admin role can do completion
		if (!UserUtils.isAdmin()) {
			String msg = String.format("User '%s' is not authorized",loggedInUser.getEmail());
			throw new NotAuthorizedException(msg);
		}
		
		// Get the targeted list
		BeerList beerList = getBeerList(listId);
		User listUser = beerList.getUser();

		// Find the beer on the beerlist and show it as having been completed 
		BeerOnList foundBeer = null;
		try {
			foundBeer = beerOnListRepo.getOne(beerOnListId);
			foundBeer.setCompleted(true);
			foundBeer.setCompletedDate(Utils.getUTC());
			foundBeer.setCompletedBy(loggedInUser.getEmail());
			beerOnListRepo.saveAndFlush(foundBeer);
		} catch (EntityNotFoundException e) {
			foundBeer = null;
			String msg = String.format("Beer (%d) not found in list for '%s'", 
					beerOnListId, listUser.getEmail());
			log.error(msg);
			// Throw an error if the beer wasn't found
			throw new Brewception(msg);
		}
		
		String msg = String.format("Completed:  Beer '%s' for '%s' completed by '%s'",
				foundBeer.getBeer().getName(), listUser.getEmail(), loggedInUser.getEmail());
		log.info(msg);

		// If the list is complete finish it off
		if (beerListCompleteSpec.IsSatisfiedBy(beerList)) {
			beerList = completeList(beerList);
		}
	}
	
	/**
	 * Administrator rejects that a customer completed a beer 
	 * @param listId
	 * @param beer
	 */
	@Transactional
	public void rejectBeer(Integer listId,Integer beerOnListId) {
		log.debug("uncompleteBeer listId="+listId+" beerOnListId="+beerOnListId);
		ActiveUser loggedInUser = UserUtils.getActiveUser();
		
		// Only the admin role can do un-completion
		if (!UserUtils.isAdmin()) {
			String msg = String.format("User '%s' is not authorized",loggedInUser.getEmail());
			throw new NotAuthorizedException(msg);
		}
		
		// Get the targeted list
		BeerList beerList = getBeerList(listId);
		User listUser = beerList.getUser();

		// Find the beer on the beerlist and return it to the user's list 
		BeerOnList foundBeer = null;
		try {
			foundBeer = beerOnListRepo.getOne(beerOnListId);
			foundBeer.setOrdered(false);
			foundBeer.setOrderedDate(null);
			beerOnListRepo.saveAndFlush(foundBeer);
		} catch (EntityNotFoundException e) {
			foundBeer = null;
			String msg = String.format("Beer (%d) not found in list for '%s'", 
					beerOnListId, listUser.getEmail());
			log.error(msg);
			// Throw an error if the beer wasn't found
			throw new Brewception(msg);
		}
		
		String msg = String.format("Rejected:  Beer '%s' for '%s' completed by '%s'",
				foundBeer.getBeer().getName(), listUser.getEmail(), loggedInUser.getEmail());
		log.info(msg);

	}

	
	/**
	 * @param list
	 */
	@Transactional
	private BeerList completeList(BeerList list) {
		userSvc.addToListFinished(list.getUser());
		list.setFinishDate(Utils.getUTC());
		listRepo.saveAndFlush(list);
		activitySvc.logListComplete(list);
		String msg = String.format("COMPLETED: '%s' finished list #%d", list
				.getUser().getEmail(), list.getListNumber());
		log.info(msg);
		return list;
	}
	
	/**
	 * @param u
	 * @param list
	 * @return true if the current user is allowed to edit/update list
	 */
	private boolean canChangeList(ActiveUser loggedInUser, BeerList beerList) {
		boolean canChange = false;
		ActiveUser listUser = new ActiveUser(beerList.getUser());
		if (!UserUtils.isAdmin() && !listUser.equals(loggedInUser)) {
			// You can't modify if it's not yours and you're not an admin
			canChange = false;
		} else {
			canChange = true;
		}
		return canChange;
	}
	
	/**
	 * @return The list of the beers that have been ordered by customers.  An administrator needs to review the list and complete each one
	 */
	public Collection<BeerToComplete> getBeersToComplete() {
		ActiveUser loggedInUser = UserUtils.getActiveUser();
		
		if (!UserUtils.isAdmin()) {
			String msg = String.format("User '%s' not authorized to complete.", loggedInUser.getEmail());
			throw new NotAuthorizedException(msg);
		}
		
		Collection<BeerToComplete> completeList = new ArrayList<BeerToComplete>();
		
		//TODO This should be a custom query rather than for-loops
		Collection<BeerList> beerLists = listRepo.findByFinishDateIsNull();
		for (BeerList beerList : beerLists) {
			for (BeerOnList beer : beerList.getBeerOnList()) {
				if (beer.isOrdered() && !beer.isCompleted()) {
					BeerToComplete beerToComplete = new BeerToComplete(
							beerList.getUser(), beerList.getId(), beer);
					completeList.add(beerToComplete);
				}
			}
		}		
		
		return completeList;
	}

	/**
	 * This method will auto-complete all beers showing ordered but not confirmed as complete. 
	 */
	public void completeAllOutstanding() {
		
		ActiveUser loggedInUser = UserUtils.getActiveUser();

		if (!UserUtils.isAdmin()) {
			String msg = String.format("User '%s' not authorized to complete.", loggedInUser.getEmail());
			throw new NotAuthorizedException(msg);
		}
		
		unSecuredCompleteAllOutstanding();
	}
	
	public void unSecuredCompleteAllOutstanding() {
		
		log.info("Completing All Outstanding Beers");

		Collection<BeerToComplete> beersToComplete = getBeersToComplete();
		for (BeerToComplete entry : beersToComplete) {
			completeBeer(entry.getBeerListId(),entry.getBeer().getId());
		}
		
	}
	

}
