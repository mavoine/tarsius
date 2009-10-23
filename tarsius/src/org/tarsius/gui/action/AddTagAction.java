package org.tarsius.gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.task.AddTagTask;

public class AddTagAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(AddTagAction.class);
	
	private Tag tag = null;
	private PhotoSelectionProvider photoSelectionProvider = null;

	public AddTagAction(Tag tag, PhotoSelectionProvider photoSelectionProvider) {
		super(tag.getName());
		this.tag = tag;
		this.photoSelectionProvider = photoSelectionProvider;
	}
	
	public void actionPerformed(ActionEvent event) {
		List<Photo> photos = photoSelectionProvider.getSelectedPhotos();
		log.debug("Add Tag action invoked: adding tag " + tag + " to " + photos.size() + " photos");
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(tag);
		AddTagTask task = new AddTagTask(tags, photos);
		task.execute();
	}
	
}
