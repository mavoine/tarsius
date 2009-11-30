package org.tarsius.task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;
import org.tarsius.bean.Photo;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.gui.component.ProgressDialog;
import org.tarsius.i18n.I18n;
import org.tarsius.imaging.PhotoLoader;
import org.tarsius.persistence.PersistenceException;
import org.tarsius.persistence.dao.PhotoDAO;
import org.tarsius.util.PathUtil;
import org.tarsius.util.StringUtil;

public class ImportPhotoTask extends BackgroundTask {
	
	private static Log log = LogFactory.getLog(ImportPhotoTask.class);
	
	private List<String> fileList = null;
	
	public ImportPhotoTask(List<String> fileList) {
		this.fileList = fileList;
	}
	
	@Override
	protected void executeTask() {
		// TODO add check for disk space
		// TODO implement cancel
		// TODO add failure handling (db rollback, remove copied files, etc)

		log.debug("Begin import photos task");
		
		ProgressDialog progress = new ProgressDialog(I18n
				.translate("ImportingPhotos"), I18n
				.translate("ImportingPhotos"), true,
				ProgressDialog.TextStatusType.Percentage, 0, this.fileList
						.size(), 0);

		// TODO make the dialog appear at the center of the main window

		PhotoLoader pl = new PhotoLoader();
		Photo photo = null;
		File file = null;
		Integer index = 0;
		for(String path : fileList){
			progress.setProgress(index);
			progress.setStatus(I18n.translate("Importing", path));
			index++;
			if(log.isDebugEnabled()){
				log.debug("Importing photo " + index + " of " + fileList.size() + 
						": " + path);
			}
			file = new File(path);
			photo = pl.load(file);
			// identify destination for copy into gallery
			String pathInGallery = Context.getGallery().getPhotosPath() + 
				PathUtil.pathFromDate(photo.getDate()) +
				File.separator + file.getName();
			// copy file to gallery
			File fileInGallery = new File(pathInGallery);
			// if a file with the same name already exists in the gallery
			if(fileInGallery.exists()){
				// find a name which is available and open the file
				fileInGallery = findAvailableName(pathInGallery);
			}
			try {
				FileUtils.copyFile(file, fileInGallery, true);
			} catch (IOException e) {
				throw new RuntimeException("Failed to copy photo to gallery", e);
			}
			// load new photo
			photo = pl.load(fileInGallery);
			// import into database
			try {
				PhotoDAO.getInstance().insertPhoto(photo);
			} catch (PersistenceException e) {
				throw new RuntimeException("Failed to import photo into db", e);
			}
		}
		progress.finished();
		log.debug("End import photos task");
		EventBelt.getInstance().dispatch(Event.Type.GALLERY_CHANGED);
	}
	
	private File findAvailableName(String path){
		int sequence = 0;
		String newName = null;
		File file = null;
		do {
			newName = addSequenceToName(path, sequence);
			file = new File(newName);
			sequence++;
		} while(file.exists());
		return file;
	}
	
	private String addSequenceToName(String path, int sequence){
		String[] split = StringUtil.splitOnLastOccurance(path, ".");
		return split[0] + "_" + String.format("%04d", sequence) + split[1];
	}

}
