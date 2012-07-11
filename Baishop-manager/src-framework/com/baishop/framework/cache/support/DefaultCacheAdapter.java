package com.baishop.framework.cache.support;

import com.baishop.framework.cache.Cache;
import com.baishop.framework.cache.CacheException;
import com.baishop.framework.cache.CacheProps;

public class DefaultCacheAdapter implements Cache {

	public CacheProps getCacheProps() {
		return null;
	}
	
	public void add(Object key, Object value) throws CacheException {

	}

	public void add(Object key, Object value, long expire)
			throws CacheException {

	}

	public void destroy() throws CacheException {

	}

	public Object get(Object key) throws CacheException {
		return null;
	}

	public void put(Object key, Object value) throws CacheException {

	}

	public void put(Object key, Object value, long expire)
			throws CacheException {

	}

	public void remove(Object key) throws CacheException {

	}

	public void update(Object key, Object value) throws CacheException {

	}

	public void update(Object key, Object value, long expire)
			throws CacheException {

	}

}
