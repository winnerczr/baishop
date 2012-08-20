package com.baishop.framework.remoting.httpinvoker;

import java.lang.reflect.InvocationTargetException;

import net.sf.json.JSONArray;

import org.apache.commons.lang.time.StopWatch;
import org.springframework.remoting.support.RemoteInvocation;

import com.baishop.framework.json.JsonConfigGlobal;


/**
 * 扩展HttpInvokerServiceExporter
 * @author Linpn
 */
public class HttpInvokerServiceExporter 
		extends org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter {
	
	
	/**
	 * 重写基类方法，添加监控功能
	 */
	@Override
	protected Object invoke(RemoteInvocation invocation, Object targetObject)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
		//记录执行时间
        final StopWatch clock = new StopWatch();
        clock.start();
        Object result = super.invoke(invocation, targetObject);
        clock.stop();
        
        
        Object[] args = invocation.getArguments();  
        String sArgs = "", sResult="";
        
		if(args!=null && args.length>0)
			sArgs = JSONArray.fromObject(args, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");
		if(result!=null)
			sResult = result instanceof String ? result.toString() : JSONArray.fromObject(result, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");       
        
		//输出访问时间日志
		if(logger.isInfoEnabled()){
			logger.info("SOA method ["+ this.getServiceInterface().getName() +"."+ invocation.getMethodName() +"(), time: "+ clock.getTime() +"ms]");
		}
		if(logger.isDebugEnabled()){
			logger.debug("SOA method args ["+ sArgs + "]");
			logger.debug("SOA method return ["+ sResult + "]");
		}
		
		return result;
	}
	
}
