package com.justjames.beertour.user;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.justjames.beertour.BaseResponse;

@XmlRootElement
public class UserCollection extends BaseResponse {
	
	private static final long serialVersionUID = 3601813937314094957L;

	private Collection<UserTO> users;
	
	public UserCollection(Collection<User> users) {
		Collection<UserTO> userColl = new ArrayList<UserTO>();
		for (User u : users) {
			UserTO uto = new UserTO(u);
			userColl.add(uto);
		}
		this.users = userColl;
	}
	
	public UserCollection(User user) {
		Collection<UserTO> users = new ArrayList<UserTO>();
		users.add(new UserTO(user));
	}

	@XmlElementWrapper
	public Collection<UserTO> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserTO> users) {
		this.users = users;
	}
	

}
