package com.baishop.framework.cache.memcached;

import java.util.Properties;

import com.baishop.framework.cache.CacheProps;
import com.baishop.framework.utils.PropertiesUtil;

public class MemcachedCacheProps implements CacheProps {
	private long expire = 0;
	
	MemcachedCacheProps(String poolName, Properties properties){
		expire = PropertiesUtil.get().getLong(
				MemcachedConstant.CACHER_PREFIX + poolName + MemcachedConstant.CACHER_EXPIRE,
				properties, -1);
	}

	public long getExpire() {
		return expire;
	}

}
