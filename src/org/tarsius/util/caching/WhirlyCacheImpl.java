package org.tarsius.util.caching;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.whirlycott.cache.CacheException;
import com.whirlycott.cache.CacheManager;

public class WhirlyCacheImpl implements Cache {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(WhirlyCacheImpl.class);
	
	private com.whirlycott.cache.Cache store;
	
	public WhirlyCacheImpl(String name) throws Exception {
		try {
			this.store = CacheManager.getInstance().getCache(name);
		} catch (CacheException e) {
			throw new Exception("Cannot init cache named '" + name + "'", e);
		}
	}

	public void clear() {
		this.store.clear();
	}

	public boolean containsKey(Object key) {
		return this.store.retrieve(key) != null;
	}

	public Object get(Object key) {
		return this.store.retrieve(key);
	}

	public void put(Object key, Object value) {
		this.store.store(key, value);
	}
	
	public int size(){
		return this.store.size();
	}

	// TODO do I need this?
//	public void dispose() {
//		try {
//			CacheManager.getInstance().destroy(this.name);
//		} catch (CacheException e) {
//			log.warn("Failed to dispose of cache named '" + this.name + "'", e);
//		}
//	}

}
