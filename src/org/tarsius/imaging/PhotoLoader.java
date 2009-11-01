package org.tarsius.imaging;

import java.io.File;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;
import org.tarsius.bean.Photo;

public class PhotoLoader {
	
	private static Log log = LogFactory.getLog(PhotoLoader.class);

	public Photo load(File file) {

		Photo photo = new Photo();

		String path = file.getAbsolutePath();
		boolean isRelativeToGallery = path.startsWith(Context.getGallery().getPhotosPath());
		if(isRelativeToGallery){
			// strip gallery path to form relative path
			path = file.getPath().substring(Context.getGallery().getPhotosPath().length(), path.length());
		} // else leave the path in its absolute form
		photo.setPath(path);
		photo.setPathRelToGallery(isRelativeToGallery);

		log.debug("Loading photo: " + path);

		MetadataInspector metadataInspector = new MetadataInspector(file);
		Date dateShot = metadataInspector.getDateShot();
		// if date is NOT retrievable from exif
		if(dateShot == null){
			// use the file last modification date
			dateShot = new Date(file.lastModified());
		}
		photo.setDate(dateShot);

		return photo;
	}

}
