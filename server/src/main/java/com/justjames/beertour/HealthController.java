package com.justjames.beertour;

import java.util.Collection;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.justjames.beertour.beer.Beer;
import com.justjames.beertour.beer.BeerSvc;

@RestController
@RequestMapping(value="/health")
public class HealthController {
	
	@Autowired
	BeerSvc beerSvc;
	
	private Log log = LogFactory.getLog(HealthController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<String> healthCheck() {
		boolean isHealthy = true;
		Collection<Beer> beers = beerSvc.getAll();
		if (beers.size() < 1) {
			isHealthy = false;
			log.error("Health Check failed, beer count =" + beers.size() );
		}
		
		if (isHealthy) {
			return new ResponseEntity<String>("", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("", HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

}
