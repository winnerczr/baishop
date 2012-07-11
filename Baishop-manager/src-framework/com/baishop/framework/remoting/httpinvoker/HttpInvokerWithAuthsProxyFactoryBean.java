package com.baishop.framework.remoting.httpinvoker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * 扩展HttpInvokerProxyFactoryBean， 使其支持权限控制、访问监控
 * @author Linpn
 */
public class HttpInvokerWithAuthsProxyFactoryBean 
			extends HttpInvokerProxyFactoryBean implements Serializable {
	
	private static final long serialVersionUID = 621006565294449723L;
	
	private String user;
	private String password;
	private String prefix;
	
	
	private HttpInvokerWithAuthsProxyFactoryBean(){
		this.setPrefix("");
		this.setServiceUrl("");
		this.setServiceInterface(Serializable.class);
	}
	
	
	/**
	 * 重写基类方法，添加权限功能
	 */
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
			return "HTTP invoker proxy for service URL [" + getServiceUrl() + "]";
		}

		RemoteInvocation invocation = createRemoteInvocation(methodInvocation);
		RemoteInvocationResult result = null;
		try {
			// 添加参数
			Map<String, Serializable> attributes = new HashMap<String, Serializable>();
			attributes.put("user", user);
			attributes.put("password", password);
			invocation.setAttributes(attributes);
			
			result = executeRequest(invocation, methodInvocation);
		}
		catch (Throwable ex) {
			throw convertHttpInvokerAccessException(ex);
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
						"] failed in HTTP invoker remote service at [" + getServiceUrl() + "]", ex);
			}
		}
	}


	/**
	 * 设置用户名
	 * @param user 用户名 
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * 设置密码
	 * @param password 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

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
