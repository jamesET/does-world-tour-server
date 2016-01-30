package com.justjames.beertour.beer;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BEER")
public class Beer implements Serializable {
	
	private static final long serialVersionUID = 1521756916080782005L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="BEER_ID")
	private Integer id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false,length=80)
	private String brewery;
	
	@Column private String country;
	
	@Column
	private String region;
	
	@Column private Float abv;
	
	@Column private Float ibu;
	
	@Column private Float oz; // ounces
	
	@Column private boolean discontinued;
	
	@Column(name="out") private boolean outOfStock;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrewery() {
		return brewery;
	}

	public void setBrewery(String brewery) {
		this.brewery = brewery;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Float getAbv() {
		return abv;
	}

	public void setAbv(Float abv) {
		this.abv = abv;
	}

	public Float getIbu() {
		return ibu;
	}

	public void setIbu(Float ibu) {
		this.ibu = ibu;
	}

	public Float getOz() {
		return oz;
	}

	public void setOz(Float oz) {
		this.oz = oz;
	}

	public boolean isDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(boolean discontinued) {
		this.discontinued = discontinued;
	}

	public boolean isOutOfStock() {
		return outOfStock;
	}

	public void setOutOfStock(boolean outOfStock) {
		this.outOfStock = outOfStock;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brewery == null) ? 0 : brewery.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Beer other = (Beer) obj;
		if (brewery == null) {
			if (other.brewery != null)
				return false;
		} else if (!brewery.equals(other.brewery))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	

}
