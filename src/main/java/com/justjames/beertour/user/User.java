package com.justjames.beertour.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import com.justjames.beertour.security.Role;

@Entity
@Table(name="USER")
public class User implements Serializable {


	private static final long serialVersionUID = 6751101780337302221L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="USER_PK")
	private Integer id;
	
	@Column(name="email",nullable=false,unique=true)
	private String email;
	
	@Column(name="name",nullable=false)
	private String name;
	
	@Column(name="password",nullable=false)
	private String password;

	@Column(name="nickname",unique=true)
	private String nickName;
	
	@Column(name="role")
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Column(name="lists_completed")
	private Integer numListsCompleted;
	
	@Column(name="token")
	private String token;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getNumListsCompleted() {
		return numListsCompleted;
	}

	public void setNumListsCompleted(Integer numListsCompleted) {
		this.numListsCompleted = numListsCompleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String
				.format("User [id=%s, email=%s, name=%s, nickName=%s, role=%s, numListsCompleted=%s]",
						id, email, name, nickName, role, numListsCompleted);
	}
	
	/**
	 * Returns Nickname if set or Name otherwise
	 * @return
	 */
	@Transient
	public String getDisplayName() {
		if  (StringUtils.hasLength( getNickName() )) {
			return getNickName();
		} else {
			return getName();
		}
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	


}
