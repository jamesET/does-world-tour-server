package com.justjames.beertour.user;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.justjames.beertour.security.Role;

@XmlRootElement(name="user")
public class UserTO implements Serializable {
	
	private static final long serialVersionUID = -9121440569645950959L;

	private Integer id;
	private String email;
	private String name;
	private String nickName;
	private Role role;
	private Integer numListsCompleted;

	public UserTO(User u) {
		this.id = u.getId();
		this.email = u.getEmail();
		this.name = u.getName();
		this.nickName = u.getNickName();
		this.numListsCompleted = u.getNumListsCompleted();
		this.role = u.getRole();
	}

	@XmlElement
	public Integer getId() {
		return id;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	@XmlElement
	public String getNickName() {
		return nickName;
	}

	@XmlElement
	public Role getRole() {
		return role;
	}

	@XmlElement
	public Integer getNumListsCompleted() {
		return numListsCompleted;
	}

}
