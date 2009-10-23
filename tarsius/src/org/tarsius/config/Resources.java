package org.tarsius.config;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Resources {
	
	private static Log log = LogFactory.getLog(Resources.class);
	private static Resources instance = null;
	
	private HashMap<String,Image> imageMap = null;
	
	private Resources() {
		imageMap = new HashMap<String,Image>();
	}
	
	public static Resources getInstance(){
		if(instance == null){
			instance = new Resources();
		}
		return instance;
	}
	
	private Image getImage(String filename) {
		
		// if the image is not yet in the map, load it and store it
		if (!imageMap.containsKey(filename)) {
			Image image = null;
			try {
				image = ImageIO.read(getClass().getResource("icon/" + filename));
			} catch (Exception e) {
				log.warn("Image not found: " + filename);
				// create a "blank" image
				// this may not be pretty, but it prevents the app from crashing
				image = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
			} finally {
				imageMap.put(filename, image);
			}
		}
		
		return imageMap.get(filename);
	}
	
	public Image getTarsiusIcon(){
		return getImage("tarsius.png");
	}
	
	public Image getNotFoundIcon(){
		return getImage("notfound.png");
	}
	
//	public Icon getAddTagIcon(){
//		return addTagIcon;
//	}
//	
//	public Icon getRenameTagIcon(){
//		return renameTagIcon;
//	}
//	
//	public Icon getDeleteTagIcon(){
//		return deleteTagIcon;
//	}

}
