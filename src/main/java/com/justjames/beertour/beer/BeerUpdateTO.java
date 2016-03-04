package com.justjames.beertour.beer;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BeerUpdateTO implements Serializable {
	
	private static final long serialVersionUID = 8712230897029552758L;

	private Integer id;
	private String name;
	private String brewery;
	private String country;
	private String region;
	private String style;
	private Float abv;
	private Float ibu;
	private Float oz; 
	private boolean discontinued;
	private boolean outOfStock;
	
	@XmlElement
	public Integer getId() {
		return id;
	}
	@XmlElement
	public String getName() {
		return name;
	}
	@XmlElement
	public String getBrewery() {
		return brewery;
	}
	@XmlElement
	public String getCountry() {
		return country;
	}
	@XmlElement
	public String getRegion() {
		return region;
	}
	@XmlElement
	public String getStyle() {
		return style;
	}
	@XmlElement
	public Float getAbv() {
		return abv;
	}
	@XmlElement
	public Float getIbu() {
		return ibu;
	}
	@XmlElement
	public Float getOz() {
		return oz;
	}
	@XmlElement
	public boolean isDiscontinued() {
		return discontinued;
	}
	@XmlElement
	public boolean isOutOfStock() {
		return outOfStock;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setBrewery(String brewery) {
		this.brewery = brewery;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public void setAbv(Float abv) {
		this.abv = abv;
	}
	public void setIbu(Float ibu) {
		this.ibu = ibu;
	}
	public void setOz(Float oz) {
		this.oz = oz;
	}
	public void setDiscontinued(boolean discontinued) {
		this.discontinued = discontinued;
	}
	public void setOutOfStock(boolean outOfStock) {
		this.outOfStock = outOfStock;
	}
	
	
	

}
