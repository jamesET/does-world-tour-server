package com.justjames.beertour.user;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.justjames.beertour.security.Role;

@XmlRootElement
public class UserEditTO implements Serializable {
	
	private static final long serialVersionUID = 7489937836693472980L;

	private Role role;
	
	public UserEditTO() {}
	
	public UserEditTO(User u) {
		this.role = u.getRole();
	}
	
	@XmlElement
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserEditTO [role=" + role + "]";
	}

}
