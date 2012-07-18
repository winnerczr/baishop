package com.baishop.framework.remoting.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;
import org.springframework.jms.support.JmsUtils;
import org.springframework.remoting.support.RemoteInvocationResult;

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
		
		JmsInvokerServiceExporter messageListener = new JmsInvokerServiceExporter(){
			/**
			 * 重写基类方法，使其的返回值为void时，不推送消息
			 */
			@Override
			protected void writeRemoteInvocationResult(
					Message requestMessage, Session session, RemoteInvocationResult result) throws JMSException {
				//判断返回值是否为void
				if(result.getValue()!=null){
					Message response = createResponseMessage(requestMessage, session, result);
					MessageProducer producer = session.createProducer(requestMessage.getJMSReplyTo());
					try {
						producer.send(response);
					}
					finally {
						JmsUtils.closeMessageProducer(producer);
					}
				}
			}
		};
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
