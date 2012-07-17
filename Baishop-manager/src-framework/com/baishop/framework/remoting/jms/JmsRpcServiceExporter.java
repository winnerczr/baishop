package com.baishop.framework.remoting.jms;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;

/**
 * 包装了DefaultMessageListenerContainer与JmsInvokerServiceExporter。
 * 并根据接口名(serviceInterface)设置默认的消息目标(destination)，目标名以jms-rpc://开头。
 * @author Administrator
 *
 */
public class JmsRpcServiceExporter extends DefaultMessageListenerContainer implements InitializingBean {
	
	private Object service;

	@SuppressWarnings("rawtypes")
	private Class serviceInterface;
	

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		JmsInvokerServiceExporter messageListener = new JmsInvokerServiceExporter();
		messageListener.setService(service);
		messageListener.setServiceInterface(serviceInterface);
		messageListener.afterPropertiesSet();
		
		super.setMessageListener(messageListener);
	}
	

	
	
	/**
	 * Set the service to export.
	 * Typically populated via a bean reference.
	 */
	public void setService(Object service) {
		this.service = service;
	}

	/**
	 * Return the service to export.
	 */
	public Object getService() {
		return this.service;
	}

	/**
	 * Set the interface of the service to export.
	 * The interface must be suitable for the particular service and remoting strategy.
	 */
	@SuppressWarnings("rawtypes")
	public void setServiceInterface(Class serviceInterface) {
		if (serviceInterface != null && !serviceInterface.isInterface()) {
			throw new IllegalArgumentException("'serviceInterface' must be an interface");
		}
		this.serviceInterface = serviceInterface;
		
		//初始化destination
		if(this.getDestination()==null)
			this.setDestination(new ActiveMQQueue("jms-rpc://" + this.serviceInterface.getName()));
	}

	/**
	 * Return the interface of the service to export.
	 */
	@SuppressWarnings("rawtypes")
	public Class getServiceInterface() {
		return this.serviceInterface;
	}
	
}
