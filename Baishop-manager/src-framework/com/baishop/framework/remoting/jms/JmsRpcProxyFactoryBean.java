package com.baishop.framework.remoting.jms;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;

public class JmsRpcProxyFactoryBean extends JmsInvokerProxyFactoryBean {
	
	private Queue queue;


	@SuppressWarnings("rawtypes")
	@Override
	public void setServiceInterface(Class serviceInterface) {
		super.setServiceInterface(serviceInterface);
		
		//初始化queue
		if(this.queue==null){
			this.setQueue(new ActiveMQQueue("jms-rpc://" + serviceInterface.getName()));
		}
	}
	
	
	/**
	 * Set the target Queue to send invoker requests to.
	 */
	@Override
	public void setQueue(Queue queue) {
		super.setQueue(queue);
		this.queue = queue;
	}
	
	/**
	 * Get the target Queue to send invoker requests to.
	 */
	public Queue getQueue() {
		return this.queue;
	}
	
}
