package org.tarsius.event;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
	public Event.Type[] eventType();
}
