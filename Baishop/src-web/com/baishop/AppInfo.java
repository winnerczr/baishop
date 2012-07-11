package com.baishop;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public final class AppInfo implements ServletContextListener,HttpSessionListener {
	
	/**
	 * 容器加载Web应用程序时
	 */
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("baishop-web - contextInitialized");
	}
	
	
	/**
	 * 建立Session对象时
	 */
	public void sessionCreated(HttpSessionEvent hse) {
		System.out.println("baishop-web - sessionCreated");
	}
	
	
	/**
	 * 销毁Session对象时
	 */
	public void sessionDestroyed(HttpSessionEvent hse) {
		System.out.println("baishop-web - sessionDestroyed");
	}	
	
	
	/**
	 * 容器移除Web应用程序时
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("baishop-web - contextDestroyed");
	}

}
