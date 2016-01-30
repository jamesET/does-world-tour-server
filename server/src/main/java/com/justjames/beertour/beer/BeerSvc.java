package com.justjames.beertour.beer;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class BeerSvc {
	
	@Inject 
	BeerRepository beerRepo;

	public Collection<Beer> getAll() {
		return beerRepo.findAll();
	}

	public Beer add(Beer newBeer) {
		Beer savedBeer = beerRepo.save(newBeer);
		return savedBeer; 
	}

}
