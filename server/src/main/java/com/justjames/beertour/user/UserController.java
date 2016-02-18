package com.justjames.beertour.user;

import java.util.Collection;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/users")
public class UserController {
	
	private Log log = LogFactory.getLog(UserController.class);
	
	@Autowired
	private UserSvc userSvc;
	
	@RequestMapping(method=RequestMethod.GET)
	public UserCollection getAll() {
		log.debug("getAll()");
		Collection<User> users = userSvc.getAll();
		UserCollection response = new UserCollection(users);
		if (users == null) {
			response.setErrorMsg("No users found");
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.PUT)
	public UserTO update(@RequestBody User user) {
		return new UserTO(userSvc.selfUpdate(user));
	}
	
	@RequestMapping(value="/{id}/adminEdit", method=RequestMethod.PUT)
	public UserEditTO adminEdit(@PathVariable("id") Integer userId, @RequestBody UserEditTO editedUser) {
		return new UserEditTO(userSvc.adminEdit(userId,editedUser));
		
	}
	
	@RequestMapping(value="/signup",method=RequestMethod.POST)
	@Transactional
	public User signupLogin(@RequestBody User user) {
		return userSvc.loginSignup(user);
	}

}
