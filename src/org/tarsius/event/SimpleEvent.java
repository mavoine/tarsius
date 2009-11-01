package org.tarsius.event;

import org.tarsius.event.Event.Type;

/**
 * A simple event which carries no extra data outside its event type. 
 */
public class SimpleEvent extends AbstractEvent {
	
	private Event.Type eventType = null; 

	public SimpleEvent(Event.Type eventType) {
		this.eventType = eventType;
	}
	
	@Override
	public Type getEventType() {
		return eventType;
	}
	
}
