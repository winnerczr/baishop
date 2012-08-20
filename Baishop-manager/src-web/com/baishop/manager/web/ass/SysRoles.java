package com.baishop.manager.web.ass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.baishop.framework.utils.ConvertUtils;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;
import com.baishop.manager.security.FilterInvocationSecurityMetadataSource;

public class SysRoles extends PageManagerController {
	
	@Autowired
	private FilterSecurityInterceptor filterSecurityInterceptor;	

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		
	}
	
	
	/**
	 * Ajax请求：获取用户角色列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getRoles(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//组装JSON
			JSONObject json = rolesService.getTreeRolesOfJSON();

			//输出数据
			out.println(json);
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}	
	
		
	/**
	 * Ajax请求：保存角色
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveRoles(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{			
			//创建对象
			Roles role = request.getBindObject(Roles.class, "role");
			int[] moduleIds = ConvertUtils.toArrInteger(request.getParameter("moduleIds").split(","));
			boolean moduleModified = request.getBooleanParameter("moduleModified");
			
			//添加模块
			role.setModules(new ArrayList<Modules>());			
			for(Integer moduleId : moduleIds){
				Modules modules = new Modules();
				modules.setModuleId(moduleId);
				role.getModules().add(modules);
			}			
			
			if(role.getRoleId()==null){
				//添加对象
				rolesService.addRoles(role, moduleModified);
			}else{
				//编辑对象
				rolesService.editRoles(role, moduleModified);
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
	 * Ajax请求：删除角色
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delRoles(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			Integer roleId = request.getIntParameter("roleId");	

			rolesService.delRoles(roleId);
			
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
	public void upRoles(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String roleId = request.getParameter("roleId");
			String prevRoleId = request.getParameter("prevRoleId");
			
			Roles role = rolesService.getRoles(Integer.valueOf(roleId));
			Roles prevRole = rolesService.getRoles(Integer.valueOf(prevRoleId));
			
			//排序对调
			int temp = role.getSort();
			role.setSort(prevRole.getSort());
			prevRole.setSort(temp);
			
			rolesService.editRoles(role, false);
			rolesService.editRoles(prevRole, false);
			
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
	public void downRoles(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String roleId = request.getParameter("roleId");
			String nextRoleId = request.getParameter("nextRoleId");
			
			Roles role = rolesService.getRoles(Integer.valueOf(roleId));
			Roles nextRole = rolesService.getRoles(Integer.valueOf(nextRoleId));
			
			//排序对调
			int temp = role.getSort();
			role.setSort(nextRole.getSort());
			nextRole.setSort(temp);
			
			rolesService.editRoles(role, false);
			rolesService.editRoles(nextRole, false);
			
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
	public void getUsersByRoles(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String callback=request.getParameter("callback");
		
		try{
			Integer roleId = request.getIntParameter("roleId");
			
			List<Admins> list;			
			if(roleId!=null){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("roleId", roleId);		
				list = adminsService.getAdminsList(params);
			}else{
				list = new ArrayList<Admins>();
			}
			
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

}
