package com.justjames.beertour.beerlist;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.justjames.beertour.BaseResponse;

@XmlRootElement
public class BeerListResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	private Collection<BeerList> beerLists;
	
	public BeerListResponse() { 
		this.beerLists = new ArrayList<BeerList>();
	};
	
	public BeerListResponse(BeerList beerList) {
		this.beerLists = new ArrayList<BeerList>();
		this.beerLists.add(beerList);
	}
	
	public BeerListResponse(Collection<BeerList> beerLists) {
		this.beerLists = beerLists;
	}

	@XmlElementWrapper
	public Collection<BeerList> getBeerLists() {
		return beerLists;
	}

	public void setBeerLists(Collection<BeerList> beerLists) {
		this.beerLists = beerLists;
	}

	
	
	

}
