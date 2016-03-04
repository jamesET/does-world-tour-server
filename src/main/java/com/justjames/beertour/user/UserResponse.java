package com.justjames.beertour.user;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.justjames.beertour.BaseResponse;

@XmlRootElement
public class UserResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	private Collection<User> users;
	
	public UserResponse(Collection<User> users) {
		this.users = users;
	}
	
	public UserResponse(User user) {
		this.users = new ArrayList<User>();
		this.users.add(user);
	}
	
	@XmlElementWrapper
	public Collection<User> getUsers() {
		return users;
	}
	
	public void setUsers(Collection<User> users) {
		this.users = users;
	}

}
