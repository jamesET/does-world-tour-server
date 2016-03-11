package com.justjames.beertour.activity;

import java.util.ArrayList;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.justjames.beertour.Brewception;
import com.justjames.beertour.Utils;
import com.justjames.beertour.beer.Beer;
import com.justjames.beertour.beer.BeerSvc;
import com.justjames.beertour.beerlist.BeerList;
import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserSvc;


@Component
public class ActivityLogSvc {
	
	@Autowired
	private ActivityLogRepo repo;
	
	@Autowired private UserSvc userSvc;
	
	@Autowired private BeerSvc beerSvc;
	
	static final private int PAGE_SIZE = 20;

	/**
	 * This method logs a beer for the activity feed
	 * 
	 * @param list The user's beer list
	 * @param beer The beer to be logged
	 */
	@Transactional
	public void logBeer(BeerList list,Beer beer) {
		User u = list.getUser();

		ActivityLog activity = new ActivityLog();
		activity.setId(null);
		activity.setUserId(u.getId());
		activity.setTime(Utils.now());
		activity.setBeerId(beer.getId());
		activity.setListNbr(list.getListNumber());
		activity.setListProgressPct(list.getListProgressPct());

		repo.save(activity);
	}

	/**
	 * @param userId Optional, The user for which to get events 
	 * @param pageNumber The page of activities to return
	 * @return  The targeted slice of activities from the feed
	 */
	public ActivityFeed getActivityFeed(Integer userId, int pageNumber) {
		ActivityFeed feed = null;

		if (pageNumber < 1) {
			throw new Brewception("Invalid page number requested in feed");
		}
		
		try {

			// pages are numbered from zero
			int effectivePageNumber = pageNumber - 1;
			PageRequest pr = new PageRequest(effectivePageNumber,PAGE_SIZE, new Sort(Direction.DESC,"time","id"));
			
			Page<ActivityLog> page;

			if (userId != null && userId > 0) {
				// get for a single user
				page = repo.findByUserId(userId,pr);
			} else {
				// get all
				page = repo.findAll(pr);
			}
			
			// Transform to activity feed
			feed = new ActivityFeed();
			feed.setPageNumber(pageNumber);
			feed.setHasNextPage(page.hasNext());
			feed.setHasPreviousPage(page.hasPrevious());
			
			ArrayList<ActivityLogTO> activities = new ArrayList<ActivityLogTO>();
			for (ActivityLog actLog : page.getContent()) {
				Beer beer = beerSvc.getBeer(actLog.getBeerId());
				User user = userSvc.getUser(actLog.getUserId());
				activities.add(new ActivityLogTO(actLog,user,beer));
			}
			feed.setActivities(activities);

				
		} catch (EntityNotFoundException enf) {
			// return an empty feed
			feed = getEmptyFeed(); 
		}
		
		return feed;
	}

	
	static final private ActivityFeed getEmptyFeed() {
		ActivityFeed feed = new ActivityFeed();
		feed.setHasNextPage(false);
		feed.setHasPreviousPage(false);
		feed.setActivities(new ArrayList<ActivityLogTO>(1));
		feed.setPageNumber(1);
		return feed;
	}
	

	
	
}
