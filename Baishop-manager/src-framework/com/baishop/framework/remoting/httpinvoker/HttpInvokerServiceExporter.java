package com.baishop.framework.remoting.httpinvoker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 扩展HttpInvokerServiceExporter
 * @author Linpn
 */
public class HttpInvokerServiceExporter 
		extends org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter {
	
	
	/**
	 * 重写基类方法
	 */
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.handleRequest(request, response);
	}

}
