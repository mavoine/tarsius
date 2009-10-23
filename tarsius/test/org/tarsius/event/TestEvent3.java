package org.tarsius.event;

import org.tarsius.event.AbstractEvent;
import org.tarsius.event.Event;
import org.tarsius.event.Event.Type;

public class TestEvent3 extends AbstractEvent {
	
	private Integer value = null;
	
	public TestEvent3(Integer value) {
		this.value = value;
	}

	@Override
	public Type getEventType() {
		return Event.Type.TEST_EVENT_3;
	}

	public Integer getValue(){
		return value;
	}
	
}
