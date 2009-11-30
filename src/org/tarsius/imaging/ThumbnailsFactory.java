package org.tarsius.imaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThumbnailsFactory {
	
	private static Log log = LogFactory.getLog(ThumbnailsFactory.class);
	
	private static Thumbnails instance = null;
	
	@SuppressWarnings("unchecked")
	public static Thumbnails getInstance(){
		
		if(instance == null){
			Class thumbnailsClass = null;
			
			String className = System.getProperty("tarsius.thumbsclass");
			if(className != null){
				try {
					thumbnailsClass = Class.forName(className);
				} catch (Exception e) {
					log.error("Error loading custom Thumbnails provider class: " 
							+ className + ", falling back to default", e);
				}
			} else {
				thumbnailsClass = ThumbnailsDefault.class;
			}
			
			try {
				instance = (Thumbnails)thumbnailsClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Failed to instanciate Thumbnails provider", e);
			}
		}
		
		return instance;
	}
	
}
