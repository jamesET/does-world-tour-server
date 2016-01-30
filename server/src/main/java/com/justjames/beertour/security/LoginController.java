package com.justjames.beertour.security;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/login")
public class LoginController {
	
	@Inject
	private LoginSvc loginSvc;
	
	@CrossOrigin(origins="*",methods={RequestMethod.POST})
	@RequestMapping(value="/",method=RequestMethod.POST)
	public ActiveUser login(@RequestBody LoginRequest request) {
		return loginSvc.login(request.getUsername(),request.getPassword());
	}
	

}
