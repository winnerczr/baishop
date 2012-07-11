package com.baishop.framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.baishop.framework.web.wrapper.MemcachedHttpServletRequestWrapper;

/**
 * Memcached集群拦截器
 * @author Linpn
 */
public class MemcachedClusterFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {		
	}

	@Override
	public void destroy() {		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new MemcachedHttpServletRequestWrapper((HttpServletRequest)request), response);		
	}
}
