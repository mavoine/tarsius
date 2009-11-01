package org.tarsius.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.util.StringUtil;

public class PreferencesPersister {
	
	private static Log log = LogFactory.getLog(PreferencesPersister.class);
	
	public PreferencesPersister(){
	}
	
	public Preferences load(InputStream inputStream){
		Preferences preferences = new Preferences();
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException ioe){
			log.error("Error loading the preferences, default values will be used.", 
				ioe);
		}
		
		// locale
		String localeStr = properties.getProperty("Locale");
		if(localeStr != null){
			preferences.setLocale(new Locale(localeStr));
		}
		
		// recent galleries
		String recentGalleriesStr = properties.getProperty("RecentGalleries");
		if(recentGalleriesStr != null){
			List<String> recentGalleries = 
				StringUtil.stringToList(recentGalleriesStr, ";");
			preferences.setRecentGalleries(recentGalleries);
		}
		
		return preferences;
	}
	
	public void save(Preferences preferences, OutputStream outputStream){
		Properties properties = new Properties();
		
		// locale
		Locale locale = preferences.getLocale();
		properties.put("Locale", locale.getLanguage());
		
		// recent galleries
		List<String> recentGalleries = preferences.getRecentGalleries();
		properties.put("RecentGalleries", 
				StringUtil.listToString(recentGalleries, ";"));
		
		try {
			properties.store(outputStream, "User preferences");
		} catch (IOException ioe){
			log.error("Error saving the preferences", ioe);
		}
	}

}
