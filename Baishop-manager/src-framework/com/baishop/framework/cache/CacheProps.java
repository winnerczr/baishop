package com.baishop.framework.cache;

public interface CacheProps {
	/**
	 * 获取该缓存中缓存对象的默认过期时间，
	 * 以秒为单位
	 * @return
	 */
	long getExpire();
}
