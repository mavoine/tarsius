package org.tarsius.persistence;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.persistence.ScriptLoader;

public class ScriptLoaderTest extends TestCase {
	
	private Log log = LogFactory.getLog(ScriptLoaderTest.class);

	/**
	 * Tests the loading of a script.
	 * @throws Exception
	 */
	public void testLoadScript() throws Exception {
		ScriptLoader sl = new ScriptLoader();
		String s[] = sl.loadScript("script/dbstruct");

		log.debug("Loaded script [script/dbstruct] which contains " + s.length + " statements");
		for(int i = 0; i < s.length; i++){
			log.trace(s[i]);
		}

		assertNotNull(s);
		assertTrue("statements not empty", s.length > 0);
	}
	
}
