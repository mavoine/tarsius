package org.tarsius.util.caching;

import junit.framework.TestCase;

public class CacheTest extends TestCase {

	private Cache cache = null;
	private Integer key1 = new Integer(1);
	private Integer key2 = new Integer(2);
	private Integer key3 = new Integer(3);
	private Integer key4 = new Integer(4);
	private String value1 = new String("value1");
	private String value2 = new String("value2");
	private String value3 = new String("value3");
	private String value4 = new String("value4");
	
	@Override
	protected void setUp() throws Exception {
		cache = CacheFactory.createCache("test");
		cache.put(key1, value1);
		cache.put(key2, value2);
	}
	
	@Override
	protected void tearDown() throws Exception {
		cache.clear();
	}
	
	public void testPut(){
		assertNull("value4 not yet in cache", cache.get(key4));
		cache.put(key4, value4);
		assertTrue("value4 now in cache", cache.containsKey(key4));
		assertEquals("Size of cache", 3, cache.size());
	}
	
	public void testGet(){
		cache.put(key3, value3);
		assertEquals("value1 retrieved from cache", "value1", cache.get(key1));
		assertEquals("value2 retrieved from cache", "value2", cache.get(key2));
		assertEquals("value3 retrieved from cache", "value3", cache.get(key3));
		assertNull("value4 not in cache", cache.get(key4));
	}
	
	// TODO fix: eviction policy or store size not honoured
	public void testEvictPolicy() {
		// evict value1 by inserting value4 (FIFO policy, only 3 spaces in cache)
		cache.put(key4, value4);
		assertEquals("value4 retrieved from cache", "value4", cache.get(key4));
		assertEquals("Size of cache not more than maxsize", 3, cache.size());
		assertFalse("value1 now evicted from cache", cache.containsKey(key1));
	}
	
	public void testClear(){
		cache.clear();
		assertEquals("Cache should be empty", 0, cache.size());
	}

}
