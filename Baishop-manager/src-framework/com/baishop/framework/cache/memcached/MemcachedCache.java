package com.baishop.framework.cache.memcached;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baishop.framework.cache.Cache;
import com.baishop.framework.cache.CacheException;
import com.baishop.framework.cache.CacheProps;
import com.baishop.framework.utils.PropertiesUtil;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcachedCache implements Cache, MemcachedConstant {
	private static Log log = LogFactory.getLog(MemcachedCache.class);
	
	private MemCachedClient memCached;
	private CacheProps cacheProps;
	
	public MemcachedCache(String poolName, String cacheName, Properties properties){
		this(poolName, cacheName, properties, true);
	}
	
	public MemcachedCache(String poolName, String cacheName, Properties properties, boolean createProps){
		initSockIOPool(poolName, cacheName, properties);
		initMemCached(poolName, cacheName, properties);
		if(createProps){
			cacheProps = new MemcachedCacheProps(poolName, properties);
		}
	}
	
	private void initSockIOPool(String poolName, String cacheName, Properties properties){
		String[] servers = PropertiesUtil.get().getStringAsArray(createKey(cacheName, MEM_SERVERS), properties, ",");
		if(servers.length < 1)
			throw new CacheException("Property [" + createKey(cacheName, MEM_SERVERS) + "] should be set in properties config!");
		
		String[] weights = PropertiesUtil.get().getStringAsArray(createKey(cacheName, MEM_WEIGHTS), properties, ",");
		Integer[] ws = null;
		if(weights.length > 0){
			if(weights.length != servers.length)
				throw new CacheException("Server weights's length should be equals to servers's length");
			else{
				ws = new Integer[weights.length];
				for(int i=0; i<weights.length; i++){
					ws[i] = Integer.valueOf(weights[i]);
				}
			}
		}
		int initConn = PropertiesUtil.get().getInt(
				createKey(cacheName, MEM_INIT_CONN), properties, -1);
		int minConn = PropertiesUtil.get().getInt(
				createKey(cacheName, MEM_MIN_CONN), properties, -1);
		int maxConn = PropertiesUtil.get().getInt(
				createKey(cacheName, MEM_MAX_CONN), properties, -1);
		
		SockIOPool pool = SockIOPool.getInstance(poolName + "_" + cacheName);
		pool.setServers( servers );
		if(ws != null){
			pool.setWeights(ws);
		}
		pool.setFailover( true );
		if(initConn > 0)
			pool.setInitConn(initConn); 
		if(minConn > 0)
			pool.setMinConn(minConn);
		if(maxConn > 0)
			pool.setMaxConn(maxConn);
		pool.initialize();
		if(log.isInfoEnabled())
			log.info("SockIOPool be initialized with servers '"
					+ PropertiesUtil.get().getString(
							createKey(cacheName, MEM_SERVERS), properties)
					+ "' and poolName '" + poolName + "_" + cacheName + "'");
	}
	private void initMemCached(String poolName, String cacheName, Properties properties){
		memCached = new MemCachedClient();
		memCached.setPoolName(poolName + "_" + cacheName);
		boolean compressEnable = PropertiesUtil.get().getBoolean(
				createKey(cacheName, MEM_COMPRESS_ENABLE), properties, true);
		int compressThreshold = PropertiesUtil.get().getInt(
				createKey(cacheName, MEM_COMPRESS_THRESHOLD), properties, -1);
		memCached.setCompressEnable(compressEnable);
		if(compressThreshold > 0)
			memCached.setCompressThreshold(compressThreshold);
		
		if(log.isInfoEnabled())
			log.info("MemCachedClient has been initialized with poolName '" + poolName + "_" + cacheName + "'");
	}
	private String createKey(String cacheName, String props){
		return new StringBuffer(MEM_PREFIX).append(cacheName).append(props).toString();
	}
	
	public CacheProps getCacheProps() {
		return cacheProps;
	}
	
	public Object get(Object key) throws CacheException {
		return memCached.get(key.toString());
	}

	public void add(Object key, Object value) throws CacheException {
		memCached.add(key.toString(), value);
	}

	public void add(Object key, Object value, long expire)
			throws CacheException {
		if(expire > 0)
			memCached.add(key.toString(), value, new Date(expire * 1000));
		else
			memCached.add(key.toString(), value);
	}

	public void update(Object key, Object value) throws CacheException {
		memCached.replace(key.toString(), value);
	}

	public void update(Object key, Object value, long expire)
			throws CacheException {
		if(expire > 0)
			memCached.replace(key.toString(), value, new Date(expire * 1000));
		else
			memCached.replace(key.toString(), value);
	}

	public void put(Object key, Object value) throws CacheException {
		memCached.set(key.toString(), value);
	}
	
	public void put(Object key, Object value, long expire)
			throws CacheException {
		if(expire > 0)
			memCached.set(key.toString(), value, new Date(expire * 1000));
		else
			memCached.set(key.toString(), value);
	}

	public void remove(Object key) throws CacheException {
		memCached.delete(key.toString());
	}

	public void destroy() throws CacheException {
		memCached.flushAll();
	}

}
