package org.tarsius.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Preferences {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(Preferences.class);
	
	// default values
	private List<String> recentGalleries = null;
	private Locale locale = null;
	
	public Preferences() {
		// set default values
		recentGalleries = new ArrayList<String>();
		locale = Locale.ENGLISH;
	}
	
	public List<String> getRecentGalleries(){
		return this.recentGalleries;
	}
	
	public void setRecentGalleries(List<String> recentGalleries){
		this.recentGalleries = recentGalleries;
	}
	
	public void addRecentGallery(String recentGallery){
		if(!this.recentGalleries.contains(recentGallery)){
			this.recentGalleries.add(recentGallery);
		}
	}
	
	public void removeRecentGallery(String recentGallery){
		this.recentGalleries.remove(recentGallery);
	}
	
	public Locale getLocale(){
		return this.locale;
	}
	
	public void setLocale(Locale locale){
		this.locale = locale;
	}
	
}
