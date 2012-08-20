package com.baishop.framework.remoting.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

import net.sf.json.JSONArray;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.jms.connection.ConnectionFactoryUtils;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;
import org.springframework.jms.support.JmsUtils;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

import com.baishop.framework.json.JsonConfigGlobal;

/**
 * 扩展JmsRpcProxyFactoryBean
 * 并根据接口名(serviceInterface)设置默认的消息目标(destination)，目标名以jms-rpc://开头。
 * @author Linpn
 */
public class JmsRpcProxyFactoryBean extends JmsInvokerProxyFactoryBean {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());	

	@SuppressWarnings("rawtypes")
	private Class serviceInterface;
	private Queue queue;
	
	
	public void afterPropertiesSet() {		
		//初始化queue
		if(this.queue==null){
			this.setQueue(new ActiveMQQueue("jms-rpc://" + serviceInterface.getName()));
		}
		
		super.afterPropertiesSet();
	}


	@SuppressWarnings("rawtypes")
	@Override
	public void setServiceInterface(Class serviceInterface) {
		super.setServiceInterface(serviceInterface);
		this.serviceInterface = serviceInterface;
	}
	
	
	@SuppressWarnings("rawtypes")
	public Class getServiceInterface() {
		return serviceInterface;
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
	
	
	/**
	 * 重写基类方法，实现返回值为void时，不等待处理结果
	 */
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {		
		//记录执行时间
        final StopWatch clock = new StopWatch();
        clock.start();
		Object result = this.invokeRequest(methodInvocation);
        clock.stop();
        

        Object[] args = methodInvocation.getArguments();  
        String sArgs = "", sResult="";
        
		if(args!=null && args.length>0)
			sArgs = JSONArray.fromObject(args, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");
		if(result!=null)
			sResult = JSONArray.fromObject(result, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");       
        
		//输出访问时间日志
		if(logger.isInfoEnabled()){
			logger.info("JMS RPC queue ["+ this.getQueue().getQueueName() +", method: "+ this.getServiceInterface().getName() +"."+ methodInvocation.getMethod().getName() +"(), time: "+ clock.getTime() +"ms]");
		}
		if(logger.isDebugEnabled()){
			logger.debug("JMS RPC args ["+ sArgs + "]");
			logger.debug("JMS RPC return ["+ sResult + "]");
		}	

		return result;		
	}
	
	
	
	/**
	 * 重写基类方法，实现返回值为void时，不等待处理结果
	 */
	public Object invokeRequest(MethodInvocation methodInvocation) 
			throws Throwable {
		
		if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
			return "JMS invoker proxy for queue [" + this.queue + "]";
		}

		RemoteInvocation invocation = createRemoteInvocation(methodInvocation);
		RemoteInvocationResult result = null;
		try {
			result = executeRequest(invocation, methodInvocation.getMethod().getReturnType());
		}
		catch (JMSException ex) {
			throw convertJmsInvokerAccessException(ex);
		}
		try {
			return recreateRemoteInvocationResult(result);
		}
		catch (Throwable ex) {
			if (result.hasInvocationTargetException()) {
				throw ex;
			}
			else {
				throw new RemoteInvocationFailureException("Invocation of method [" + methodInvocation.getMethod() +
						"] failed in JMS invoker remote service at queue [" + this.queue + "]", ex);
			}
		}
	}
	
	
	/**
	 * 重载基类方法，实现返回值为void时，不等待处理结果
	 */
	protected RemoteInvocationResult executeRequest(RemoteInvocation invocation, Class<?> returnType) throws JMSException {
		Connection con = createConnection();
		Session session = null;
		try {
			session = createSession(con);
			Queue queueToUse = resolveQueue(session);
			Message requestMessage = createRequestMessage(session, invocation);
			con.start();
			Message responseMessage = doExecuteRequest(session, queueToUse, requestMessage, returnType);
			return extractInvocationResult(responseMessage);
		}
		finally {
			JmsUtils.closeSession(session);
			ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory(), true);
		}
	}
	
	
	/**
	 * 重载基类方法，实现返回值为void时，不等待处理结果
	 */
	protected Message doExecuteRequest(Session session, Queue queue, Message requestMessage, Class<?> returnType) throws JMSException {
		TemporaryQueue responseQueue = null;
		MessageProducer producer = null;
		MessageConsumer consumer = null;
		try {
			if (session instanceof QueueSession) {
				// Perform all calls on QueueSession reference for JMS 1.0.2 compatibility...
				QueueSession queueSession = (QueueSession) session;
				responseQueue = queueSession.createTemporaryQueue();
				QueueSender sender = queueSession.createSender(queue);
				producer = sender;
				consumer = queueSession.createReceiver(responseQueue);
				requestMessage.setJMSReplyTo(responseQueue);
				sender.send(requestMessage);
			}
			else {
				// Standard JMS 1.1 API usage...
				responseQueue = session.createTemporaryQueue();
				producer = session.createProducer(queue);
				consumer = session.createConsumer(responseQueue);
				requestMessage.setJMSReplyTo(responseQueue);
				producer.send(requestMessage);
			}
			
			
			//如果接口方法返回值为void，就异步执行，不需要等待
			if(!returnType.getName().equals("void")){
				long timeout = getReceiveTimeout();
				return (timeout > 0 ? consumer.receive(timeout) : consumer.receive());
			}else{
				ObjectMessage result = new ActiveMQObjectMessage();
				result.setObject(new RemoteInvocationResult(null));
				return result;
			}
		}
		finally {
			JmsUtils.closeMessageConsumer(consumer);
			JmsUtils.closeMessageProducer(producer);
			if (responseQueue != null) {
				responseQueue.delete();
			}
		}
	}
}
