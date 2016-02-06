package com.justjames.beertour.user;

import java.util.Collection;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/users")
public class UserController {
	
	@Inject
	private UserSvc userSvc;
	
	@CrossOrigin
	@RequestMapping(value="/",method=RequestMethod.GET)
	public UserCollection getAll() {
		Collection<User> users = userSvc.getAll();
		UserCollection response = new UserCollection(users);
		if (users == null) {
			response.setErrorMsg("No users found");
		}
		return response;
	}
	
	@CrossOrigin(methods={RequestMethod.POST,RequestMethod.OPTIONS})
	@RequestMapping(value="/signup",method=RequestMethod.POST)
	@Transactional
	public User signupLogin(@RequestBody User user) {
		return userSvc.loginSignup(user);
	}
	

}
