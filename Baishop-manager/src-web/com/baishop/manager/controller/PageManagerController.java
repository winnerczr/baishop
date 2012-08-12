package com.baishop.manager.controller;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
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
import com.baishop.entity.ass.Depts;
import com.baishop.entity.ass.Modules;
import com.baishop.entity.ass.Roles;
import com.baishop.entity.goods.Category;
import com.baishop.framework.exception.ServiceException;
import com.baishop.framework.utils.PropsConf;
import com.baishop.framework.utils.TreeRecursiveHandle;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.framework.web.controller.PageController;
import com.baishop.service.ass.AdminsService;
import com.baishop.service.ass.CityService;
import com.baishop.service.ass.DeptsService;
import com.baishop.service.ass.EnumsService;
import com.baishop.service.ass.ModulesService;
import com.baishop.service.ass.ParamsService;
import com.baishop.service.ass.RolesService;
import com.baishop.service.goods.CategoryService;

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
	/** 组织架构服务类 */
	@Autowired
	protected DeptsService deptsService;	
	/** 行政区划服务类 */
	@Autowired
	protected CityService cityService;	
	/** 系统参数服务类 */
	@Autowired
	protected ParamsService paramsService;	
	/** 系统枚举服务类 */
	@Autowired
	protected EnumsService enumsService;
	/** 商品类目服务类  */
	@Autowired
	protected CategoryService categoryService;
	
	
	
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
	 * 获取子系统、模块、操作的map集合
	 * @return 返回JSON对象
	 */
	public JSONArray getListSystemsOfJSON(){		
		JSONArray json = new JSONArray();		
		List<Modules> list = this.modulesService.getModulesList(null);
		
		for(Modules module : list){		
			//添加最叶子节点到列表中
			if(ModulesService.SYSTEM.equals(module.getType())){
				JSONObject m = new JSONObject();
				m.put("id", module.getModuleId());
				m.put("cls", module.getText());
				json.add(m);
			}
		}
		
		return json;
	}
	
	
	/**
	 * 获取JSON格式的树型模块
	 * @param user 后台用户对象,如果user为null，则查出所有的模块
	 * @param subsystem 后台子系统,如果为null，则查出所有子系统的模块
	 * @param types 模块类型,值为"SYSTEM"、"GROUP"、"MODULE"、"FUNCTION"， 如果为null，则查出所有的模块
	 * @return 返回JSON对象
	 */
	public JSONObject getTreeModulesOfJSON(Admins user, final String subsystem, final String[] types) {
		final JSONObject json = new JSONObject();
		
		try {
			json.put("moduleId", 0);
			json.put("id", 0);
			json.put("text", "应用模块");
			json.put("iconCls", "icon-docs");
			json.put("children", new JSONArray());
			
			List<Modules> list;
			if(user==null)
				list = this.modulesService.getModulesList(null);
			else
				list = this.modulesService.getModulesListByUser(user, false);
			
						
			//递归加载
			TreeRecursiveHandle<Modules> treeRecursiveHandle = new TreeRecursiveHandle<Modules>(){
				public void recursive(List<Modules> list, JSONObject treeNode) throws JSONException{
					for(Modules module : list){
						if(module.getModulePid().equals(treeNode.getInt("id"))){	
							//判断是不是当前子系统
							if(subsystem!=null){
								if(module.getModulePid()==0 || module.getType().equals(ModulesService.SYSTEM)){
									if(!module.getText().equals(subsystem)){
										continue;
									}
								}
							}
							
							//判断是否匹配要查询的类型
							if(types!=null){
								boolean isMatch = false; 
								for(String type : types){
									if(type.equals(module.getType())){
										isMatch = true;
										break;
									}
								}
								if(!isMatch){
									continue;
								}
							}
							
							
							//获取JSON对象
							JSONObject node = JSONObject.fromObject(module);
							
							node.put("id", module.getModuleId());
							node.put("text", module.getText());
							node.put("leaf", true);

							node.put("nExpanded", module.getExpanded());
							if(module.getExpanded()==1)
								node.put("expanded", true);
							else
								node.put("expanded", false);
														
							//递归
							this.recursive(list, node);
							
							//添加到树中
							JSONArray children;
							try {
								children = treeNode.getJSONArray("children");
							} catch (JSONException e) {
								treeNode.put("children", new JSONArray());
								children = treeNode.getJSONArray("children");
							}							
							children.add(node);
							treeNode.put("leaf", false);
						}
					}
				}
			};
			
			treeRecursiveHandle.recursive(list, json);
			
		} catch (Exception e) {
			throw new ServiceException(902001, e);
		}
		
		return json;
	}
	

	
	/**
	 * 获取子系统、模块、操作的map集合
	 * @return 返回JSON对象
	 */
	public JSONObject getLeafModulesOfJSON(){		
		JSONObject json = new JSONObject();		
		List<Modules> list = this.modulesService.getModulesList(null);
		
		for(Modules module : list){		
			//添加最叶子节点到列表中
			if(ModulesService.SYSTEM.equals(module.getType()) || ModulesService.MODULE.equals(module.getType()) || ModulesService.FUNCTION.equals(module.getType())){
				json.put(module.getText(), module.getUrl());
			}
		}
		
		return json;
	}
	
	
	/**
	 * 获取JSON格式的树型角色
	 * @return 返回JSON对象，json.get("leafMap")可以获取叶子节点集合
	 */
	public JSONObject getTreeRolesOfJSON() {
		final JSONObject json = new JSONObject();
		
		try {
			json.put("id", 0);
			json.put("text", "用户角色");
			json.put("roleId", 0);
			json.put("roleName", "用户角色");
			json.put("iconCls", "icon-docs");
			json.put("children", new JSONArray());
			json.put("leafMap", new JSONObject());
			
			List<Roles> list = this.rolesService.getRolesList(null);			
			
			//递归加载
			TreeRecursiveHandle<Roles> treeRecursiveHandle = new TreeRecursiveHandle<Roles>(){
				public void recursive(List<Roles> list, JSONObject treeNode) throws JSONException{
					for(Roles role : list){
						if(role.getRolePid().equals(treeNode.getInt("id"))){					
							
							JSONObject node = JSONObject.fromObject(role);
							
							node.put("id", role.getRoleId());
							node.put("text", role.getRoleName());
							node.put("expanded", true);
							node.put("leaf", true);
							node.put("nLeaf", role.getLeaf());
							
							//图标
							if(role.getLeaf()==0)
								node.put("iconCls", "icon-role-group");
							else
								node.put("iconCls", "icon-role-leaf");
							
							//角色中的模块ID
							JSONArray modules = new JSONArray();
							for(Modules module : role.getModules()){
								modules.add(module.getModuleId());
							}				
							node.put("modules", modules);
							
							//递归
							this.recursive(list, node);
							
							//添加到树中
							JSONArray children;
							try {
								children = treeNode.getJSONArray("children");
							} catch (JSONException e) {
								treeNode.put("children", new JSONArray());
								children = treeNode.getJSONArray("children");
							}							
							children.add(node);
							treeNode.put("leaf", false);						
							
							//添加最叶子节点到列表中
							if(role.getLeaf()==1){
								json.getJSONObject("leafMap").put(role.getRoleId(), role.getRoleName());
							}
						}
					}
				}
			};
			
			treeRecursiveHandle.recursive(list, json);
			
		} catch (Exception e) {
			throw new ServiceException(902001, e);
		}
		
		return json;
	}
	
	
	
	/**
	 * 获取JSON格式的树型部门
	 * @return 返回JSON对象，json.get("cbbDept")可以获取combobox所需要的格式
	 */
	public JSONObject getTreeDeptOfJSON() {
		final JSONObject json = new JSONObject();
		
		try {
			json.put("id", 0);
			json.put("text", "组织架构");
			json.put("deptId", 0);
			json.put("deptName", "组织架构");
			json.put("iconCls", "icon-dept");
			json.put("children", new JSONArray());
			json.put("cbbDept", new JSONArray());
			
			List<Depts> list = this.deptsService.getDeptsList(null);			
			
			//递归加载
			TreeRecursiveHandle<Depts> treeRecursiveHandle = new TreeRecursiveHandle<Depts>(){
				public void recursive(List<Depts> list, JSONObject treeNode) throws JSONException{
					for(Depts dept : list){
						if(dept.getDeptPid().equals(treeNode.getInt("id"))){					
							
							JSONObject node = JSONObject.fromObject(dept);
							
							node.put("id", dept.getDeptId());
							node.put("text", dept.getDeptName());
							node.put("expanded", true);
							node.put("leaf", true);
							node.put("iconCls", "icon-dept");
							
							//递归
							this.recursive(list, node);
							
							//添加到树中
							JSONArray children;
							try {
								children = treeNode.getJSONArray("children");
							} catch (JSONException e) {
								treeNode.put("children", new JSONArray());
								children = treeNode.getJSONArray("children");
							}							
							children.add(node);
							treeNode.put("leaf", false);						
							
							//添加最叶子节点到列表中
							json.getJSONArray("cbbDept").add(JSONArray.fromObject(new Object[]{dept.getDeptId(), dept.getDeptName()}));
						}
					}
				}
			};
			
			treeRecursiveHandle.recursive(list, json);
			
		} catch (Exception e) {
			throw new ServiceException(902001, e);
		}
		
		return json;
	}	
	
	
	/**
	 * 获取JSON格式的类目树
	 * @return
	 */
	public JSONObject getTreeCategoryOfJSON() {
		JSONObject json = new JSONObject();
		
		try {
			json.put("id", 0);
			json.put("text", "商品类目");
			json.put("iconCls", "icon-docs");
			
			List<Category> list = categoryService.getCategoryList();
			
			//递归加载
			TreeRecursiveHandle<Category> treeRecursiveHandle = new TreeRecursiveHandle<Category>(){
				public void recursive(List<Category> list, JSONObject treeNode) throws JSONException{
					for(Category cate : list){
						if(cate.getCateParent().equals(treeNode.getInt("id"))){
							JSONObject node = JSONObject.fromObject(cate);
							
							node.put("id", cate.getCateId());
							node.put("text", cate.getCateName());
							node.put("expanded", true);
							node.put("leaf", true);
							
							this.recursive(list, node);
							
							JSONArray children;
							try {
								children = treeNode.getJSONArray("children");
							} catch (JSONException e) {
								treeNode.put("children", new JSONArray());
								children = treeNode.getJSONArray("children");
							}
							children.add(node);
							treeNode.put("leaf", false);
						}
					}
				}
			};
			
			treeRecursiveHandle.recursive(list, json);
			
		} catch (Exception e) {
			throw new ServiceException(902001, e);
		}
		
		return json;
	}

	
	/**
	 * 获取JSON格式的叶子列表
	 * @return
	 */
	public JSONArray getCbbLeafCategoryOfJSON() {
		JSONArray json = new JSONArray();
		
		List<Category> list = categoryService.getCategoryList();
		
		for(Category cate : list){
			if(cate.getLeaf()!=1)
				continue;
			
			JSONArray node = new JSONArray();
			node.add(cate.getCateId());
			
			String name = cate.getCateName();			
			int pid = cate.getCateParent();
			while(pid>0){
				Category _cate = categoryService.getCategory(pid);
				pid = _cate.getCateParent();
				name = _cate.getCateName() + ">>" + name;
			}
			
			node.add(name);
			json.add(node);
		}
		
		return json;
	}
	
}
