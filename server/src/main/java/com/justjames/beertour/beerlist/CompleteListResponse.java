package com.justjames.beertour.beerlist;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.justjames.beertour.BaseResponse;

@XmlRootElement
public class CompleteListResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	private Collection<BeerToComplete> beers = new ArrayList<BeerToComplete>();
	
	public CompleteListResponse(Collection<BeerToComplete> beers ) {
		for(BeerToComplete beer : beers) {
			this.beers.add(beer);
		}
	}
	
	@XmlElementWrapper(name="beers")
	public Collection<BeerToComplete> getBeers() {
		return beers;
	}


}
