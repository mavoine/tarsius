package org.tarsius.imaging;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;

public class ThumbnailsDefault extends Thumbnails {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(ThumbnailsDefault.class);

	private static final Integer THUMB_MAX_WIDTH  = 256;
	private static final Integer THUMB_MAX_HEIGHT = 256;
	private static final String  THUMB_EXTENTION = "png";

	protected ThumbnailsDefault(){
	}

	@Override
	public Integer getThumbnailMaxHeight() {
		return THUMB_MAX_HEIGHT;
	}

	@Override
	public Integer getThumbnailMaxWidth() {
		return THUMB_MAX_WIDTH;
	}

	@Override
	public void deleteThumbnail(String pathToOriginal) {
		if(this.permanentCachingEnabled){
			// TODO implement
		}
	}

	@Override
	protected Image makeThumbnail(String rootDirectory, String relativePathToImage) throws Exception {
		
		BufferedImage thumbnailImage = null;
		
		String thumbnailDirPath = resolveThumbnailDirectory(relativePathToImage);
		String thumbnailFilename = resolveThumbnailFilename(relativePathToImage);
		File thumbnailFile = new File(thumbnailDirPath + thumbnailFilename);
		
		if(this.permanentCachingEnabled && thumbnailFile.exists()){
			try {
				thumbnailImage = ImageIO.read(thumbnailFile);
				// TODO investigate use of ImageIO.setUseCache()
			} catch (IOException e) {
				throw new IOException("Unable to read thumbnail file", e);
			}
		} else {
			try {
				thumbnailImage = ImageOperationUtil.makeThumbnail(new File(rootDirectory + relativePathToImage),
						getThumbnailMaxWidth(), getThumbnailMaxHeight());
			} catch (IOException e){
				throw new IOException("Unable to create the thumbnail image", e);
			}
			if(this.permanentCachingEnabled){
				try {
					// create empty file, creating directory tree if it does not exist
					File thumbnailDir = new File(thumbnailDirPath);
					if(!thumbnailDir.exists()){
						FileUtils.forceMkdir(thumbnailDir);
					}
					ImageIO.write(thumbnailImage, THUMB_EXTENTION, thumbnailFile);
				} catch (IOException e){
					// if saving the thumbnail failed, it's OK; we log the error and return the thumbnail
					log.error("Unable to write thumbnail file", e);
				}
			}
		}
		
		return thumbnailImage;
	}
	
	private String resolveThumbnailDirectory(String relativePathToImage) {
		if (!Context.getGallery().isOpen()) {
			throw new RuntimeException("No gallery opened");
		}
		String thumbnailpath = Context.getGallery().getThumbsPath()
				+ relativePathToImage.substring(0, relativePathToImage.lastIndexOf(File.separator) + 1);
		return thumbnailpath;
	}

	private String resolveThumbnailFilename(String relativePathToImage) {
		if (!Context.getGallery().isOpen()) {
			throw new RuntimeException("No gallery opened");
		}
		return relativePathToImage.substring(relativePathToImage.lastIndexOf(File.separator) + 1) 
			+ "." + THUMB_EXTENTION;
	}

}
