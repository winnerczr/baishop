package com.baishop.manager.web.ass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.ass.Admins;
import com.baishop.entity.ass.Depts;
import com.baishop.entity.ass.Modules;
import com.baishop.entity.ass.Roles;
import com.baishop.framework.json.JsonConfigGlobal;
import com.baishop.framework.utils.ConvertUtils;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;

public class SysAdmins extends PageManagerController {

	private Md5PasswordEncoder md5 = new Md5PasswordEncoder();

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {

		//部门tree与combobox
		modeview.addObject("depts", this.getTreeDeptOfJSON());	
	}
	
	
	/**
	 * Ajax请求：获取后台用户列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getAdmins(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String callback=request.getParameter("callback");
		
		try{
			//获取参数
			String searchKey = request.getParameter("searchKey", "UTF-8");
			
			//查询参数
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("searchKey", searchKey);
			
			//查询数据
			List<Admins> list = adminsService.getAdminsList(params);
			
			//组装JSON
			JSONObject json = new JSONObject();
			json.put("records", new JSONArray());
			
			JSONArray records = json.getJSONArray("records");
			for(Admins admins : list) {
				JSONObject record = JSONObject.fromObject(admins, JsonConfigGlobal.jsonConfig);

				//部门
				JSONArray deptIds = new JSONArray();
				JSONArray deptNames = new JSONArray();
				for(Depts dept : admins.getDepts()){
					deptIds.add(dept.getDeptId());
					deptNames.add(dept.getDeptName());
				}				
				record.put("deptIds", deptIds);
				record.put("deptNames", deptNames);		
				
				//角色
				JSONArray roleIds = new JSONArray();
				JSONArray roleNames = new JSONArray();
				for(Roles role : admins.getRoles()){
					roleIds.add(role.getRoleId());
					roleNames.add(role.getRoleName());
				}				
				record.put("roleIds", roleIds);
				record.put("roleNames", roleNames);
				

				//角色模块
				JSONArray roleModuleIds = new JSONArray();
				for(Roles role : admins.getRoles()){
					for(Modules module : role.getModules()){
						roleModuleIds.add(module.getModuleId());
					}
				}
				record.put("roleModuleIds", roleModuleIds);	

				//个人模块
				JSONArray userModuleIds = new JSONArray();
				for(Modules module : admins.getModules()){
					userModuleIds.add(module.getModuleId());
				}				
				record.put("userModuleIds", userModuleIds);	
				
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
	 * Ajax请求：保存用户
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveAdmins(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{						
			//创建对象
			Admins user = request.getBindObject(Admins.class, "user");		
			int[] deptIds = ConvertUtils.toArrInteger(request.getParameterValues("user.depts"));
			int[] roleIds = ConvertUtils.toArrInteger(request.getParameter("roleIds").split(","));
			int[] moduleIds = ConvertUtils.toArrInteger(request.getParameter("moduleIds").split(","));
			boolean roleModified = request.getBooleanParameter("roleModified");
			boolean moduleModified = request.getBooleanParameter("moduleModified");
									
			//密码加密
			user.setPassword(StringUtils.isBlank(user.getPassword()) || user.getPassword().equals("********") ? null : md5.encodePassword(user.getPassword(), user.getUsername()));
			
			//添加部门
			user.setDepts(new ArrayList<Depts>());
			for(int deptId : deptIds){
				Depts dept = new Depts();
				dept.setDeptId(deptId);
				user.getDepts().add(dept);
			}
			
			//添加角色
			user.setRoles(new ArrayList<Roles>());			
			for(int roleId : roleIds){
				Roles roles = new Roles();
				roles.setRoleId(roleId);
				user.getRoles().add(roles);
			}
			
			//过滤角色中的模块
			List<Modules> roleModulesList = modulesService.getModulesListByRoleIds(roleIds);			
			for(Modules roleModule : roleModulesList){
				for(int moduleId : moduleIds){
					if(roleModule.getModuleId()==moduleId){
						moduleIds = ArrayUtils.removeElement(moduleIds, moduleId);
						break;
					}
				}
			}
			
			//添加个人模块
			user.setModules(new ArrayList<Modules>());			
			for(Integer moduleId : moduleIds){
				Modules modules = new Modules();
				modules.setModuleId(moduleId);
				user.getModules().add(modules);
			}
			
			
			if(user.getUserId()==null){
				//添加对象
				user.setRegTime(new Date());
				user.setUpdateTime(new Date());
				adminsService.addAdmins(user, true, roleModified, moduleModified);
			}else{
				//编辑对象
				user.setUpdateTime(new Date());
				adminsService.editAdmins(user, true, roleModified, moduleModified);
			}
			
			//输出数据
			out.println("{success: true}");

		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：删除用户
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delAdmins(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String userIds = request.getParameter("userIds");	
			int[] _userIds = ConvertUtils.toArrInteger(userIds.split(","));
			
			adminsService.delAdmins(_userIds);
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}

}
