package org.tarsius.imaging;

import java.awt.Image;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.util.caching.Cache;
import org.tarsius.util.caching.CacheFactory;

public class ImageCache {
	
	private static Log log = LogFactory.getLog(ImageCache.class);
	private static ImageCache instance = null;
	
	private Cache thumbnailsCache = null;
	
	private ImageCache() {
		log.info("Creating thumbnails cache");
		thumbnailsCache = CacheFactory.createCache("thumbnails");
	}
	
	public static ImageCache getInstance(){
		if(instance == null){
			instance = new ImageCache();
		}
		return instance;
	}
	
	public Image getThumbnail(String pathToOriginal){
		return (Image)thumbnailsCache.get(pathToOriginal);
	}
	
	public void putThumbnail(String pathToOriginal, Image image){
		thumbnailsCache.put(pathToOriginal, image);
	}

}
