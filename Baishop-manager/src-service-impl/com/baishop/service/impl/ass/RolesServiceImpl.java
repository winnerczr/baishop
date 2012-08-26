package com.baishop.service.impl.ass;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.transaction.annotation.Transactional;

import com.baishop.entity.ass.Modules;
import com.baishop.entity.ass.Roles;
import com.baishop.entity.ass.RolesModules;
import com.baishop.framework.exception.ServiceException;
import com.baishop.framework.utils.TreeRecursiveHandle;
import com.baishop.service.BaseService;
import com.baishop.service.ass.RolesService;
import com.ibatis.sqlmap.client.SqlMapExecutor;


/**
 * 后台角色服务类
 * @author Linpn
 */
public class RolesServiceImpl extends BaseService implements RolesService {

	private static final long serialVersionUID = -6044363077079748792L;

	@Override
	public Roles getRoles(int roleId) {
		List<Roles> list = this.getRolesList(null);
		for(Roles role : list){
			if(role.getRoleId().equals(roleId))
				return role;
		}
		return null;
	}

	@Override
	public List<Roles> getRolesList(Map<String,Object> params) {
		try{
			@SuppressWarnings("unchecked")
			List<Roles> list = this.getSqlMapClientAss().queryForList("Roles.getRoles", params);
			return list;			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"角色"});
		}
	}

	@Override
	public List<Roles> getRolesListByUserId(int userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		
		List<Roles> list = this.getRolesList(params);
		return list;
	}
	

	@Override
	@Transactional
	public void delRoles(int roleId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rolePid", roleId);
		
		List<Roles> list = this.getRolesList(params);
		if(list.size()>0){
			//请先删除子节点
			throw new ServiceException(110);
		}
		
		this.delRoles(new int[]{roleId});
	}
	
	@Override
	@Transactional
	public void delRoles(int[] roleIds) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("roleIds", roleIds);
			
			this.getSqlMapClientAss().delete("Roles.delRoles", params);
			this.getSqlMapClientAss().delete("AdminsRoles.delAdminsRoles", params);
			this.getSqlMapClientAss().delete("RolesModules.delRolesModules", params);
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"角色"});
		}
		
	}

	@Override
	@Transactional
	public void addRoles(final Roles roles, boolean syncModules) {
		try{
			// 添加角色
			int roleId = (Integer)this.getSqlMapClientAss().insert("Roles.addRoles", roles);
			
			//更新排序
			roles.setRoleId(roleId);
			roles.setSort(Integer.valueOf(roles.getRolePid().toString() + roleId));
			this.getSqlMapClientAss().update("Roles.editRoles", roles);
						
			// 添加角色模块记录
			if(syncModules){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("roleIds", new int[]{roles.getRoleId()});
				
				this.getSqlMapClientAss().delete("RolesModules.delRolesModules", params);
				this.getSqlMapClientAss().execute(new SqlMapClientCallback<Integer>(){
					@Override
					public Integer doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						
						for(Modules modules : roles.getModules()){
							RolesModules rolesModules = new RolesModules();
							rolesModules.setRoleId(roles.getRoleId());
							rolesModules.setModuleId(modules.getModuleId());
							executor.insert("RolesModules.addRolesModules", rolesModules);
						}
						
						return executor.executeBatch();
					}
				});
			}
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"角色"});
		}
	}

	@Override
	@Transactional
	public void editRoles(final Roles roles, boolean syncModules) {
		try{
			// 编辑角色
			this.getSqlMapClientAss().update("Roles.editRoles", roles);
			
			// 编辑角色模块记录
			if(syncModules){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("roleIds", new int[]{roles.getRoleId()});
				
				this.getSqlMapClientAss().delete("RolesModules.delRolesModules", params);
				this.getSqlMapClientAss().execute(new SqlMapClientCallback<Integer>(){
					@Override
					public Integer doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						
						for(Modules modules : roles.getModules()){
							RolesModules rolesModules = new RolesModules();
							rolesModules.setRoleId(roles.getRoleId());
							rolesModules.setModuleId(modules.getModuleId());
							executor.insert("RolesModules.addRolesModules", rolesModules);
						}
						
						return executor.executeBatch();
					}
				});
			}
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"角色"});
		}
	}
	
	
	
	@Override
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
			
			List<Roles> list = this.getRolesList(null);			
			
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
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"角色"});
		}
		
		return json;
	}
	
}
