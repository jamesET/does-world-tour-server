package com.justjames.beertour.user;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.justjames.beertour.Brewception;
import com.justjames.beertour.beerlist.BeerListSvc;
import com.justjames.beertour.security.ActiveUser;
import com.justjames.beertour.security.LoginSvc;
import com.justjames.beertour.security.Role;
import com.justjames.beertour.security.UserUtils;

@Named
public class UserSvc {
	
	private Log log = LogFactory.getLog(UserSvc.class);
	
	@Inject 
	IUserRepository userRepo;
	
	@Inject
	BeerListSvc listSvc;
	
	@Inject
	LoginSvc loginSvc;
	
	public Collection<User> getAll() {
		if (!UserUtils.isAdmin()) {
		 throw new Brewception("Only Admin can list all users:" + UserUtils.getActiveUser());	
		} 
		return userRepo.findAll();
	}
	
	/**
	 * @param id
	 * @return
	 */
	public User getUser(Integer id) {
		return userRepo.getOne(id);
	}
	
	/**
	 * @param u
	 * @return
	 */
	public User addUser(User u) {
		
		if (u.getNumListsCompleted() == null) {
			u.setNumListsCompleted(0);
		}
		
		validateUser(u);
		return userRepo.saveAndFlush(u);
	}
	
	/**
	 * Save changes to a beer
	 * @param beer
	 * @return
	 */
	@Transactional
	public User update(User user) {
		log.info("Updating user: " + user);

		ActiveUser loggedInUser = UserUtils.getActiveUser();
		if (!UserUtils.isAdmin() && loggedInUser.getUserId() != user.getId() ) {
			throw new Brewception("Only admin or user can update account.");
		}
		
		if (!userRepo.exists(user.getId())) {
			throw new Brewception("User does not exist:" + user);
		}
		
		User updatedUser = userRepo.saveAndFlush(user);
		return updatedUser;
	}

	
	/**
	 * @param finisher
	 */
	public void addToListFinished(User finisher) {
		User u = userRepo.getOne(finisher.getId());
		Integer listsCompleted = u.getNumListsCompleted() + 1;
		u.setNumListsCompleted(listsCompleted);
		userRepo.saveAndFlush(u);
	}

	/**
	 * @param newUser
	 * @return
	 */
	@Transactional
	public User loginSignup(User newUser) {
		newUser.setRole(Role.CUSTOMER);
		newUser.setNumListsCompleted(0);
		log.info("Adding new user " + newUser);
		
		if (emailExists(newUser.getEmail())) {
			String msg = String.format("'%s' already exists, please login.",newUser.getEmail());
			log.warn(msg);
			throw new Brewception(msg);
		}

		User savedUser = addUser(newUser);
		
		// Login their session
		loginSvc.login(savedUser.getEmail(),savedUser.getPassword());

		return savedUser;
	}
	
	
	/**
	 * @param email
	 * @return
	 */
	public User findByEmail(String email) {
		User user = null;
		user = userRepo.findByEmail(email.trim());
		return user;
	}

	public boolean emailExists(String email) {
		User user = findByEmail(email.trim());
		return user != null ? true : false;
	}
	
	private boolean validateUser(User user) {
		if (user == null) {
			throw new Brewception("User can't be null.");
		}
		
		if (StringUtils.isBlank(user.getEmail())) {
			throw new Brewception("Email is required!");
		}

		if (StringUtils.isBlank(user.getName())) {
			throw new Brewception("Name is required!");
		}

		if (StringUtils.isBlank(user.getPassword())) {
			throw new Brewception("Password is required!");
		}

		return true;
	}
	
	

}
