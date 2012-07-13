package com.baishop.framework.remoting.jms;

import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.remoting.support.RemoteExporter;

/**
 * 包装了DefaultMessageListenerContainer与JmsInvokerServiceExporter。
 * 并根据接口名(serviceInterface)设置默认的消息目标(destination)，目标名以jms-rpc://开头。
 * @author Administrator
 *
 */
public class JmsRpcServiceExporter extends DefaultMessageListenerContainer {
	
	
	/**
	 * Set the message listener implementation to register.
	 * This can be either a standard JMS {@link MessageListener} object
	 * or a Spring {@link SessionAwareMessageListener} object.
	 * <p>Note: The message listener may be replaced at runtime, with the listener
	 * container picking up the new listener object immediately (works e.g. with
	 * DefaultMessageListenerContainer, as long as the cache level is less than
	 * CACHE_CONSUMER). However, this is considered advanced usage; use it with care!
	 * @throws IllegalArgumentException if the supplied listener is not a
	 * {@link MessageListener} or a {@link SessionAwareMessageListener}
	 * @see javax.jms.MessageListener
	 * @see SessionAwareMessageListener
	 */
	@Override
	public void setMessageListener(Object messageListener) {
		super.setMessageListener(messageListener);
		
		//初始化destination
		if(this.getDestination()==null)
			this.setDestination(new ActiveMQQueue("jms-rpc://" + ((RemoteExporter)messageListener).getServiceInterface().getName()));
	}
	
}
