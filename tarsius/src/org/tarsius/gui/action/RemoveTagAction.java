package org.tarsius.gui.action;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.task.RemoveTagTask;

public class RemoveTagAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(RemoveTagAction.class);
	
	private Tag tag = null;
	private PhotoSelectionProvider photoSelectionProvider = null;
	
	public RemoveTagAction(Tag tag, PhotoSelectionProvider photoSelectionProvider) {
		super(tag.getName());
		this.tag = tag;
		this.photoSelectionProvider = photoSelectionProvider;
	}
	
	public void actionPerformed(ActionEvent event) {
		List<Photo> photos = photoSelectionProvider.getSelectedPhotos();
		RemoveTagTask task = new RemoveTagTask(this.tag, photos);
		log.debug("Removing tag [" + this.tag + "] from " + photos.size()
				+ " photo(s)");
		task.execute();
	}
	
	public Tag getTag(){
		return tag;
	}

}
