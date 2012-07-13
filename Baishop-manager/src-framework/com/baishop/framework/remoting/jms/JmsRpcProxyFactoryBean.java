package com.baishop.framework.remoting.jms;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;

public class JmsRpcProxyFactoryBean extends JmsInvokerProxyFactoryBean {


	@SuppressWarnings("rawtypes")
	@Override
	public void setServiceInterface(Class serviceInterface) {
		super.setServiceInterface(serviceInterface);
		
		//初始化destination
		this.setQueue(new ActiveMQQueue("jms-rpc://" + serviceInterface.getName()));

	}
	
}
