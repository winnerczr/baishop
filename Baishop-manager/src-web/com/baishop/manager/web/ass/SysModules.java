package com.baishop.manager.web.ass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.ass.Admins;
import com.baishop.entity.ass.Modules;
import com.baishop.entity.ass.Roles;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;
import com.baishop.manager.security.FilterInvocationSecurityMetadataSource;

public class SysModules extends PageManagerController {

	@Autowired
	private FilterSecurityInterceptor filterSecurityInterceptor;

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {

	}
	
	
	/**
	 * Ajax请求：获取用户应用模块列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getModules(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//组装JSON
			JSONObject json = modulesService.getTreeModulesOfJSON(null,null,null);

			//输出数据
			out.println(json);
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
		
	/**
	 * Ajax请求：保存应用模块
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveModules(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{			
			//创建对象
			Modules module = request.getBindObject(Modules.class, "module");
			
			if(module.getModuleId()==null){
				//添加对象
				modulesService.addModules(module);
			}else{
				//编辑对象
				modulesService.editModules(module);
			}
			
			//输出数据
			out.println("{success: true}");
			((FilterInvocationSecurityMetadataSource)filterSecurityInterceptor.getSecurityMetadataSource()).clearSecuritySource();

		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}	
	
	
	/**
	 * Ajax请求：删除应用模块
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delModules(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			Integer moduleId = request.getIntParameter("moduleId");	
			
			modulesService.delModules(moduleId);
			
			//输出数据
			out.println("{success: true}");
			((FilterInvocationSecurityMetadataSource)filterSecurityInterceptor.getSecurityMetadataSource()).clearSecuritySource();
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：上移应用模块
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void upModules(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String moduleId = request.getParameter("moduleId");
			String prevModuleId = request.getParameter("prevModuleId");
			
			Modules module = modulesService.getModules(Integer.valueOf(moduleId));
			Modules prevModule = modulesService.getModules(Integer.valueOf(prevModuleId));
			
			//排序对调
			int temp = module.getSort();
			module.setSort(prevModule.getSort());
			prevModule.setSort(temp);
			
			modulesService.editModules(module);
			modulesService.editModules(prevModule);
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：下移应用模块
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void downModules(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String moduleId = request.getParameter("moduleId");
			String nextModuleId = request.getParameter("nextModuleId");
			
			Modules module = modulesService.getModules(Integer.valueOf(moduleId));
			Modules nextModule = modulesService.getModules(Integer.valueOf(nextModuleId));
			
			//排序对调
			int temp = module.getSort();
			module.setSort(nextModule.getSort());
			nextModule.setSort(temp);
			
			modulesService.editModules(module);
			modulesService.editModules(nextModule);
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：通过模块ID获取用户ID，包含角色关联的用户
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getUsersByModules(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String callback=request.getParameter("callback");
		
		try{
			Integer moduleId = request.getIntParameter("moduleId");
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("moduleId", moduleId);		
			List<Admins> list = adminsService.getAdminsList(params);
			
			//组装JSON
			JSONObject json = new JSONObject();
			json.put("records", new JSONArray());
			
			JSONArray records = json.getJSONArray("records");
			for(Admins admins : list) {
				JSONObject record = new JSONObject();
				record.put("userId", admins.getUserId());
				record.put("username", admins.getUsername());
				record.put("code", admins.getCode());
				record.put("name", admins.getName());
				
				records.add(record);
			}
			
			//输出数据
			out.println(callback + "("+ json +")");
			
		}catch(Exception e){
			out.println(callback + "({success: false, msg: '"+ e.getMessage() +"'})");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：通过模块ID获取角色ID
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getRolesByModules(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			Integer moduleId = request.getIntParameter("moduleId");
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("moduleId", moduleId);			
			List<Roles> list = rolesService.getRolesList(params);
			
			//组装JSON
			JSONArray json = new JSONArray();
			
			for(Roles role : list){
				json.add(role.getRoleId());
			}
			
			//输出数据
			out.println(json);
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}

}
