package org.tarsius.util.caching;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheFactory {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(CacheFactory.class);
	
	private CacheFactory(){
	}
	
	public static Cache createCache(String name){
		log.info("Creating a cache named '" + name + "'");
		WhirlyCacheImpl cache = null;
		try {
			cache = new WhirlyCacheImpl(name);
		} catch (Exception e){
			throw new RuntimeException("Cannot init cache", e);
		}
		return cache;
	}

}
