package com.baishop.framework.remoting.httpinvoker;

import org.aopalliance.intercept.MethodInvocation;


/**
 * 扩展HttpInvokerProxyFactoryBean
 * @author Linpn
 */
public class HttpInvokerProxyFactoryBean 
			extends org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean {
	
	private String prefix;
	
	
	/**
	 * 重写基类方法，添加计时功能
	 */
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object result = super.invoke(methodInvocation);
		return result;		
	}
	
	
	
	/**
	 * 设置URL前缀
	 * @param prefix URL前缀
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * 设置URL
	 */
	@Override
	public void setServiceUrl(String serviceUrl) {
		super.setServiceUrl(this.prefix + serviceUrl);
	}

}
