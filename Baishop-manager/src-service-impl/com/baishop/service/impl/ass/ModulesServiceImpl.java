package com.baishop.service.impl.ass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baishop.entity.ass.Admins;
import com.baishop.entity.ass.Modules;
import com.baishop.entity.ass.Roles;
import com.baishop.framework.exception.ServiceException;
import com.baishop.framework.utils.TreeRecursiveHandle;
import com.baishop.service.BaseService;
import com.baishop.service.ass.AdminsService;
import com.baishop.service.ass.ModulesService;

/**
 * 功能模块服务类
 * @author Linpn
 */
public class ModulesServiceImpl extends BaseService implements ModulesService {

	private static final long serialVersionUID = 2218426418456488310L;
	
	@Autowired
	private AdminsService adminsService;
	

	/**
	 * 获取功能模块
	 * @param moduleId 功能模块ID
	 * @return 返回功能模块对象
	 */
	@Override
	public Modules getModules(int moduleId) {
		List<Modules> list = this.getModulesList();
		for(Modules module : list){
			if(module.getModuleId().equals(moduleId))
				return module;
		}
		return null;
	}
	
	@Override
	public Modules getModules(String text) {
		List<Modules> list = this.getModulesList();
		for(Modules module : list){
			if(module.getText().equals(text))
				return module;
		}
		return null;
	}
	
	@Override
	public Modules getModulesByUrl(String url) {
		List<Modules> list = this.getModulesList();
		for(Modules module : list){
			if(module.getUrl().equals(url))
				return module;
		}
		return null;
	}


	@Override
	public List<Modules> getModulesList() {
		return this.getModulesList(null);
	} 
	
	@Override
	public List<Modules> getModulesList(Map<String,Object> params) {
		try{
			@SuppressWarnings("unchecked")
			List<Modules> list = this.getSqlMapClientAss().queryForList("Modules.getModules", params);
			return list;			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"模块"});
		}
	}
	
	@Override
	public List<Modules> getModulesListByRoleIds(int[] roleIds) {
		if(roleIds==null || roleIds.length<=0)
			return new ArrayList<Modules>();
		
		// 模块列表
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleIds", roleIds);			
		List<Modules> list = this.getModulesList(params);
		
		//去除重复
		list = removeRepeatModule(list);
		
		return list;
	}	
	
	@Override
	public List<Modules> getModulesListByUser(Admins user, boolean requery) {
		// 模块列表
		List<Modules> list = new ArrayList<Modules>();
		
		// 重新查询
		if(requery){
			user = adminsService.getAdmins(user.getUserId());
		}
		
		// 获取用户中的模块列表
		list.addAll(user.getModules());
		
		// 获取用色中的模块列表
		List<Roles> roles = user.getRoles();
		for(Roles role : roles){
			list.addAll(role.getModules());
		}			

		//去除重复
		list = removeRepeatModule(list);
		
		return list;
	}	
	
	
	@Override
	@Transactional
	public void delModules(int moduleId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("modulePid", moduleId);
		
		List<Modules>	list = this.getModulesList(params);
		if(list.size()>0){
			//请先删除子节点
			throw new ServiceException(110);
		}
		
		this.delModules(new int[]{moduleId});
	}
	
	@Override
	@Transactional
	public void delModules(int[] moduleIds) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("moduleIds", moduleIds);
			
			this.getSqlMapClientAss().delete("Modules.delModules", params);
			this.getSqlMapClientAss().delete("RolesModules.delRolesModules", params);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"模块"});
		}
	}

	
	@Override
	@Transactional
	public void addModules(Modules modules) {
		try{
			int moduleId = (Integer)this.getSqlMapClientAss().insert("Modules.addModules", modules);
			
			//更新排序
			modules.setModuleId(moduleId);
			modules.setSort(Integer.valueOf(modules.getModulePid().toString() + moduleId));
			this.getSqlMapClientAss().update("Modules.editModules", modules);
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"模块"});
		}
	}

	@Override
	@Transactional
	public void editModules(Modules modules) {
		try{
			this.getSqlMapClientAss().update("Modules.editModules", modules);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"模块"});
		}
	}
	
	
	

	@Override
	public JSONArray getListSystemsOfJSON(){		
		JSONArray json = new JSONArray();		
		List<Modules> list = this.getModulesList();
		
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

	@Override
	public JSONObject getLeafModulesOfJSON(){		
		JSONObject json = new JSONObject();		
		List<Modules> list = this.getModulesList();
		
		for(Modules module : list){		
			//添加最叶子节点到列表中
			if(ModulesService.SYSTEM.equals(module.getType()) || ModulesService.MODULE.equals(module.getType()) || ModulesService.FUNCTION.equals(module.getType())){
				json.put(module.getText(), module.getUrl());
			}
		}
		
		return json;
	}

	@Override
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
				list = this.getModulesList();
			else
				list = this.getModulesListByUser(user, false);
			
						
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
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"模块"});
		}
		
		return json;
	}
	
	
	

	
	/**
	 * 去除重复模块
	 * @param list 模块列表
	 * @return
	 */
	private List<Modules> removeRepeatModule(List<Modules> list) {
		// 去除重复
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getModuleId().equals(list.get(i).getModuleId()))
					list.remove(j);
			}
		}
		
		// 排序
		Collections.sort(list, new Comparator<Modules>() {
			@Override
			public int compare(Modules o1, Modules o2) {
				return o1.getSort().compareTo(o2.getSort());
			}
		});		

		return list;
	}

}
