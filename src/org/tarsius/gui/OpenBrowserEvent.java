package org.tarsius.gui;

import org.tarsius.bean.Photo;
import org.tarsius.event.AbstractEvent;
import org.tarsius.event.Event;
import org.tarsius.event.Event.Type;

public class OpenBrowserEvent extends AbstractEvent {
	
	private Photo photo = null;
	
	public OpenBrowserEvent(Photo photo) {
		this.photo = photo;
	}

	@Override
	public Type getEventType() {
		return Event.Type.OPEN_BROWSER;
	}
	
	public Photo getPhoto() {
		return photo;
	}

}
