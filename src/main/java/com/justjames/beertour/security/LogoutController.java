package com.justjames.beertour.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/logout")
public class LogoutController {
	
	@Autowired
	private LoginSvc loginSvc;
	
	@CrossOrigin(origins="*",methods={RequestMethod.POST})
	@RequestMapping(value="/",method=RequestMethod.POST)
	public void logout() {
		loginSvc.logout();
	}
	

}
