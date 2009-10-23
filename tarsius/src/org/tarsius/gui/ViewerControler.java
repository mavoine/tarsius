package org.tarsius.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.tarsius.bean.Photo;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.event.EventListener;
import org.tarsius.gui.action.GoToNextActionListener;
import org.tarsius.gui.action.GoToPreviousActionListener;
import org.tarsius.gui.action.PhotoSelectionProvider;
import org.tarsius.util.DateUtil;

public class ViewerControler implements PhotoSelectionProvider,
		GoToNextActionListener, GoToPreviousActionListener {
	
	private ViewerPane viewerPane = null;
	private MainControler mainControler = null;
	
	private int index = 0;
	private List<Photo> photoSet = null;
	
	public ViewerControler(MainControler mainControler) {
		this.mainControler = mainControler;
	}
	
	public void bind(ViewerPane viewerPane){
		this.viewerPane = viewerPane;

		// bind event listeners
		EventBelt.getInstance().registerListener(this);
		viewerPane.goToPreviousAction.addActionListener(this);
		viewerPane.goToNextAction.addActionListener(this);

		// bind actions to components
		viewerPane.openBrowserAction.setPhotoSelectionProvider(this);
	}

	@EventListener(eventType=Event.Type.OPEN_VIEWER)
	public void openPhoto(OpenViewerEvent openViewerEvent){
		this.index = openViewerEvent.getIndex();
		this.photoSet = openViewerEvent.getPhotoSet();
		this.mainControler.show(this.viewerPane);
		refreshUI();
	}
	
	private void refreshUI(){
		Photo currentPhoto = photoSet.get(index);
		File photoFile = new File(currentPhoto.getAbsolutePath());
		// set next/previous controls
		
		// set info
		this.viewerPane.fileNameLabel.setText(photoFile.getName());
		this.viewerPane.dateLabel.setText(DateUtil.formatDate(currentPhoto
				.getDate()));
		// set image
		this.viewerPane.photoFrame.setImage(photoFile);
	}

	public List<Photo> getSelectedPhotos() {
		List<Photo> photos = new ArrayList<Photo>();
		photos.add(this.photoSet.get(index));
		return photos;
	}

	public void goToNext() {
		if((index + 1) < this.photoSet.size()){
			index++;
			refreshUI();
		}
	}

	public void goToPrevious() {
		if((index - 1) >= 0){
			index--;
			refreshUI();
		}
	}

}
