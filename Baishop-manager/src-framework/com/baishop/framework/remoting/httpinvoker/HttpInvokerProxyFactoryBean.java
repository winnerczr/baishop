package com.baishop.framework.remoting.httpinvoker;

import net.sf.json.JSONArray;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.time.StopWatch;

import com.baishop.framework.json.JsonConfigGlobal;


/**
 * 扩展HttpInvokerProxyFactoryBean
 * @author Linpn
 */
public class HttpInvokerProxyFactoryBean 
			extends org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean {
	
	private String prefix;
	
	
	/**
	 * 重写基类方法，添加计时功能
	 */
	@Override
	public Object invoke(MethodInvocation methodInvocation) 
			throws Throwable {

		//记录执行时间
        final StopWatch clock = new StopWatch();
        clock.start();
		Object result = super.invoke(methodInvocation);
        clock.stop();
        

        Object[] args = methodInvocation.getArguments();  
        String sArgs = "", sResult="";
        
		if(args!=null && args.length>0)
			sArgs = JSONArray.fromObject(args, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");
		if(result!=null)
			sResult = JSONArray.fromObject(result, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");       
        
		//输出访问时间日志
		if(logger.isInfoEnabled()){
			logger.info("SOA URL path ["+ this.getServiceUrl() +", time: "+ clock.getTime() +"ms]");
		}
		if(logger.isDebugEnabled()){
			logger.debug("SOA args ["+ sArgs + "]");
			logger.debug("SOA return ["+ sResult + "]");
		}		

		return result;		
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
