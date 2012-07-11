package com.baishop.manager.security;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;

public class FilterSecurityInterceptor extends org.springframework.security.web.access.intercept.FilterSecurityInterceptor {

	@Override
	public void invoke(FilterInvocation fi) throws IOException,
			ServletException {
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}
}