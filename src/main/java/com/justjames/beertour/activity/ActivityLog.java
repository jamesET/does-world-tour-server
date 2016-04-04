package com.justjames.beertour.activity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for the user activity feed 
 * 
 * @author james
 *
 */
@Entity
@Table
public class ActivityLog implements Serializable {
	
	private static final long serialVersionUID = -5575193511473824215L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="logId")
	private Long id;

	@Column(nullable=false)
	private Date time;

	@Column
	private Integer userId;
	
	@Column
	private Integer beerId;
	
	@Column
	private Integer listNbr;
	
	@Column
	private Float listProgressPct;
	
	@Column
	private ActivityType activityType;
	
	@Column
	private String messageHtml;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBeerId() {
		return beerId;
	}

	public void setBeerId(Integer beerId) {
		this.beerId = beerId;
	}

	public Integer getListNbr() {
		return listNbr;
	}

	public void setListNbr(Integer listNbr) {
		this.listNbr = listNbr;
	}

	public Float getListProgressPct() {
		return listProgressPct;
	}

	public void setListProgressPct(Float listProgressPct) {
		this.listProgressPct = listProgressPct;
	}
	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public String getMessageHtml() {
		return messageHtml;
	}

	public void setMessageHtml(String messageHtml) {
		this.messageHtml = messageHtml;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		ActivityLog other = (ActivityLog) obj;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}


}
