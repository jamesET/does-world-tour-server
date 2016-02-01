package com.justjames.beertour.security;

import javax.inject.Inject;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	
	
	// Convert a predefined exception to an HTTP Status code
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="Not Authorized")  // 401
	@ExceptionHandler(AuthenticationException.class)
	public void notAuthorized() {
	  // Nothing to do
	}
	

}
