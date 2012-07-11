package com.baishop.framework.cache;

public class CacheException extends RuntimeException {
	
	private static final long serialVersionUID = -7428229639605392218L;

	public CacheException(String s) {
		super(s);
	}

	public CacheException(String s, Exception e) {
		super(s, e);
	}
	
	public CacheException(Exception e) {
		super(e);
	}
	
}
