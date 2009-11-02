package org.tarsius.imaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThumbnailsFactory {
	
	private static Log log = LogFactory.getLog(ThumbnailsFactory.class);
	
	private static Thumbnails instance = null;
	
	@SuppressWarnings("unchecked")
	public static Thumbnails getInstance(){
		if(instance == null){
			create();
		}
		return instance;
	}
	
	public static void init(){
		create();
	}
	
	private static void create(){
		String className = System.getProperty("tarsius.thumbsclass",
				"org.tarsius.imaging.Thumbnails"
						+ System.getProperty("os.name", "Default"));
		Class thumbnailsClass;
		try {
			thumbnailsClass = Class.forName(className);
			instance = (Thumbnails)thumbnailsClass.newInstance();
		} catch (Exception e) {
			log.error("Error initializing platform-specific class: " 
					+ className + ", falling back to default", e);
			instance = new ThumbnailsDefault();
		}
	}

}
