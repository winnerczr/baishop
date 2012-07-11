package com.baishop.framework.remoting.hessian;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;

public class HessianWithAuthsProxyFactoryBean extends HessianProxyFactoryBean {
	
	private String prefix;
	
	
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
