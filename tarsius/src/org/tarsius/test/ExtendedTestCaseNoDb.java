package org.tarsius.test;

import junit.framework.TestCase;

import org.tarsius.Context;

public class ExtendedTestCaseNoDb extends TestCase {
	
	public ExtendedTestCaseNoDb(){
	}
	
	@Override
	protected void setUp() throws Exception {
		String runDir = System.getProperty("user.dir");
		Context.getGallery().openGallery(runDir, null);
		TestData.setup();
	}
	
	@Override
	protected void tearDown() throws Exception {
		Context.getGallery().close();
		TestData.tearDown();
	}

}
