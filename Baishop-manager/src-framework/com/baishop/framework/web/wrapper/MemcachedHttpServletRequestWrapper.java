package com.baishop.framework.web.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * Memcached request 装饰类
 * @author Administrator
 *
 */
public class MemcachedHttpServletRequestWrapper extends HttpServletRequestWrapper {
	
	public MemcachedHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	
    /**
     * The default behavior of this method is to return getSession()
     * on the wrapped request object.
     */
    public HttpSession getSession() {
    	return this.getSession(true);
    }
    
	
    /**
     * The default behavior of this method is to return getSession(boolean create)
     * on the wrapped request object.
     */
    public HttpSession getSession(boolean create) {
		HttpSession session = super.getSession(create);
		if(session!=null)
			session =  new MemcachedHttpSessionWrapper(session);
		
		return session;
    }
	

}
