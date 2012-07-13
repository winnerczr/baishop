package com.baishop.framework.remoting.jms;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;

/**
 * 包装了DefaultMessageListenerContainer与JmsInvokerServiceExporter。
 * 并根据接口名(serviceInterface)设置默认的消息目标(destination)，目标名以jms-rpc://开头。
 * @author Administrator
 *
 */
public class JmsRpcServiceExporter extends DefaultMessageListenerContainer {

	/**
	 * Set the service to export.
	 * Typically populated via a bean reference.
	 */
	public void setService(Object service) {
		if(this.getMessageListener()==null){
			this.setMessageListener(new JmsInvokerServiceExporter());
		}
		
		((JmsInvokerServiceExporter)this.getMessageListener()).setService(service);
	}

	/**
	 * Return the service to export.
	 */
	public Object getService() {
		if(this.getMessageListener()==null){
			this.setMessageListener(new JmsInvokerServiceExporter());
		}
		
		return ((JmsInvokerServiceExporter)this.getMessageListener()).getService();
	}
	
	/**
	 * Set the interface of the service to export.
	 * The interface must be suitable for the particular service and remoting strategy.
	 */
	@SuppressWarnings("rawtypes")
	public void setServiceInterface(Class serviceInterface) {
		if(this.getMessageListener()==null){
			this.setMessageListener(new JmsInvokerServiceExporter());
		}
		
		((JmsInvokerServiceExporter)this.getMessageListener()).setServiceInterface(serviceInterface);
		
		//初始化destination
		this.setDestination(new ActiveMQQueue("jms-rpc://" + serviceInterface.getName()));
	}

	/**
	 * Return the interface of the service to export.
	 */
	@SuppressWarnings("rawtypes")
	public Class getServiceInterface() {
		if(this.getMessageListener()==null){
			this.setMessageListener(new JmsInvokerServiceExporter());
		}
		
		return ((JmsInvokerServiceExporter)this.getMessageListener()).getServiceInterface();
	}
	
	
}
