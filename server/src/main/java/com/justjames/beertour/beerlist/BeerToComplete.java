package com.justjames.beertour.beerlist;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.justjames.beertour.user.User;
import com.justjames.beertour.user.UserTO;

@XmlRootElement(name="beerToComplete")
public class BeerToComplete {
	
	private Integer beerListId;
	private UserTO user;
	BeerOnList beer;
	
	public BeerToComplete(User u,Integer beerListId,BeerOnList beerOnList) {
		this.beerListId = beerListId; 
		this.user = new UserTO(u); 
		this.beer = beerOnList;
	}

	@XmlElement
	public Integer getBeerListId() {
		return beerListId;
	}

	public void setBeerListId(Integer beerListId) {
		this.beerListId = beerListId;
	}

	@XmlElement
	public UserTO getUser() {
		return user;
	}

	public void setUser(UserTO user) {
		this.user = user;
	}

	@XmlElement(name="beerOnList")
	public BeerOnList getBeer() {
		return beer;
	}

	public void setBeer(BeerOnList beer) {
		this.beer = beer;
	}
	

}
