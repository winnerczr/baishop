package com.baishop.framework.web.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.framework.web.wrapper.DefaultHttpServletExtendRequest;
import com.baishop.framework.web.wrapper.DefaultHttpServletExtendResponse;



/**
 * 页面MVC控制层基类
 * @author Linpn
 */
public abstract class PageController implements Controller {
	
	/**
	 * Spring MVC设置的视图
	 */
	protected String view;

	/**
	 * 获取View页面，默认为跟本类同名同目录的视图文件
	 * @return 返回页面视图路径
	 */
	public String getView() {
		return view;
	}

	/**
	 * 设置View页面，默认为跟本类同名同目录的视图文件
	 * @param view 页面视图路径
	 */
	public void setView(String view) {
		this.view = view;
	}

	
	
	/**
	 * 处理DispatcherServlet的请求，并返回ModelAndView对象，显示view。
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		this.setHTMLHeader(request,response,"utf-8");
		
		//开始业务处理
		String func = request.getParameter("func");
		if(func==null){
			return this.doExecute(new DefaultHttpServletExtendRequest(request), new DefaultHttpServletExtendResponse(response));							
		}else{
			return this.doExecuteFunc(new DefaultHttpServletExtendRequest(request), new DefaultHttpServletExtendResponse(response), func);
		}		
	}
		
	
	/**
	 * 当请求无func参数时,返回页面view
	 */
	private ModelAndView doExecute(HttpServletExtendRequest request,
			HttpServletExtendResponse response) throws Exception {
		ModelAndView modeview = new ModelAndView(view);
		this.initModelAndView(request, response, modeview);
		this.execute(request,response,modeview);		
		return modeview;
	}
	
	
	/**
	 * 当请求有func参数时,执行func方法请求
	 */
	private ModelAndView doExecuteFunc(HttpServletExtendRequest request,
			HttpServletExtendResponse response, String func) throws Exception {
		Method method = this.getClass().getMethod(func, HttpServletExtendRequest.class, HttpServletExtendResponse.class);
		Object result = method.invoke(this, request, response);
		
		if(result instanceof ModelAndView){
			ModelAndView modeview = (ModelAndView)result;
			this.initModelAndView(request, response, modeview);
			return modeview;
		}else if(result instanceof String){
			ModelAndView modeview = new ModelAndView((String)result);
			this.initModelAndView(request, response, modeview);
			return modeview;
		}else{
			return null;
		}
	}
	
	
	/**
	 * 初始化ModelAndView
	 * @param request HttpServletExtendRequest对象
	 * @param response HttpServletExtendResponse对象
	 * @param modeview ModelAndView对象
	 */
	protected void initModelAndView(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) throws Exception {
	}
	
	
	/**
	 * 页面入口方法
	 * @param request HttpServletExtendRequest对象
	 * @param response HttpServletExtendResponse对象
	 * @param modeview ModelAndView对象
	 */
	public abstract void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview);	
		
	
	
	/**
	 * 设置输出XML
	 * @param request HttpServletRequest对象
	 * @param response HttpServletResponse对象
	 * @param encoding 编码,如"utf-8"
	 */
	public void setXMLHeader(HttpServletRequest request,
			HttpServletResponse response,String encoding){
		try{
			request.setCharacterEncoding(encoding);
			response.setContentType("text/xml");
			response.setCharacterEncoding(encoding);
			response.setHeader("Cache-Control","no-cache");             
			response.setHeader("Pragma","no-cache"); 
			response.setDateHeader("Expires",0); 
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 设置输出HTML
	 * @param request HttpServletRequest对象
	 * @param response HttpServletResponse对象
	 * @param encoding 编码,如"utf-8"
	 */
	public void setHTMLHeader(HttpServletRequest request,
			HttpServletResponse response,String encoding){
		try{
			request.setCharacterEncoding(encoding);
			response.setContentType("text/html");
			response.setCharacterEncoding(encoding);
			response.setHeader("Cache-Control","no-cache");             
			response.setHeader("Pragma","no-cache"); 
			response.setDateHeader("Expires",0); 
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
}
