package org.tarsius.event;

import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;

import junit.framework.TestCase;

public class EventBeltTest extends TestCase {
	
	public void testDispatch(){
		
		// before registering the listener
		MockListener ml = new MockListener();
		EventBelt.getInstance().dispatch(Event.Type.TEST_EVENT_1);
		// the listener should stay unaffected
		assertEquals(0, ml.getNum().intValue());
		
		// after registering the listener
		EventBelt.getInstance().registerListener(ml);
		EventBelt.getInstance().dispatch(Event.Type.TEST_EVENT_1);
		// it should have been triggered
		assertEquals(1, ml.getNum().intValue());
		
		// after dispatching an event for which the listener is NOT registered
		EventBelt.getInstance().dispatch(Event.Type.TEST_EVENT_2);
		// it should not be affected
		assertEquals(1, ml.getNum().intValue());
		
		// test an event with parameters
		EventBelt.getInstance().dispatch(new TestEvent3(100));
		assertEquals(100, ml.getNum().intValue());

		// TODO fix unregisterListener
//		// after unregistering the listener
//		EventBelt.getInstance().unregisterListener(ml);
//		EventBelt.getInstance().dispatch(Event.Type.TEST_EVENT_1);
//		// it should not be affected anymore
//		assertEquals(1, ml.getNum().intValue());
	}
	
	private class MockListener {
		
		private Integer num = 0;
		
		@EventListener(eventType=Event.Type.TEST_EVENT_1)
		public void increment(){
//		public void processEvent(AbstractEvent event) {
//			switch (event.getEventType()){
//			case TEST_EVENT_1:
				num++;
//			}
		}
		
		public Integer getNum(){
			return num;
		}
		
		@EventListener(eventType=Event.Type.TEST_EVENT_3)
		public void setNum(TestEvent3 testEvent3){
			num = testEvent3.getValue();
		}
		
	}
		
}
