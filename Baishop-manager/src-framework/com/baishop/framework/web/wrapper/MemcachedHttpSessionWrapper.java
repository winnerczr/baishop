package com.baishop.framework.web.wrapper;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baishop.framework.cache.Cache;


/**
 * Memcached Session 装饰类
 * @author Linpn
 */
@SuppressWarnings("deprecation")
public class MemcachedHttpSessionWrapper implements HttpSession {
	
	private static Cache clusterCache;
	private HttpSession session;	
	
	public MemcachedHttpSessionWrapper(HttpSession session) {	    
	    if(clusterCache==null)
	    	clusterCache = (Cache)WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()).getBean("clusterCache");
	    
	    this.session = session;	    
	}
	
	
	@Override
	public Object getAttribute(String name) {
		return clusterCache.get(this.getId() + name);
	}
	
	@Override
	public void setAttribute(String name, Object value) {
		clusterCache.put(this.getId() + name, value, clusterCache.getCacheProps().getExpire());
	}

	@Override
	public void removeAttribute(String name) {
		clusterCache.remove(this.getId() + name);
	}
	
	
	@Override
	public String getId() {
		return session.getId();
	}


	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}


	@Override
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}


	@Override
	public ServletContext getServletContext() {
		return session.getServletContext();
	}


	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
	}


	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Enumeration getAttributeNames() {
		return session.getAttributeNames();
	}
	

	@Override
	public void invalidate() {
		session.invalidate();
	}


	@Override
	public boolean isNew() {
		return session.isNew();
	}
	
	
	
	
	/**
    *
    * @deprecated 	As of Version 2.1, this method is
    *			deprecated and has no replacement.
    *			It will be removed in a future
    *			version of the Java Servlet API.
    *
    */
	@Override
	public HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}
	
    /**
    *
    * @deprecated 	As of Version 2.2, this method is
    * 			replaced by {@link #getAttribute}.
    *
    * @param name		a string specifying the name of the object
    *
    * @return			the object with the specified name
    *
    * @exception IllegalStateException	if this method is called on an
    *					invalidated session
    *
    */
	@Override
	public Object getValue(String name) {
		return session.getValue(name);
	}

    /**
    *
    * @deprecated 	As of Version 2.2, this method is
    * 			replaced by {@link #getAttributeNames}
    *
    * @return				an array of <code>String</code>
    *					objects specifying the
    *					names of all the objects bound to
    *					this session
    *
    * @exception IllegalStateException	if this method is called on an
    *					invalidated session
    *
    */
	@Override
	public String[] getValueNames() {
		return session.getValueNames();
	}
	
    /**
    *
    * @deprecated 	As of Version 2.2, this method is
    * 			replaced by {@link #setAttribute}
    *
    * @param name			the name to which the object is bound;
    *					cannot be null
    *
    * @param value			the object to be bound; cannot be null
    *
    * @exception IllegalStateException	if this method is called on an
    *					invalidated session
    *
    */
	@Override
	public void putValue(String name, Object value) {
		session.putValue(name, value);
	}

    /**
    *
    * @deprecated 	As of Version 2.2, this method is
    * 			replaced by {@link #removeAttribute}
    *
    * @param name				the name of the object to
    *						remove from this session
    *
    * @exception IllegalStateException	if this method is called on an
    *					invalidated session
    */
	@Override
	public void removeValue(String name) {
		session.removeValue(name);
	}
	

}
