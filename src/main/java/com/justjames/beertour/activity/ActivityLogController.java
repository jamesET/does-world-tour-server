package com.justjames.beertour.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
		
		ActivityFeed feed = activitySvc.getBeerFeed(userId,page.intValue());

		return feed;
	}

	@RequestMapping(value="/v2",method=RequestMethod.GET)
	public ActivityFeed getFeed( @RequestParam Integer page, 
			@RequestParam(value="userId", required=false) Integer userId,
			@RequestParam(value="newsOnly", required=false) Boolean news) {
		
		ActivityFeed feed = activitySvc.getActivityFeed(userId,news,page.intValue());

		return feed;
	}
	
	@RequestMapping(value="/news",method=RequestMethod.POST)
	public ResponseEntity<String> newsPost(@RequestBody NewsTO news) {
		activitySvc.logNews(news.getMessage());
		return ResponseEntity.ok().body("OK");
	}

	

}
