package com.justjames.beertour.beer;

import java.util.Collection;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/beers")
public class BeerController {
	
	@Autowired
	private BeerSvc beerSvc;
	
	private Log log = LogFactory.getLog(BeerController.class);
	
	@RequestMapping(value="/browse",method=RequestMethod.GET)
	public BeerResponse getAll() {
		Collection<Beer> beers = beerSvc.getAll();
		BeerResponse response = new BeerResponse(beers);
		if (beers == null) {
			response.setErrorMsg("No beers found");
		}
		return response; 
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Beer addBeer(@RequestBody Beer beer) {
		log.debug("Adding beer " + beer.getName());
		return beerSvc.add(beer);
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public Beer updateBeer(@RequestBody BeerUpdateTO beer) {
		log.debug("Updating beer " + beer);
		return beerSvc.update(beer);
	}

}
