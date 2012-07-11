package com.baishop.framework.cache.memcached;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baishop.framework.cache.Cache;
import com.baishop.framework.cache.CacheException;
import com.baishop.framework.cache.CacheProps;

public class MemcachedGroupCache implements Cache {
	private static Log log = LogFactory.getLog(MemcachedGroupCache.class);
	private MemcachedCache[] caches;
	private int cacherLength;
	private Random random = new Random();
	private CacheProps cacheProps;

	public MemcachedGroupCache(String poolName, String[] cacheGroup, Properties properties){
		if(cacheGroup == null || cacheGroup.length < 2)
			throw new CacheException("Cache's group.length should be 2 or >2 value");
		cacherLength = cacheGroup.length;
		caches = new MemcachedCache[cacherLength];
		for(int i=0; i<cacherLength; i++){
			caches[i] = new MemcachedCache(poolName, cacheGroup[i], properties, false);
		}
		cacheProps = new MemcachedCacheProps(poolName, properties);
	}
	
	public Object get(Object key) throws CacheException {
		int curr = random.nextInt(cacherLength);
		Object rs = caches[curr].get(key);
		if(rs == null){
			log.warn("Could not get value from cache server [" + curr + "], now rescure get from other servers.");
			rs = rescureGet(key, curr);
		}
		return rs;
	}
	// 补救获取
	private Object rescureGet(Object key, int currFail){
		List<Integer> liveList = new ArrayList<Integer>(cacherLength - 1);
		List<Integer> deadList = new ArrayList<Integer>(cacherLength);
		deadList.add(currFail);
		for(int i=0; i<cacherLength; i++){
			if(i != currFail)
				liveList.add(i);
		}
		int curr = 0, liveIndex = 0;
		Object rs = null;
		while(liveList.size() > 0){
			liveIndex = random.nextInt(liveList.size());
			curr = liveList.get(liveIndex);
			rs = caches[curr].get(key);
			if(rs == null){
				deadList.add(curr);
				liveList.remove(liveIndex);
			}else{
				for(int index : deadList){
					caches[index].put(key, rs, cacheProps.getExpire());
				}
				break;
			}
		}
		
		return rs;
	}
	
	public CacheProps getCacheProps() {
		return cacheProps;
	}
	
	public void add(Object key, Object value) throws CacheException {
		for(MemcachedCache cache : caches){
			cache.add(key, value);
		}
	}

	public void add(Object key, Object value, long expire)
			throws CacheException {
		for(MemcachedCache cache : caches){
			cache.add(key, value, expire);
		}
	}

	public void update(Object key, Object value) throws CacheException {
		for(MemcachedCache cache : caches){
			cache.update(key, value);
		}
	}

	public void update(Object key, Object value, long expire)
			throws CacheException {
		for(MemcachedCache cache : caches){
			cache.update(key, value, expire);
		}
	}

	public void put(Object key, Object value) throws CacheException {
		for(MemcachedCache cache : caches){
			cache.put(key, value);
		}
	}

	public void put(Object key, Object value, long expire)
			throws CacheException {
		for(MemcachedCache cache : caches){
			cache.put(key, value, expire);
		}
	}

	public void remove(Object key) throws CacheException {
		for(MemcachedCache cache : caches){
			cache.remove(key);
		}
	}

	public void destroy() throws CacheException {
		for(MemcachedCache cache : caches){
			cache.destroy();
		}
	}

}
