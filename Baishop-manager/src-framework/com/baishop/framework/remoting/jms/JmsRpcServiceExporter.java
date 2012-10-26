package com.baishop.framework.remoting.jms;

import java.lang.reflect.InvocationTargetException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import net.sf.json.JSONArray;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;
import org.springframework.jms.support.JmsUtils;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

import com.baishop.framework.json.JsonConfigGlobal;

/**
 * 包装了DefaultMessageListenerContainer与JmsInvokerServiceExporter。
 * 并根据接口名(serviceInterface)设置默认的消息目标(destination)，目标名以jms-rpc://开头。
 * @author Linpn
 */
public class JmsRpcServiceExporter extends DefaultMessageListenerContainer {
	
	private Object service;

	@SuppressWarnings("rawtypes")
	private Class serviceInterface;	

	private JmsInvokerServiceExporter messageListener = new JmsInvokerServiceExporter(){
		/**
		 * 重写基类方法，添加监控功能
		 */
		protected Object invoke(RemoteInvocation invocation, Object targetObject)
				throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
			//记录执行时间
	        final StopWatch clock = new StopWatch();
	        clock.start();
			Object result = super.invoke(invocation, targetObject);		
	        clock.stop();

	        this.logger(clock, invocation, result);
			
			return result;
		}		
		
		
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

		
		/**
		 * 方法调用日志
		 */
		private void logger(final StopWatch clock, RemoteInvocation invocation, Object result){
			try{
		        Object[] args = invocation.getArguments();  
		        String sArgs = "", sResult="";
		        
		        try{
		        	//获取参数和返回值
					if(args!=null && args.length>0)
						sArgs = JSONArray.fromObject(args, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");
					if(result!=null)
						sResult = result instanceof String ? result.toString() : JSONArray.fromObject(result, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");       
		        
		        }catch(Exception e){}finally{	
					//输出访问时间日志
					if(logger.isInfoEnabled()){
						logger.info("JMS RPC method ["+ this.getServiceInterface().getName() +"."+ invocation.getMethodName() +"(), time: "+ clock.getTime() +"ms]");
					}
					if(logger.isDebugEnabled()){
						logger.debug("JMS RPC method args ["+ sArgs + "]");
						logger.debug("JMS RPC method return ["+ sResult + "]");
					}
		        }
		        
			}catch(Exception e){
				//e.printStackTrace();
			}
		}
	};
	

	@Override
	public void afterPropertiesSet() {		
		//初始化destination
		if(this.getDestination()==null)
			this.setDestination(new ActiveMQQueue("jms-rpc://" + this.serviceInterface.getName()));			
		
		messageListener.setService(service);
		messageListener.setServiceInterface(serviceInterface);
		messageListener.afterPropertiesSet();		
		super.setMessageListener(messageListener);
		super.afterPropertiesSet();	
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
	}

	/**
	 * Return the interface of the service to export.
	 */
	@SuppressWarnings("rawtypes")
	public Class getServiceInterface() {
		return this.serviceInterface;
	}
	
	
	
}
