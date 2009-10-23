package org.tarsius.task;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.persistence.Database;
import org.tarsius.persistence.dao.TagDAO;


public class AddTagTask extends BackgroundTask {
	
	private static Log log = LogFactory.getLog(AddTagTask.class);
	
	private List<Tag> tags = null;
	private List<Photo> photos = null;
	
	public AddTagTask(List<Tag> tags, List<Photo> photos) {
		this.tags = tags;
		this.photos = photos;
	}
	
	protected void executeTask() throws TaskException {
		try {
			// TODO could/should this be done in a batch?
			// TODO deal with already existing tags
			Database.getInstance().getSqlMap().startTransaction();
			for(Tag tag : tags){
				for(Photo photo : photos){
					TagDAO.getInstance().addTag(tag, photo);
				}
			}
			Database.getInstance().getSqlMap().commitTransaction();
		} catch (Exception e){
			throw new TaskException("AddTagTask failed", e);
		} finally {
			try {
				Database.getInstance().getSqlMap().endTransaction();
			} catch (SQLException e){
				log.error("Transaction did not complete normally", e);
			}
		}
		EventBelt.getInstance().dispatch(Event.Type.GALLERY_CHANGED);
	}

}
