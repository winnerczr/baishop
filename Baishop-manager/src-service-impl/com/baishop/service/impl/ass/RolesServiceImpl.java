package com.baishop.service.impl.ass;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.transaction.annotation.Transactional;

import com.baishop.entity.ass.Modules;
import com.baishop.entity.ass.Roles;
import com.baishop.entity.ass.RolesModules;
import com.baishop.framework.exception.ServiceException;
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
	
}
