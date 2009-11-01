package org.tarsius.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventBelt {
	
	private static Log log = LogFactory.getLog(EventBelt.class);
	private static EventBelt instance = null;
	
	private HashMap<Event.Type,Set<InvocationTarget>> listeners = null;
	
	private EventBelt() {
		this.listeners = new HashMap<Event.Type,Set<InvocationTarget>>();
	}
	
	public static EventBelt getInstance(){
		if(instance == null){
			instance = new EventBelt();
		}
		return instance;
	}
	
	/**
	 * Dispatches an event to all the listeners of its event type.
	 * @param event
	 */
	public void dispatch(AbstractEvent event){
		Set<InvocationTarget> list = this.listeners.get(event.getEventType());
		if(list != null){
			for(InvocationTarget el : list){
				try {
					log.debug("Invoking event " + event.getEventType()
							+ ", calling method " + el.getMethod().getName()
							+ " on object " + el.getObject().getClass().getName());
					if(el.getMethod().getParameterTypes().length == 0){
						// if there are no parameters
						el.getMethod().invoke(el.getObject());
					} else if(el.getMethod().getParameterTypes().length == 1){
						// if there is 1 parameter expected, pass the event
						el.getMethod().invoke(el.getObject(), event);
					} else {
						throw new Exception(
							"InvocationTarget has invalid number of parameters");
					}
				} catch (Exception ex){
					log.error("Error processing event", ex);
				}
			}
		}
	}
	
	/**
	 * A short-hand for dispatching an event without extra data.
	 * @param eventType
	 * @see EventBelt#dispatch(AbstractEvent) 
	 */
	public void dispatch(Event.Type eventType){
		dispatch(new SimpleEvent(eventType));
	}
	
//	public void registerListener(Event.Type eventType, EventListener listener){
//		if(this.listeners.get(eventType) == null){
//			this.listeners.put(eventType, new HashSet<EventListener>());
//		}
//		this.listeners.get(eventType).add(listener);
//	}
//	
//	public void unregisterListener(Event.Type eventType, EventListener listener){
//		if(this.listeners.get(eventType) != null){
//			this.listeners.get(eventType).remove(listener);
//		}
//	}

	// TODO fix unregisterListener
//	public void unregisterListener(Object object){
//		Set<Event.Type> x = this.listeners.keySet();
//		for(Event.Type type : x ){
//			if(this.listeners.get(type).contains(new Whatever(object, null))){
//				this.listeners.get(type).remove(new Whatever(object, null));
//			}
//		}
//	}
	
	public void registerListener(Object object){
		for(Method objectMethod : object.getClass().getMethods()){
			Annotation[] annotations = objectMethod.getDeclaredAnnotations();
			for(Annotation annotation : annotations){
				try {
					if(annotation instanceof EventListener){
						Method eventMethod = annotation.getClass().getMethod("eventType");
						Event.Type[] events = (Event.Type[])eventMethod.invoke(annotation);
						for(Event.Type eventType : events){
							log.debug("Registering object of type [" 
									+ object.getClass() 
									+ "] to listen for event [" + eventType + "]");
							if(this.listeners.get(eventType) == null){
								this.listeners.put(eventType, new HashSet<InvocationTarget>());
							}
							this.listeners.get(eventType).add(new InvocationTarget(object, objectMethod));
						}
					}
				} catch (SecurityException e) {
					log.error(e);
				} catch (NoSuchMethodException e) {
					log.error(e);
				} catch (IllegalArgumentException e) {
					log.error(e);
				} catch (IllegalAccessException e) {
					log.error(e);
				} catch (InvocationTargetException e) {
					log.error(e);
				}
			}
		}
	}
	
	private class InvocationTarget {
		
		private Object object = null;
		private Method method = null;
		
		public InvocationTarget(Object object, Method method) {
			this.object = object;
			this.method = method;
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if(otherObject instanceof InvocationTarget){
				return ((InvocationTarget)otherObject).object == this.object;
			}
			return false;
		}
		
		public Object getObject() {
			return object;
		}
		
		public Method getMethod() {
			return method;
		}
		
	}
	
}
