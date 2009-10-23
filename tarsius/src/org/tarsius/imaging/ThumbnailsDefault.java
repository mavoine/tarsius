package org.tarsius.imaging;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;

public class ThumbnailsDefault extends ThumbnailsLinux {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(ThumbnailsDefault.class);

	private static final Integer THUMB_MAX_WIDTH  = 256;
	private static final Integer THUMB_MAX_HEIGHT = 256;

	protected ThumbnailsDefault(){
		// force creation of thumbs directory
		try {
			FileUtils.forceMkdir(new File(getThumbnailCachePath()));
		} catch (IOException e) {
			// if creation failed, disable saving of thumbnails on disk
			log.error("Failed to create thumbnails cache directory", e);
			permanentCachingEnabled = false;
		}
	}
	
	@Override
	protected String getThumbnailCachePath() {
		return Context.getGallery().getThumbsPath();
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
