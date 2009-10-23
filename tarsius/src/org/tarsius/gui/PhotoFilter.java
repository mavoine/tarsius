package org.tarsius.gui;

import org.tarsius.bean.Tag;
import org.tarsius.i18n.I18n;
import org.tarsius.persistence.dao.criteria.PhotoCriteria;

public class PhotoFilter {
	
	private Tag tag = null;
	
	public PhotoFilter() {
	}
	
	public void reset(){
		tag = null;
	}
	
	public boolean isEmpty(){
		return tag == null;
	}
	
	public void setTag(Tag tag){
		this.tag = tag;
	}
	
	public PhotoCriteria getPhotoCriteria(){
		PhotoCriteria criteria = new PhotoCriteria();
		if(tag != null){
			criteria.addTagRestriction(tag);
		}
		return criteria;
	}
	
	public String getDescription(){
		String desc = "";
		if(tag != null){
			desc += I18n.translate("Tag:") + " " + tag.getName();
		}
		return desc;
	}

}
