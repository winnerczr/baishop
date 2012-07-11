package com.baishop.framework.cache.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baishop.framework.cache.Cache;
import com.baishop.framework.cache.CacheException;
import com.baishop.framework.cache.CacheProvider;

public class CacheFactory {
	private static Log log = LogFactory.getLog(CacheFactory.class);
	private static CacheFactory instance = new CacheFactory();
	
	private CacheFactory(){
		
	}
	
	public static CacheFactory get(){
		return instance;
	}
	
	protected final Map<String, Cache> caches = new HashMap<String, Cache>();
	
	@SuppressWarnings("rawtypes")
	public Cache create(String cacherName, Class providerClz,
			Properties properties) throws CacheException {
		if(StringUtils.isBlank(cacherName))
			throw new CacheException("CacherName of cache should not be NULL!");

        Cache cache = (Cache) caches.get(cacherName);
        if (cache != null){
        	log.info("Cache hit from cache, ");
            return cache;
        }

		if(providerClz == null)
			throw new CacheException("Cache provider should not be NULL!");

        if (properties == null) {
            throw new IllegalArgumentException("Properties of cache should not be NULL!");
        }
        
		try {
			CacheProvider provider = (CacheProvider)providerClz.newInstance();
			cache = provider.buildCache(cacherName, properties);
			if(cache == null){
				throw new CacheException("Interface: " + providerClz.getName() + ".buildCache() should not return NULL cache!");
			}
			caches.put(cacherName, cache);
			return cache;
		} catch (InstantiationException e) {
			throw new CacheException("Error instantiating " + providerClz.getName(), e);
		} catch (IllegalAccessException e) {
			throw new CacheException("Access errror for " + providerClz.getName(), e);
		}
	}
}
