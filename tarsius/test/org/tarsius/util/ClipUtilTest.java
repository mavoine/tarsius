package org.tarsius.util;

import java.awt.Dimension;

import org.tarsius.util.ClipUtil;

import junit.framework.TestCase;

public class ClipUtilTest extends TestCase {

	public void testClip(){
		Dimension clip = null;

		clip = ClipUtil.clip(new Dimension(750, 1000), new Dimension(75, 100));
		assertEquals("proportional portrait clip", new Dimension(75, 100), clip);

		clip = ClipUtil.clip(new Dimension(1000, 750), new Dimension(100, 75));
		assertEquals("proportional landscape clip", new Dimension(100, 75), clip);
		
		clip = ClipUtil.clip(new Dimension(1000, 750), new Dimension(50, 100));
		assertEquals("non-proportional landscape clip", new Dimension(50, 37), clip);
		
		clip = ClipUtil.clip(new Dimension(1000, 750), new Dimension(100, 50));
		assertEquals("non-proportional portrait clip", new Dimension(66, 50), clip);
		
		clip = ClipUtil.clip(new Dimension(1024, 683), new Dimension(468, 383));
		assertEquals("different proportions", new Dimension(468, 312), clip);
		
	}
	
}
