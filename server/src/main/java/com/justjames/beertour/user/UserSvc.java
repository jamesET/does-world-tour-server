package com.justjames.beertour.user;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.justjames.beertour.Brewception;
import com.justjames.beertour.beerlist.BeerListSvc;
import com.justjames.beertour.security.ActiveUser;
import com.justjames.beertour.security.LoginSvc;
import com.justjames.beertour.security.Role;

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
		if (isAdmin()) {
			return userRepo.findAll();
		} else {
			//TODO, this should be a 403 but just return an empty collection for now 
			log.info("User not allowed to get users");
			return new ArrayList<User>();
		}
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
	 *  if the email already exists then it could be an update
	 *  but before we can allow an update
	 *  	- must be logged in as that user
	 *         
	 *  if the email doesn't exist then just create the account 
	 *  
	 *  This service has to be open to both logged and not logged in users
	 * 
	 */
	@Transactional
	public User userUpdate(User user) {
		boolean allowUpdate = false;
		
		// If the email exists then see if we can update
		if (emailExists(user.getEmail())) {
			// is this the user logged in?
			Subject subject = SecurityUtils.getSubject();
			ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
			if (activeUser.getEmail().equals(user.getEmail())) {
				allowUpdate = true;
			}
		} else {
			// Initialize values for new user
			//newUser.setRole(Role.CUSTOMER);
			//newUser.setNumListsCompleted(0);
			//log.info("Adding new user " + newUser);

		}
		
		
		
		return null;
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
