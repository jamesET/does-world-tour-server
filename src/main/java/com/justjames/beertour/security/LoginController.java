package com.justjames.beertour.security;


import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/login")
public class LoginController {
	
	@Autowired
	private LoginSvc loginSvc;
	
	@RequestMapping(value="/",method=RequestMethod.POST)
	public ActiveUser login(@RequestBody LoginRequest request) {
		return loginSvc.login(request.getUsername(),request.getPassword());
	}
	
	@RequestMapping(value="/sendpass",method=RequestMethod.POST) 
	public void sendPassword(@RequestParam("email") String email) {
		loginSvc.sendPassword(email);
	}
	
	// Convert a predefined exception to an HTTP Status code
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="Not Authorized")  // 401
	@ExceptionHandler(AuthenticationException.class)
	public void notAuthorized() {
	  // Nothing to do
	}
	

}
