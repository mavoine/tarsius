package org.tarsius.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.imaging.ThumbnailsFactory;
import org.tarsius.persistence.dao.PhotoDAO;

public class DeletePhotoTask {
	
	private static Log log = LogFactory.getLog(DeletePhotoTask.class);
	
	private boolean deleteFromDrive = false;
	private List<Photo> photos = null;

	public DeletePhotoTask(List<Photo> photos, boolean deleteFromDrive) {
		this.photos = photos;
		this.deleteFromDrive = deleteFromDrive;
	}
		
	public void execute() throws TaskException {
		try {
			List<String> photosToDelete = new ArrayList<String>();
			// TODO transaction
			for (Photo photo : photos) {
				log.debug("Delete photo: " + photo);
				photosToDelete.add(photo.getAbsolutePath());
				PhotoDAO.getInstance().deletePhoto(photo);
			}
			if(deleteFromDrive){
				for (String path : photosToDelete) {
					// delete the actual photo
					FileUtils.deleteQuietly(new File(path));
					// delete its thumbnail
					ThumbnailsFactory.getInstance().deleteThumbnail(path);
					// TODO prune empty directories
				}
			}
		} catch (Exception e) {
			throw new TaskException("Failed to delete photos", e);
		}		
	}

}
