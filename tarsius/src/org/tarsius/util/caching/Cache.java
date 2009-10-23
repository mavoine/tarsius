package org.tarsius.util.caching;

public interface Cache {
	
	public void clear();
	public boolean containsKey(Object key);
	public Object get(Object key);
	public void put(Object key, Object value);
	public int size();
	// TODO do I need this?
//	public void dispose();

}
