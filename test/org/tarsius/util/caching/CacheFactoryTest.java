package org.tarsius.util.caching;

import org.tarsius.util.caching.Cache;
import org.tarsius.util.caching.CacheFactory;

import junit.framework.TestCase;

public class CacheFactoryTest extends TestCase {
	
	public void testCreateCache(){
		Cache cache = CacheFactory.createCache("test");
		assertNotNull(cache);
	}

}
