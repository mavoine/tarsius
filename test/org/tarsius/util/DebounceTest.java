package org.tarsius.util;

import org.tarsius.util.Debounce;

import junit.framework.TestCase;

public class DebounceTest extends TestCase {
	
	private Integer myInt = 0;
	
	public DebounceTest() {
	}
	
	public void testDebounce(){
		
		Debounce debounce = new Debounce(200, 100){
			@Override
			public void execute() {
				increase();
			}
		};
		for(int i=0; i<10; i++){
			debounce.hit();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		assertEquals("Right after having hit the debounce, no change", 
				0, myInt.intValue());
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {}
		assertEquals("Wait beyond the debonce delay", 1, myInt.intValue());
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {}
		assertEquals("Check that the debounce was cancelled", 1, myInt.intValue());
	}
	
	private void increase(){
		myInt++;
	}

}
