package com.justjames.beertour.beerlist;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.justjames.beertour.BaseResponse;
import com.justjames.beertour.user.UserTO;

@XmlRootElement(name="beerList")
public class MyBeerListResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private UserTO user;
	private String startDate;
	private String finishDate;
	private Integer listNumber;
	private Integer totalBeersOnList;
	private Integer numberOrderedOnList;
	private Integer numberCompletedOnList;
	private Integer numberRemaining;
	private Float	listProgressPct;
	private Integer numberNotConfirmed;
	private Collection<BeerOnList> drinkList = new ArrayList<BeerOnList>();
	private Collection<BeerOnList> orderedList = new ArrayList<BeerOnList>();
	private Collection<BeerOnList> completedList = new ArrayList<BeerOnList>();
	
	public MyBeerListResponse() { };
	
	public MyBeerListResponse(BeerList beerList) {
		this.setId(beerList.getId());
		this.setUser(new UserTO(beerList.getUser()));
		this.setStartDate(beerList.getStartDate());
		this.setFinishDate(beerList.getFinishDate());
		this.setListNumber(beerList.getListNumber());
		this.setTotalBeersOnList(beerList.getTotalBeersOnList());
		this.setNumberOrderedOnList(beerList.getNumberOrderedOnList());
		this.setNumberCompletedOnList(beerList.getNumberCompletedOnList());
		this.setNumberRemaining(beerList.getNumberRemaining());
		this.setListProgressPct(beerList.getListProgressPct());
		this.setNumberNotConfirmed(beerList.getNumberNotConfirmed());
		
		for (BeerOnList beer : beerList.getBeerOnList()) {
			if (beer.isCompleted()) {
				this.completedList.add(beer);
			} else if (beer.isOrdered()){
				this.orderedList.add(beer);
			} else {
				this.drinkList.add(beer);
			}
		}
		
	}

	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public UserTO getUser() {
		return user;
	}

	public void setUser(UserTO user) {
		this.user = user;
	}

	@XmlElement
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@XmlElement
	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	@XmlElement
	public Integer getListNumber() {
		return listNumber;
	}

	public void setListNumber(Integer listNumber) {
		this.listNumber = listNumber;
	}

	@XmlElement
	public Integer getTotalBeersOnList() {
		return totalBeersOnList;
	}

	public void setTotalBeersOnList(Integer totalBeersOnList) {
		this.totalBeersOnList = totalBeersOnList;
	}

	@XmlElement
	public Integer getNumberOrderedOnList() {
		return numberOrderedOnList;
	}

	public void setNumberOrderedOnList(Integer numberOrderedOnList) {
		this.numberOrderedOnList = numberOrderedOnList;
	}

	@XmlElement
	public Integer getNumberCompletedOnList() {
		return numberCompletedOnList;
	}

	public void setNumberCompletedOnList(Integer numberCompletedOnList) {
		this.numberCompletedOnList = numberCompletedOnList;
	}

	@XmlElement
	public Integer getNumberRemaining() {
		return numberRemaining;
	}

	public void setNumberRemaining(Integer numberRemaining) {
		this.numberRemaining = numberRemaining;
	}
	
	@XmlElement
	public Float getListProgressPct() {
		return listProgressPct;
	}
	
	public void setListProgressPct(Float listProgressPct) {
		this.listProgressPct = listProgressPct;
	}
	
	@XmlElement
	public Integer getNumberNotConfirmed() {
		return this.numberNotConfirmed;
	}
	
	public void setNumberNotConfirmed(Integer numberNotConfirmed) {
		this.numberNotConfirmed = numberNotConfirmed;
	}

	@XmlElementWrapper(name="drinkList")
	public Collection<BeerOnList> getDrinkList() {
		return drinkList;
	}

	public void setDrinkList(Collection<BeerOnList> drinkList) {
		this.drinkList = drinkList;
	}

	@XmlElementWrapper(name="orderedList")
	public Collection<BeerOnList> getOrderedList() {
		return orderedList;
	}

	public void setOrderedList(Collection<BeerOnList> orderedList) {
		this.orderedList = orderedList;
	}

	@XmlElementWrapper(name="completedList")
	public Collection<BeerOnList> getCompletedList() {
		return completedList;
	}

	public void setCompletedList(Collection<BeerOnList> completedList) {
		this.completedList = completedList;
	}


}
