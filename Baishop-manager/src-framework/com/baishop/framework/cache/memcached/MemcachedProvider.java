package com.baishop.framework.cache.memcached;

import java.util.Properties;

import com.baishop.framework.cache.Cache;
import com.baishop.framework.cache.CacheException;
import com.baishop.framework.cache.CacheProvider;
import com.baishop.framework.utils.PropertiesUtil;

public class MemcachedProvider implements CacheProvider, MemcachedConstant {

	public Cache buildCache(String cacherName, Properties properties)
			throws CacheException {
		String key = new StringBuffer(CACHER_PREFIX).append(cacherName).append(CACHER_GROUP).toString();
		String[] groups = PropertiesUtil.get().getStringAsArray(key, properties, ","); 
		if(groups.length < 1){
			throw new CacheException("Couldn't find cache_group for [" + key + "] in properties config!");
		}
		Cache cache = null;
		if(groups.length == 1){
			cache = new MemcachedCache(cacherName, groups[0], properties);
		}else{
			cache = new MemcachedGroupCache(cacherName, groups, properties);
		}
		return cache;
	}

}
