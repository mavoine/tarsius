package org.tarsius.gui;

import java.util.List;

import org.tarsius.bean.Photo;
import org.tarsius.event.AbstractEvent;
import org.tarsius.event.Event;
import org.tarsius.event.Event.Type;

public class OpenViewerEvent extends AbstractEvent {
	
	private int index = -1;
	private List<Photo> photoSet = null;
	
	public OpenViewerEvent(List<Photo> photoSet, int index) {
		this.index = index;
		this.photoSet = photoSet;
	}

	@Override
	public Type getEventType() {
		return Event.Type.OPEN_VIEWER;
	}
	
	public int getIndex(){
		return index;
	}
	
	public List<Photo> getPhotoSet() {
		return photoSet;
	}
	
}
