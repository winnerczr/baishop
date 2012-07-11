package com.baishop.framework.cache.memcached;

public interface MemcachedConstant {
	/**
	 * 前缀
	 */
	String CACHER_PREFIX = "cacher.";
	/**
	 * 每个 cache 对应的服务器组定义
	 */
	String CACHER_GROUP = ".group";
	/**
	 * 每个 cache 对应的过期时间
	 */
	String CACHER_EXPIRE = ".expire";
	/**
	 * 前缀
	 */
	String MEM_PREFIX = "mem.";
	/**
	 * 服务器列表，如：kd-cache1:11211,kd-cache2:11211
	 */
	String MEM_SERVERS = ".servers";
	/**
	 * 各服务器权重，如：2,3
	 */
	String MEM_WEIGHTS = ".weights";
	/**
	 * 初始化连接数，默认 3
	 */
	String MEM_INIT_CONN = ".init_conn";
	/**
	 * 最小连接数，默认 3
	 */
	String MEM_MIN_CONN = ".min_conn";
	/**
	 * 最大连接数，默认10
	 */
	String MEM_MAX_CONN = ".max_conn";
	/**
	 * 是否启用大数据压缩处理，默认为 true
	 */
	String MEM_COMPRESS_ENABLE = ".compress_enable";
	/**
	 * 在 compressEnable 为 true 情况下大于该值（默认30Kb）时采用Gzip压缩处理
	 */
	String MEM_COMPRESS_THRESHOLD = ".compress_threshold";
//	String MEM_ERROR_HANDLER = ".error_handler";
	
//	String MEM_MAX_IDLE = ".max_idle";
//	String MEM_MAX_BUSY_TIME = ".max_busy_time";
//	String MEM_MAINT_SLEEP = ".maint_sleep";
//	String MEM_SOCKET_TO = ".socket_to";
//	String MEM_SOCKET_CONNECT_TO = ".socket_connect_to";
//	String MEM_ALIVE_CHECK = ".alive_check";
//	String MEM_FAILOVER = ".failover";
//	String MEM_FAILBACK = ".failback";
//	String MEM_NAGLE = ".nagle";
//	String MEM_HASHING_ALG = ".hashing_alg";
}
