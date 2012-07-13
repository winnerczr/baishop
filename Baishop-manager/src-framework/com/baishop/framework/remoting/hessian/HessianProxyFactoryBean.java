package com.baishop.framework.remoting.hessian;


public class HessianProxyFactoryBean extends org.springframework.remoting.caucho.HessianProxyFactoryBean {
	
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
