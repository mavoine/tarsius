package org.tarsius;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.imaging.ThumbnailsFactory;
import org.tarsius.persistence.dao.PhotoDAO;
import org.tarsius.persistence.dao.criteria.PhotoCriteria;


public class ThumbnailMachine {
	
	private static Log log = LogFactory.getLog(ThumbnailMachine.class);
	
	public static void main(String[] args) throws Exception {
		ThumbnailMachine tm = new ThumbnailMachine();
		tm.run(args[0]);
	}
	
	private void run(String galleryLocation) throws Exception {
		Gallery gallery = new Gallery();
		gallery.openGallery(galleryLocation);
		List<Photo> allPhotos = PhotoDAO.getInstance().getPhotos(new PhotoCriteria());
		for(Photo photo : allPhotos){
			try {
				ThumbnailsFactory.getInstance().getThumbnailNow(gallery.getPhotosPath(), photo.getPath());
			} catch(Exception ex){
				log.error("Failed to create thumbnail", ex);
			}
		}
	}

}
