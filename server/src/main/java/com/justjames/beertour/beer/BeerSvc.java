package com.justjames.beertour.beer;

import java.time.LocalDate;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.justjames.beertour.Brewception;
import com.justjames.beertour.security.Role;

@Named
public class BeerSvc {
	
	private Log log = LogFactory.getLog(BeerSvc.class);
	
	@Inject 
	BeerRepository beerRepo;

	public Collection<Beer> getAll() {
		return beerRepo.findAll();
	}

	public Beer add(Beer newBeer) {
		newBeer.setCreateDate(LocalDate.now());
		
		if (!isAdmin()) {
			log.warn("User not authorized to add beer.");
			return null;
		}

		Beer savedBeer = beerRepo.save(newBeer);
		return savedBeer; 
	}
	
	
	/**
	 * 
	 * @return true if the current logged-in user is an Admin
	 */
	private boolean isAdmin() {
		Subject subject = SecurityUtils.getSubject();
		if (subject == null) {
			throw new Brewception("User is not logged in!");
		}
		return subject.hasRole(Role.ADMIN.toString());
	}

}
