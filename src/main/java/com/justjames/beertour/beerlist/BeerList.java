package com.justjames.beertour.beerlist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.justjames.beertour.user.User;

@Entity
@Table(name="BEER_LIST")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@XmlRootElement(name="beerList")
public class BeerList implements Serializable {

	private static final long serialVersionUID = 2069265856735481767L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="BEER_LIST_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name="USER_PK")
	private User user;
	
	@Column(nullable=false)
	private String startDate;
	
	@Column
	private String finishDate;
	
	@Column
	private Integer listNumber;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Collection<BeerOnList> beerOnList;
	
	@Version
	private long version;
	
	public BeerList() {
			beerOnList = new ArrayList<BeerOnList>();
	}

	public void addBeer(BeerOnList beer) {
		beerOnList.add(beer);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public Collection<BeerOnList> getBeerOnList() {
		return beerOnList;
	}

	public void setBeerOnList(Collection<BeerOnList> beerOnList) {
		this.beerOnList = beerOnList;
	}

	public Integer getListNumber() {
		return listNumber;
	}

	public void setListNumber(Integer listNumber) {
		this.listNumber = listNumber;
	}
	
	@Transient
	public boolean isActiveList() {
		return this.getFinishDate() == null ? true : false; 
	}
	
	@Transient
	public Integer getTotalBeersOnList() {
		return getBeerOnList().size();
	}
	
	@Transient
	public Integer getNumberOrderedOnList() {
		Integer numberOrdered = 0;
		for (BeerOnList b : getBeerOnList()) {
			if (b.isOrdered()) {
				numberOrdered++;
			}
		}
		return numberOrdered;
	}
	
	@Transient
	public Integer getNumberCompletedOnList() {
		Integer numberCompleted = 0;
		for (BeerOnList b : getBeerOnList()) {
			if (b.isCompleted()) {
				numberCompleted++;
			}
		}
		return numberCompleted;
	}
	
	@Transient
	public Integer getNumberRemaining() {
		return this.getTotalBeersOnList() - getNumberCompletedOnList();
	}
	
	@Transient
	public Float getListProgressPct() {
		Float listProgress;
		try {
			listProgress = (float) 
					getNumberOrderedOnList() / getTotalBeersOnList() * 100;
		} catch (ArithmeticException e) {
			listProgress = 0f;
		}
		return listProgress;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((listNumber == null) ? 0 : listNumber.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		BeerList other = (BeerList) obj;
		if (listNumber == null) {
			if (other.listNumber != null)
				return false;
		} else if (!listNumber.equals(other.listNumber))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format(
				"BeerList [id=%s, list#=%d userId=%s, startDate=%s, beerTotal=%d beerOrdered=%d beerCompleted=%d beerRemaining=%d ]", 
				id, getListNumber(), user.getEmail(), startDate, getTotalBeersOnList(), getNumberOrderedOnList(), getNumberCompletedOnList(), getNumberRemaining() );
	}
	
	

}
