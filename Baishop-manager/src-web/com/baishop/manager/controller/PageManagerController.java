package com.baishop.manager.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.ass.Admins;
import com.baishop.entity.ass.Modules;
import com.baishop.framework.utils.PropsConf;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.framework.web.controller.PageController;
import com.baishop.service.ass.AdminsService;
import com.baishop.service.ass.EnumsService;
import com.baishop.service.ass.ModulesService;
import com.baishop.service.ass.ParamsService;
import com.baishop.service.ass.RolesService;

/**
 * 页面MVC控制层基类，主要编写公共方法
 * @author Linpn
 */
public abstract class PageManagerController extends PageController {

	/** 系统基础配置，读取app.conf里的信息 */
	@Autowired 	
	protected PropsConf appConf;	
	/** 系统用户服务类 */
	@Autowired
	protected AdminsService adminsService;	
	/** 用户角色服务类 */
	@Autowired
	protected RolesService rolesService;	
	/** 应用模块服务类 */
	@Autowired
	protected ModulesService modulesService;
	/** 系统参数服务类 */
	@Autowired
	protected ParamsService paramsService;	
	/** 系统枚举服务类 */
	@Autowired
	protected EnumsService enumsService;
	
	
	
	/**
	 * 初始化ModelAndView
	 * @param request
	 * @param response
	 * @param modeview
	 */
	@Override
	protected void initModelAndView(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) throws Exception {

		//请求路径
		modeview.addObject("page_context", request.getContextPath());
		modeview.addObject("page_action", request.getContextPath() + request.getServletPath());
		
		//系统参数
		modeview.addObject("app_title", appConf.getProperty("app.title", "", "UTF-8"));		
		modeview.addObject("mall_keywords", paramsService.getParams("mall_keywords").getParamsValue());
		modeview.addObject("mall_description", paramsService.getParams("mall_description").getParamsValue());
		modeview.addObject("mall_copyright", paramsService.getParams("mall_copyright").getParamsValue());
		
		//模块信息
		String func = request.getParameter("func");
		String path = request.getContextPath() + request.getServletPath() + (func!=null ? "?func="+func : "");  
		Modules module = modulesService.getModulesByUrl(path);
		if(module!=null){
			modeview.addObject("module_title", module.getText());
			modeview.addObject("module_icon", module.getIconCls());
		}
	}
	
	
	
	/**
	 * 获取当前用户
	 * @return
	 */
	public Admins getCurrUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null)
			return null;

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return (Admins)userDetails;
	}
	
	/**
	 * 用户登录操作
	 * @param request request对象
	 * @param response response对象
	 * @param username 用户名
	 * @param password 密码
	 * @return 返回是否登录成功
	 */
	public void login(HttpServletRequest request, HttpServletResponse response, String username, String password) {
		this.login(request, response, username, password, request.getContextPath());
	}
	
	/**
	 * 用户登录操作
	 * @param request request对象
	 * @param response response对象
	 * @param username 用户名
	 * @param password 密码
	 * @param url 执行成功后跳转的地址
	 * @return 返回是否登录成功
	 */
	public void login(HttpServletRequest request, HttpServletResponse response, String username, String password, String url) {
		try {
			HttpClient httpclient = new DefaultHttpClient();			
			HttpHost target = new HttpHost(request.getServerName(), request.getServerPort(), request.getScheme());
			HttpPost post = new HttpPost(request.getContextPath() + "/j_spring_security_check?j_username="+username+"&j_password="+password);
		
			//复制request的header
			Enumeration<String> reqHeaders = request.getHeaderNames();
			while(reqHeaders.hasMoreElements()){
				String name = reqHeaders.nextElement();
				post.addHeader(name, request.getHeader(name));
			}
			
			//设置response的header
			HttpResponse res = httpclient.execute(target, post);			
			for(Header header : res.getAllHeaders()){
				response.addHeader(header.getName(), header.getValue());
			}
			
			//登录成功，跳转
			response.sendRedirect(url);
			throw new RuntimeException("登录成功，请忽略此异常");

		} catch (Exception e) {
			if(e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 用户登出操作
	 * @param request request对象
	 * @param response response对象
	 * @return 返回是否登录成功
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		this.logout(request, response, request.getContextPath());
	}
	
	/**
	 * 用户登出操作
	 * @param request request对象
	 * @param response response对象
	 * @param url 执行成功后跳转的地址
	 * @return 返回是否登录成功
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response, String url) {
		try {
			HttpClient httpclient = new DefaultHttpClient();			
			HttpHost target = new HttpHost(request.getServerName(), request.getServerPort(), request.getScheme());
			HttpPost post = new HttpPost(request.getContextPath() + "/j_spring_security_logout");

			//获取request的header
			Enumeration<String> reqHeaders = request.getHeaderNames();
			while(reqHeaders.hasMoreElements()){
				String name = reqHeaders.nextElement();
				post.addHeader(name, request.getHeader(name));
			}
			
			//设置response的header
			HttpResponse res = httpclient.execute(target, post);			
			for(Header header : res.getAllHeaders()){
				response.addHeader(header.getName(), header.getValue());
			}
			
			//登出成功，跳转
			response.sendRedirect(url);
			throw new RuntimeException("登出成功，请忽略此异常");

		} catch (Exception e) {
			if(e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new RuntimeException(e);
		}
	}
	

	//------------------------------------- 公共方法 ----------------------------------------------//
	
	/**
	 * 执行首页的公共代码
	 * @param request
	 * @param response
	 * @param modeview
	 */
	protected void doMainPage(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview){
		// 获取当前用户有权限的模块树
		JSONObject modules = modulesService.getTreeModulesOfJSON(this.getCurrUser(),
															this.appConf.getProperty("app.name", "", "UTF-8"),
															new String[] { 
																ModulesService.SYSTEM,
																ModulesService.GROUP,
																ModulesService.MODULE
															});
		JSONArray treeModules = modules.getJSONArray("children");
		if(treeModules.size()>0)
			modeview.addObject("treeModules", treeModules.get(0));
		else
			modeview.addObject("treeModules", "{}");
		
		// 获取所有包含URL的模块
		modeview.addObject("leafModules", modulesService.getLeafModulesOfJSON());
		
		// 获取所有子系统
		modeview.addObject("listSystems", modulesService.getListSystemsOfJSON());
	}

}
