package com.justjames.beertour.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/activities")
public class ActivityLogController {
	
	@Autowired
	private ActivityLogSvc activitySvc;
	
	@RequestMapping(method=RequestMethod.GET)
	public ActivityFeed getAll( @RequestParam Integer page, 
			@RequestParam(value="userId", required=false) Integer userId) {
		
		ActivityFeed feed = activitySvc.getActivityFeed(userId,page.intValue());

		return feed;
	}

	

}
