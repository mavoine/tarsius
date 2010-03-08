package org.tarsius;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.persistence.Database;
import org.tarsius.persistence.ScriptLoader;
import org.tarsius.util.FileUtil;

public class Gallery {
	
	private static Log log = LogFactory.getLog(Gallery.class);
	
	public static final String PHOTO_LOCATION = File.separatorChar + "photos";
	public static final String THUMBS_LOCATION = File.separatorChar + ".thumbs";
	public static final String DATA_LOCATION = File.separatorChar + ".data";
	public static final String DATABASE_LOCATION = DATA_LOCATION + File.separatorChar + "db";
	
	private static String galleryPath = null;
	
	public Gallery(){
		
	}
	
	/**
	 * Opens a gallery with the specified gallery path. The database is assumed
	 * to be in the default location under the gallery root directory.
	 * @param path The absolute path to the root directory of the gallery.
	 * @throws FileNotFoundException If the gallery doesn't exist or cannot
	 * be opened.
	 */
	public void openGallery(String path) throws FileNotFoundException {
		openGallery(path, path + DATABASE_LOCATION);
	}	
	
	/**
	 * Opens a gallery with the specified gallery and database paths. If
	 * dbPath is null, the gallery is opened without opening a connection to 
	 * a database.
	 * @param path The absolute path to the root directory of the gallery.
	 * @param dbPath The absolute path to the database of the gallery.
	 * @throws FileNotFoundException If the gallery doesn't exist or cannot
	 * be opened.
	 */
	public void openGallery(String path, String dbPath) 
		throws FileNotFoundException {
		
		if(galleryPath != null){
			log.info("Closing currently open gallery");
			Database.getInstance().shutdown();
			log.info("Gallery closed");
		}
		
		log.info("Opening gallery at '" + path + "', database at '" + dbPath + "'");
		
		File file = new File(path);
		if(file.exists() && file.isDirectory()){
			
			// TODO add better checks for the validity of the gallery (properties file?)
			
			if(dbPath != null){
				Database.getInstance().open(
						dbPath, 
						Context.getGlobalConfig().getDatabaseUsername(),
						Context.getGlobalConfig().getDatabasePassword());
			}
			
			galleryPath = path;
			
//			ThumbnailsFactory.init();
			
			log.info("Gallery opened");
			EventBelt.getInstance().dispatch(Event.Type.GALLERY_OPENED);
			
		} else {
			galleryPath = null;
			throw new FileNotFoundException("Path '" + path + 
					"' is either invalid or is not a directory");
		}
	}

	/**
	 * Creates a gallery at the specified path. 
	 * @param path The absolute path to the root directory of the gallery.
	 * @throws Exception If a fatal error occured while creating the gallery.
	 */
	public void createGallery(String path) throws Exception {
		
		log.info("Creating a gallery at '" + path + "'");

		File galleryDir = new File(path);
		File dataDir = new File(path + DATA_LOCATION);
		File photosDir = new File(path + PHOTO_LOCATION);
		File thumbsDir = new File(path + THUMBS_LOCATION);

		log.debug("Creating directory structure");
		
		FileUtils.forceMkdir(galleryDir);
		FileUtils.forceMkdir(dataDir);
		FileUtils.forceMkdir(thumbsDir);
		FileUtils.forceMkdir(photosDir);
		
		FileUtil.makeHidden(dataDir);
		FileUtil.makeHidden(thumbsDir);
		
		log.debug("Creating database");
		Database.getInstance().open(
				path + DATABASE_LOCATION, 
				Context.getGlobalConfig().getDatabaseUsername(),
				Context.getGlobalConfig().getDatabasePassword());

		// load the data structure
		ScriptLoader scriptLoader = new ScriptLoader();
		String[] statements = scriptLoader.loadScript("script/dbstruct");
		Database.getInstance().executeBatch(statements);

		galleryPath = path;
		
		log.info("Gallery created");
	}
	
	public void close(){
		if(Database.getInstance().isDatabaseOpen()){
			try {
				Database.getInstance().shutdown();
			} catch (Exception e){
				log.error("Problem closing the database", e);
			}
		}
		galleryPath = null;
		EventBelt.getInstance().dispatch(Event.Type.GALLERY_CLOSED);
		log.info("Gallery closed");
	}
	
	/**
	 * Returns the absolute path to the gallery's root directory.
	 * @return path to gallery's root directory
	 */
	public String getRootPath() {
		return galleryPath;
	}
	
	/**
	 * Returns the absolute path to the gallery's photo directory.
	 * @return path to gallery's photo directory
	 */
	public String getPhotosPath() {
		return galleryPath + PHOTO_LOCATION;
	}
	
	/**
	 * Returns the absolute path to the gallery's thumbnails directory.
	 * @return path to gallery's thumbnails directory
	 */
	public String getThumbsPath() {
		return galleryPath + THUMBS_LOCATION;
	}

	public boolean isOpen(){
		return galleryPath != null;
	}

}
