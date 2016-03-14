package com.justjames.beertour.activity;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.justjames.beertour.Utils;
import com.justjames.beertour.beer.Beer;
import com.justjames.beertour.user.User;

@XmlRootElement
public class ActivityLogTO implements Serializable {
	
	private static final long serialVersionUID = -1567122223840714683L;

	private Long id;
	private String time;
	private String name;
	private Beer beer;
	private Integer listNbr;
	private Float listProgressPct;
	
	public ActivityLogTO(ActivityLog al,User u,Beer b) {
		setId(al.getId());
		setTime(Utils.utcDateStr(al.getTime()));
		setName(u.getDisplayName());
		setBeer(b);
		setListNbr(al.getListNbr());
		setListProgressPct(al.getListProgressPct());

	}

	@XmlElement
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public Beer getBeer() {
		return beer;
	}

	public void setBeer(Beer beer) {
		this.beer = beer;
	}

	@XmlElement
	public Integer getListNbr() {
		return listNbr;
	}

	public void setListNbr(Integer listNbr) {
		this.listNbr = listNbr;
	}

	@XmlElement
	public Float getListProgressPct() {
		return listProgressPct;
	}

	public void setListProgressPct(Float listProgressPct) {
		this.listProgressPct = listProgressPct;
	}

	

}
