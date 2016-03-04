package com.justjames.beertour.beerlist;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.justjames.beertour.beer.Beer;

@Entity
@Table(name="BEER_ON_LIST")
public class BeerOnList implements Serializable {
	
	private static final long serialVersionUID = -201335284212431819L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="BEER_ON_LIST_ID")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name="BEER_ID")
	private Beer beer;

	@ManyToOne
	@JoinColumn(name="BEER_LIST_ID")
	@XmlTransient
	private BeerList beerList;

	@Column
	private boolean ordered; 
	
	@Column
	private String orderedDate;
	
	@Column
	private boolean completed;
	
	@Column
	private String	completedDate;
	
	@Column
	private String completedBy;
	
	public BeerOnList() { }

	public BeerOnList(Beer beer) {
		this.beer = beer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setList(BeerList list) {
		this.beerList = list;
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	public String getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(String orderedDate) {
		this.orderedDate = orderedDate;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}

	public String getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}

	public Beer getBeer() {
		return beer;
	}

	public void setBeer(Beer beer) {
		this.beer = beer;
	}

	

}
