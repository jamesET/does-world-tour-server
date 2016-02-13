package com.justjames.beertour;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.justjames.beertour.beer.Beer;
import com.justjames.beertour.beer.BeerSvc;

@RestController
@RequestMapping(value="/health")
public class HealthController {
	
	@Inject
	BeerSvc beerSvc;
	
	private Log log = LogFactory.getLog(HealthController.class);
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public Response healthCheck() {
		boolean isHealthy = true;
		Collection<Beer> beers = beerSvc.getAll();
		if (beers.size() < 1) {
			isHealthy = false;
			log.error("Health Check failed, beer count =" + beers.size() );
		}
		
		if (isHealthy) {
			return Response.ok().build();
		} else {
			return Response.serverError().build(); 
		}
	}

}
