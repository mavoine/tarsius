package org.tarsius.imaging;

import java.awt.Image;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Thumbnails {
	
	private static Log log = LogFactory.getLog(Thumbnails.class);

	private final ExecutorService pool;
	
	protected abstract Image makeThumbnail(String rootDirectory, String relativePathToImage) throws Exception;
	public abstract Integer getThumbnailMaxWidth();
	public abstract Integer getThumbnailMaxHeight();
	public abstract void deleteThumbnail(String pathToOriginal);
	public abstract boolean thumbnailExists(String rootDirectory, String relativePathToImage);
	
	protected boolean permanentCachingEnabled = true;
	
	public Thumbnails() {
		pool = Executors.newSingleThreadExecutor();
	}
	
	public void setPermanentCaching(boolean enabled){
		permanentCachingEnabled = enabled;
	}
	
	public void getThumbnail(String rootDirectory, String relativePathToImage, 
			ThumbnailsCallback thumbnailsCallback){
		// TODO split in 2 queues: one for existing thumbnails and one for thumbnails which need to be created
		pool.execute(new Thumbnails.Executor(rootDirectory, relativePathToImage, thumbnailsCallback));
	}
	
	public Image getThumbnailNow(String rootDirectory, String relativePathToImage) throws Exception {
		return makeThumbnail(rootDirectory, relativePathToImage);
	}

	public class Executor implements Runnable {
		
		private String rootDirectory = null;
		private String relativePathToImage = null;
		private ThumbnailsCallback postOperation = null;
		
		public Executor(String rootDirectory, String relativePathToImage, ThumbnailsCallback postOperation) {
			this.rootDirectory = rootDirectory;
			this.relativePathToImage = relativePathToImage;
			this.postOperation = postOperation;
		}
		
		public void run() {
			try {
				Image image = makeThumbnail(rootDirectory, relativePathToImage);
				if(postOperation != null){
					postOperation.execute(image);
				}
			} catch (Exception ex){
				log.error("Error while processing thumbnail job", ex);
			}
		}
	}
	
}
