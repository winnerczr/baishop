package com.baishop.framework.cache;

import java.util.Properties;

public interface CacheProvider {

	public Cache buildCache(String cacherName, Properties properties)
			throws CacheException;

}
