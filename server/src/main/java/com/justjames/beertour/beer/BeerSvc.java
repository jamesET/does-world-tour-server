package com.justjames.beertour.beer;

import java.time.LocalDate;
import java.util.Collection;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justjames.beertour.Brewception;
import com.justjames.beertour.security.Role;
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
	 * @param beer
	 * @return
	 */
	@Transactional
	public Beer update(Beer beer) {
		log.info("Updating beer: " + beer);

		if (!UserUtils.isAdmin()) {
			throw new Brewception("Only admin users can udpate beer.");
		}
		
		if (!beerRepo.exists(beer.getId())) {
			throw new Brewception("Beer does not exist");
		}
		
		Beer updatedBeer = beerRepo.saveAndFlush(beer);
		return updatedBeer;
	}
	

}
