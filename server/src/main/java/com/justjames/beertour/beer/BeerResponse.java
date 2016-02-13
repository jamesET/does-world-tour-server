package com.justjames.beertour.beer;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.justjames.beertour.BaseResponse;

@XmlRootElement
public class BeerResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	private Collection<Beer> beers;
	
	public BeerResponse() { }
	
	public BeerResponse(Collection<Beer> beers) {
		this.beers = beers;
	}

	public BeerResponse(Beer b) {
		this.beers = new ArrayList<Beer>();
		this.beers.add(b);
	}

	@XmlElementWrapper
	public Collection<Beer> getBeers() {
		return beers;
	}

	public void setBeers(Collection<Beer> beers) {
		this.beers = beers;
	}
	
	
}
