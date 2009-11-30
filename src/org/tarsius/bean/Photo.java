package org.tarsius.bean;

import java.util.Date;
import java.util.List;

public class Photo {
	
	private Integer id = -1;
	private String path = null;
	private Boolean isPathRelative = null;
	private Date date = null;
	private List<Tag> tags = null;
	
	public Photo() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getPathRelToGallery() {
		return isPathRelative;
	}

	public void setPathRelToGallery(Boolean isPathRelative) {
		this.isPathRelative = isPathRelative;
	}
	
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public List<Tag> getTags() {
		return tags;
	}
	
	@Override
	public boolean equals(Object otherObj) {
		if(otherObj instanceof Photo && otherObj != null){
			Photo otherPhoto = (Photo)otherObj;
			if(otherPhoto.id != null && this.id != null){
				return this.id.intValue() == otherPhoto.id.intValue();
			}
			return false;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id != null ? this.id.intValue() : 0;
	}
	
	@Override
	public String toString() {
		return "[Photo id = " + this.id + ", path = " + this.path + "]";
	}
	
}
