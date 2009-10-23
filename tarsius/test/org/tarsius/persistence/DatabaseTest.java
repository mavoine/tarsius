package org.tarsius.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.persistence.Database;
import org.tarsius.test.ExtendedTestCase;

public class DatabaseTest extends ExtendedTestCase {
	
	private static Log log = LogFactory.getLog(DatabaseTest.class);

	public void testUpgrade() throws Exception {
		log.debug("Creating database");
		Database db = Database.getInstance();
		log.debug("Upgrading database");
		db.upgrade();
	}
	
}
