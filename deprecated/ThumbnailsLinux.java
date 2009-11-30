package org.tarsius.imaging;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.util.PathUtil;

public class ThumbnailsLinux extends Thumbnails {
	
	// TODO make sure no file is created in the thumbs dir if the thumb creation fails
	
	private static Log log = LogFactory.getLog(ThumbnailsLinux.class);
	
	private static final Integer THUMB_MAX_WIDTH  = 256;
	private static final Integer THUMB_MAX_HEIGHT = 256;
	
	protected ThumbnailsLinux(){
	}
	
	protected String getThumbnailCachePath(){
		return System.getProperty("user.home") 
			+ File.separator + ".thumbnails" + File.separator + "large";
	}
	
	public Image getThumbnail(String pathToOriginal) {
		
		if(log.isTraceEnabled()){
			log.trace("Loading thumbnail for path: " + pathToOriginal);
		}

		// try to load from memory thumbnails cache
		Image imageFromCache = ImageCache.getInstance().getThumbnail(pathToOriginal);
		if(imageFromCache != null){
			return imageFromCache;
		}

		// create path to thumbnail
		String thumbnailPath = getPathToThumbnail(pathToOriginal);
		if(log.isTraceEnabled()){
			log.trace("md5 path: " + thumbnailPath);
		}
		
		// try to load from disk thumbnails cache
		BufferedImage thumbnailImage = null;
		File thumbnailFile = new File(thumbnailPath);
		if(!thumbnailFile.exists()){
			log.debug("Creating thumbnail for image [" + pathToOriginal + "]");
			thumbnailImage = createThumbnail(pathToOriginal, thumbnailFile);
		} else {
			try {
				// check validity of the thumbnail (Thumb::MTime)
				// if invalid or date tag unavailable, recreate
//				if(metadata == null){
//					thumbnailImage = recreateThumbnail(pathOriginal, thumbnailFile);
//				}
				// TODO recreate thumbnail if out of date
				thumbnailImage = ImageIO.read(thumbnailFile);
			} catch (IOException e) {
				log.error("Could not read image [" + thumbnailPath + "]", e);
				// TODO load a default image into thumbnailImage
			}
		}
		
		ImageCache.getInstance().putThumbnail(pathToOriginal, thumbnailImage);
		
		return thumbnailImage;
	}
	
	private String getPathToThumbnail(String pathToOriginal) {
		return getThumbnailCachePath() + File.separator
				+ PathUtil.md5Path("file://" + pathToOriginal) + ".png";
	}
	
	private void saveThumbnail(BufferedImage thumbnailImage, File thumbnailFile){
		try {
			ImageIO.write(thumbnailImage, "png", thumbnailFile);
		} catch (IOException e){
			log.error("Error writing thumbnail to drive", e);
		}
	}
	
	private BufferedImage createThumbnail(String pathOriginal, File thumbnailFile){
		File originalFile = new File(pathOriginal);
		// TODO read exif thumbnail and use if suitable
		BufferedImage thumbnailImage = prepareThumbnail(originalFile);
		if(permanentCachingEnabled){
			// TODO add proper metadata to thumbnail
//			Date lastModification = new Date(originalFile.lastModified());
			saveThumbnail(thumbnailImage, thumbnailFile);
		}
		return thumbnailImage;
	}
	
//	private BufferedImage recreateThumbnail(String pathOriginal, 
//			File thumbnailFile) throws IOException{
//		thumbnailFile.delete();
//		thumbnailFile.createNewFile();
//		BufferedImage thumbnailImage = createThumbnail(pathOriginal, thumbnailFile);
//		return thumbnailImage;
//	}
	
	@Override
	public void deleteThumbnail(String pathToOriginal) {
		File file = new File(getPathToThumbnail(pathToOriginal));
		FileUtils.deleteQuietly(file);
	}

	@Override
	public Integer getThumbnailMaxHeight() {
		return THUMB_MAX_HEIGHT;
	}

	@Override
	public Integer getThumbnailMaxWidth() {
		return THUMB_MAX_WIDTH;
	}
	
	
}
