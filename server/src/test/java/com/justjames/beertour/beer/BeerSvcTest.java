package com.justjames.beertour.beer;

import java.util.Collection;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.justjames.beertour.BeerTourApplication;
import com.justjames.beertour.beer.Beer;
import com.justjames.beertour.beer.BeerSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BeerTourApplication.class)
public class BeerSvcTest {
	
		@Inject
		BeerSvc beerSvc;
		
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
		public void addBeer() {
			Beer newBeer = new Beer();
			newBeer.setName("Jimmy Light");
			newBeer.setBrewery("Jimmy Brewing");
			newBeer.setCountry("US");
			Beer savedBeer = beerSvc.add(newBeer);
			Assert.assertNotNull(savedBeer);
			Assert.assertTrue(savedBeer.getId() > 0);
		}
	

}
