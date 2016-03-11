package com.justjames.beertour.activity;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ActivityFeed implements Serializable {
	
	private static final long serialVersionUID = -1749588020966215012L;

	// Page number
	private Integer pageNumber;
	
	// Has previous page
	private boolean hasPreviousPage;
	
	// Has next page
	private boolean hasNextPage;
	
	// Collection of activities in Page
	private ArrayList<ActivityLogTO> activities;

	@XmlElement
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	@XmlElement
	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	@XmlElement
	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	@XmlElement
	public ArrayList<ActivityLogTO> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<ActivityLogTO> activities) {
		this.activities = activities;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pageNumber == null) ? 0 : pageNumber.hashCode());
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
		ActivityFeed other = (ActivityFeed) obj;
		if (pageNumber == null) {
			if (other.pageNumber != null)
				return false;
		} else if (!pageNumber.equals(other.pageNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityFeed [pageNumber=" + pageNumber + ", hasPreviousPage="
				+ hasPreviousPage + ", hasNextPage=" + hasNextPage
				+ ", " + activities.size() + " activities]";
	}

	
	
	

}
