package com.justjames.beertour.beer;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.justjames.beertour.Brewception;
import com.justjames.beertour.security.UserUtils;

@Component
public class BeerSvc {
	
	private Log log = LogFactory.getLog(BeerSvc.class);
	
	@Autowired 
	BeerRepository beerRepo;

	public Collection<Beer> getAll() {
		return beerRepo.findAll();
	}

	@Transactional
	public Beer add(Beer newBeer) {
		newBeer.setCreateDate(LocalDate.now());
		
		if (!UserUtils.isAdmin()) {
			log.warn("User not authorized to add beer.");
			return null;
		}

		log.info("Updating beer: " + newBeer);
		Beer savedBeer = beerRepo.save(newBeer);
		return savedBeer; 
	}
	
	/**
	 * Save changes to a beer
	 * @param updatedBeer
	 * @return
	 */
	@Transactional
	public Beer update(BeerUpdateTO updatedBeer) {
		log.info("Updating beer: " + updatedBeer);

		if (!UserUtils.isAdmin()) {
			throw new Brewception("Only admin users can udpate beer.");
		}
		
		Beer beer = null;
		try {

			beer = beerRepo.getOne(updatedBeer.getId());
			// These are the only attributes that may be updated
			beer.setName(updatedBeer.getName());
			beer.setBrewery(updatedBeer.getBrewery());
			beer.setCountry(updatedBeer.getCountry());
			beer.setRegion(updatedBeer.getRegion());
			beer.setStyle(updatedBeer.getStyle());
			beer.setAbv(updatedBeer.getAbv());
			beer.setOz(updatedBeer.getOz());
			beer.setDiscontinued(updatedBeer.isDiscontinued());
			beer.setOutOfStock(updatedBeer.isOutOfStock());

		} catch (EntityNotFoundException nf) {
			throw new Brewception("Beer not found");
		} catch (PersistenceException pe) {
			throw new Brewception("Error, can't update beer");
		}
		
		Beer savedBeer = null;
		try {
			savedBeer = beerRepo.saveAndFlush(beer);
		} catch (DataAccessException de) {
			log.error(de.getMessage() + "\n" + updatedBeer);
			throw new Brewception("Update Beer Failed");
		}
		
		return savedBeer;
	}
	

}
