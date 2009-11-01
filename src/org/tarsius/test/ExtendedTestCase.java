package org.tarsius.test;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;
import org.tarsius.persistence.Database;
import org.tarsius.persistence.ScriptLoader;

/**
 * Adds behaviors specific to tests that require a test gallery and
 * database to run.
 */
public class ExtendedTestCase extends TestCase {
	
	private static Log log = LogFactory.getLog(ExtendedTestCase.class);
	
	private static String[] dbStructureStmt = null;
	private static String[] dbTestDataStmt = null;
	
	static {
		try {
			// load the database structure statements
			ScriptLoader scriptLoader = new ScriptLoader();
			dbStructureStmt = scriptLoader.loadScript("script/dbstruct");
			// load the test data statements
			dbTestDataStmt = scriptLoader.loadScript("script/testdata");
		} catch (Exception ex){
			log.fatal("Datastructure and test data loading error", ex);
			fail("Cannot create test setup");
		}
	}
	
	@Override
	protected void setUp() throws Exception {
		// open the test gallery and an empty in-memory database
		String runDir = System.getProperty("user.dir");
		Context.getGallery().openGallery(runDir, "mem:test");
		
		TestData.setup();
		
		// prepare the test database
		Database db = Database.getInstance();
		// insert data structure
		db.executeBatch(dbStructureStmt);
		// insert test data
		db.executeBatch(dbTestDataStmt);
	}
	
	@Override
	protected void tearDown() throws Exception {
		// closing the gallery
		Context.getGallery().close();
		TestData.tearDown();
	}

}
