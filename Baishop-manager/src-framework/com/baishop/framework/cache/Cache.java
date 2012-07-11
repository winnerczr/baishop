package com.baishop.framework.cache;


public interface Cache {
	
	CacheProps getCacheProps();
	
	Object get(Object key) throws CacheException;
	
	void add(Object key, Object value) throws CacheException;
	/**
	 * 
	 * @param key
	 * @param value
	 * @param expire 过期时间，单位为秒
	 * @throws CacheException
	 */
	void add(Object key, Object value, long expire) throws CacheException;
	
	void update(Object key, Object value) throws CacheException;
	/**
	 * 
	 * @param key
	 * @param value
	 * @param expire 过期时间，单位为秒
	 * @throws CacheException
	 */
	void update(Object key, Object value, long expire) throws CacheException;

	void put(Object key, Object value) throws CacheException;
	/**
	 * 
	 * @param key
	 * @param value
	 * @param expire 过期时间，单位为秒
	 * @throws CacheException
	 */
	void put(Object key, Object value, long expire) throws CacheException;
	
	void remove(Object key) throws CacheException;

	void destroy() throws CacheException;
	
}
