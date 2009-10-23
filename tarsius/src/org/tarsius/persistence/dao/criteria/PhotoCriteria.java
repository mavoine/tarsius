package org.tarsius.persistence.dao.criteria;

import java.util.Date;

import org.tarsius.bean.Tag;

public class PhotoCriteria {
	
	// tag restriction
	private boolean tagRestriction = false;
	private Integer tagId = null;
	
	// date restriction
	private boolean timeRestriction = false;
	private Date startDate = null;
	private Date endDate = null;
	
	public void addTimeRestriction(Date startDate, Date endDate){
		this.timeRestriction = true;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public boolean getHasTimeRestriction() {
		return timeRestriction;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void addTagRestriction(Tag tag){
		this.tagRestriction = true;
		this.tagId = tag.getId();
	}
	
	public boolean getHasTagRestriction() {
		return tagRestriction;
	}
	
	public Integer getTagId() {
		return tagId;
	}
	
}
