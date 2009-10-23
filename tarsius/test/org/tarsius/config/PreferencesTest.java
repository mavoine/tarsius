package org.tarsius.config;

import org.tarsius.config.Preferences;

import junit.framework.TestCase;

public class PreferencesTest extends TestCase {
	
	private Preferences preferences = null;
	
	@Override
	protected void setUp() throws Exception {
		preferences = new Preferences();
	}
	
	@Override
	protected void tearDown() throws Exception {
		preferences = null;
	}
	
	public void testRecentGalleries(){
		assertEquals("Initially empty", 0, 
			preferences.getRecentGalleries().size());
		
		preferences.addRecentGallery("/my/gallery1");
		assertEquals("Adding 1 gallery", 1, 
			preferences.getRecentGalleries().size());
		
		preferences.addRecentGallery("/my/gallery1");
		assertEquals("Adding an existing gallery", 1, 
			preferences.getRecentGalleries().size());
		
		preferences.addRecentGallery("/my/gallery2");
		assertEquals("Adding another gallery", 2, 
			preferences.getRecentGalleries().size());

		preferences.removeRecentGallery("/my/gallery1");
		assertEquals("Removing a gallery", 1, 
			preferences.getRecentGalleries().size());

	}

}
