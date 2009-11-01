package org.tarsius.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.persistence.PersistenceHelper;
import org.tarsius.persistence.dao.TagDAO;

public class RemoveTagTask extends BackgroundTask {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(RemoveTagTask.class);

	private Tag tag = null;
	private List<Photo> photos = null;
	
	public RemoveTagTask(Tag tag, List<Photo> photos) {
		this.tag = tag;
		this.photos = photos;
	}
	
	@Override
	protected void executeTask() throws TaskException {
		try {
			PersistenceHelper.getInstance().beginTransaction();
			for(Photo photo : photos){
				TagDAO.getInstance().removeTag(tag, photo);
			}
			PersistenceHelper.getInstance().commitTransaction();
		} catch (Exception e){
			// TODO notify user
			throw new TaskException("RemoveTagTask failed", e);
		} finally {
			PersistenceHelper.getInstance().endTransaction();
		}
		EventBelt.getInstance().dispatch(Event.Type.GALLERY_CHANGED);
	}

}
