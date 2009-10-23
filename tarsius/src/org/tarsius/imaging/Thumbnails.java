package org.tarsius.imaging;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Thumbnails {
	
	private static Log log = LogFactory.getLog(Thumbnails.class);
	
	public abstract Image getThumbnail(String pathToOriginal);
	public abstract Integer getThumbnailMaxWidth();
	public abstract Integer getThumbnailMaxHeight();
	public abstract void deleteThumbnail(String pathToOriginal);
	
	// TODO is this useful?
	protected boolean permanentCachingEnabled = true;
	protected boolean temporaryCachingEnabled = true;
	
	public void enablePermanentCaching(){
		permanentCachingEnabled = true;
	}
	
	public void enableTemporaryCaching(){
		temporaryCachingEnabled = true;
	}
	
	public BufferedImage prepareThumbnail(File originalFile){

		log.trace("Begin preparing thumbnail");
		
		BufferedImage thumbnailImage = null;
//		MetadataInspector mi = new MetadataInspector(originalFile);
		
		try {
			
			thumbnailImage = ImageOperationUtil.makeThumbnail(originalFile,
					getThumbnailMaxWidth(), getThumbnailMaxHeight());
			
//			BufferedImage originalImage = ImageIO.read(originalFile);
//			Integer width = originalImage.getWidth();
//			Integer height = originalImage.getHeight();
//
//			log.trace("Calculate clipping");
//			Dimension clip = ClipUtil.clip(new Dimension(width, height), 
//					new Dimension(getThumbnailMaxWidth(), getThumbnailMaxHeight()));
//			
//			log.trace("Make a scaled-down copy of the image");
//			Image scaledImage = 
//				originalImage.getScaledInstance(clip.width, clip.height, Image.SCALE_SMOOTH);
//
//			log.trace("Perform rotation if necessary");
//			Integer thumbWidth = null;
//			Integer thumbHeight = null;
//			Orientation orientation = mi.getOrientation();
//			AffineTransform transform = new AffineTransform();
//			if(orientation == Orientation.TOP_LEFT){
//				thumbHeight = clip.height;
//				thumbWidth = clip.width;
//			} else if(orientation == Orientation.LEFT_BOTTOM){
//				thumbHeight = clip.width;
//				thumbWidth = clip.height;
//				transform.translate(0, thumbHeight);
//				transform.rotate(Math.toRadians(-90.0));
//			} else {
//				thumbHeight = clip.height;
//				thumbWidth = clip.width;
//			}
//			// TODO support other orientations
//			thumbnailImage = new BufferedImage(thumbWidth, thumbHeight, 
//					BufferedImage.TYPE_INT_RGB);
//			thumbnailImage.createGraphics().drawImage(scaledImage, transform, null);

			log.trace("Done preparing thumbnail");

		} catch (IOException e) {
			log.error("Unable to read image", e);
			// TODO manage exception
		} catch (Exception e) {
			log.error("Unexpected exception", e);
		}
		return thumbnailImage;
	}
	
}
