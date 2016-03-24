package com.justjames.beertour.user;

import java.util.Collection;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justjames.beertour.InvalidPostDataException;
import com.justjames.beertour.ResourceException;
import com.justjames.beertour.beerlist.BeerListSvc;
import com.justjames.beertour.security.ActiveUser;
import com.justjames.beertour.security.LoginSvc;
import com.justjames.beertour.security.NotAuthorizedException;
import com.justjames.beertour.security.Role;
import com.justjames.beertour.security.UserUtils;

@Component
public class UserSvc {
	
	private Log log = LogFactory.getLog(UserSvc.class);
	
	@Autowired 
	UserRepository userRepo;
	
	@Autowired
	BeerListSvc listSvc;
	
	@Autowired
	LoginSvc loginSvc;
	
	public Collection<User> getAll() {
		if (!UserUtils.isAdmin()) {
		 throw new NotAuthorizedException("Only Admin can list all users:" + UserUtils.getActiveUser());	
		} 
		return userRepo.findAllByOrderByNameAsc();
	}
	
	/**
	 * @param id
	 * @return
	 */
	public User getUser(Integer id) {
		User user = null;
		try {
			user = userRepo.findOne(id);
		} catch (EntityNotFoundException enf) {
			log.info("No user found for id=" + id);
		}
		return user;
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
	 * @param user
	 * @return
	 */
	@Transactional
	public User selfUpdate(User user) {
		log.info("Updating user: " + user);

		ActiveUser loggedInUser = UserUtils.getActiveUser();
		if (!UserUtils.isAdmin() && loggedInUser.getUserId() != user.getId() ) {
			throw new NotAuthorizedException("Only admin or user can update account.");
		}

		
		User currentUser = null;
		currentUser = userRepo.findByEmail(user.getEmail());
		if (currentUser == null) {
			throw new InvalidPostDataException("User does not exist:" + user);
		}
		
		// These are the only attributes that the user can actually update
		currentUser.setName(user.getName());
		currentUser.setPassword(user.getPassword());
		currentUser.setNickName(user.getNickName());
		
		User updatedUser = userRepo.saveAndFlush(currentUser);
		return updatedUser;
	}
	
	/**
	 * @param editedUser
	 * @return
	 */
	@Transactional
	public User adminEdit(Integer userId, UserEditTO editedUser) {
		log.info("adminEdit: " + editedUser);

		ActiveUser loggedInUser = UserUtils.getActiveUser();
		if (!UserUtils.isAdmin() && loggedInUser.getUserId() != userId ) {
			throw new NotAuthorizedException("Only admin or user can update account.");
		}
		
		User currentUser = null;
		User updatedUser = null;
		try {
			currentUser = userRepo.getOne(userId);
			if (currentUser == null) {
				throw new InvalidPostDataException("User does not exist:" + editedUser);
			}
			
			if (currentUser.getRole() != editedUser.getRole()) {
				log.info(String.format("%s assigned to role '%s' by %s",
					currentUser.getName(),
					editedUser.getRole(),
					loggedInUser.getUserTO().getName()));
			}
			
			// These are the only attributes that the user can actually update
			currentUser.setRole(editedUser.getRole());

			updatedUser = userRepo.saveAndFlush(currentUser);
			
		} catch (EntityNotFoundException enf) {
			throw new InvalidPostDataException("User not found");
		} catch (PersistenceException pe) {
			throw new ResourceException("Error, can't update user");
		}
	
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
		log.info("Adding new user " + newUser);
		
		if (emailExists(newUser.getEmail())) {
			String msg = String.format("'%s' already exists",newUser.getEmail());
			log.warn(msg);
			throw new UserExistsException(msg);
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
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		User user = findByEmail(email.trim());
		return user != null ? true : false;
	}
	
	private boolean validateUser(User user) {
		if (user == null) {
			throw new InvalidPostDataException("User can't be null.");
		}
		
		if (StringUtils.isBlank(user.getEmail())) {
			throw new InvalidPostDataException("Email is required!");
		}

		if (StringUtils.isBlank(user.getName())) {
			throw new InvalidPostDataException("Name is required!");
		}

		if (StringUtils.isBlank(user.getPassword())) {
			throw new InvalidPostDataException("Password is required!");
		}

		return true;
	}
	
	

}
