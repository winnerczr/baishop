package com.baishop.framework.remoting.httpinvoker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.time.StopWatch;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.web.util.NestedServletException;

import com.baishop.framework.json.JsonConfigGlobal;


/**
 * 扩展HttpInvokerServiceExporter
 * @author Linpn
 */
public class HttpInvokerServiceExporter 
		extends org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter {
	
	
	/**
	 * 重写基类方法，添加计时功能
	 */
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			//记录执行时间
	        final StopWatch clock = new StopWatch();
	        clock.start();
	
			RemoteInvocation invocation = readRemoteInvocation(request);
			RemoteInvocationResult result = invokeAndCreateResult(invocation, getProxy());
			writeRemoteInvocationResult(request, response, result);
			
	        clock.stop();
	        
	        
	        Object[] args = invocation.getArguments();  
	        String sArgs = "", sResult="";
	        
			if(args!=null && args.length>0)
				sArgs = JSONArray.fromObject(args, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");
			if(result!=null)
				sResult = JSONArray.fromObject(result.getValue(), JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");       
	        
			//输出访问时间日志
			if(logger.isInfoEnabled()){
				logger.info("SOA method ["+ invocation.getMethodName() +", time: "+ clock.getTime() +"ms]");
			}
			if(logger.isDebugEnabled()){
				logger.debug("SOA method args ["+ sArgs + "]");
				logger.debug("SOA method return ["+ sResult + "]");
			}
		
		}
		catch (ClassNotFoundException ex) {
			throw new NestedServletException("Class not found during deserialization", ex);
		}
	}

}
