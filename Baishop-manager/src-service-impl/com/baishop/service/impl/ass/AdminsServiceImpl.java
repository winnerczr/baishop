package com.baishop.service.impl.ass;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.baishop.entity.ass.Admins;
import com.baishop.entity.ass.AdminsDepts;
import com.baishop.entity.ass.AdminsModules;
import com.baishop.entity.ass.AdminsRoles;
import com.baishop.entity.ass.Depts;
import com.baishop.entity.ass.Modules;
import com.baishop.entity.ass.Roles;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.AdminsService;
import com.baishop.service.ass.DeptsService;
import com.ibatis.sqlmap.client.SqlMapExecutor;


/**
 * 后台用户服务类
 * @author Linpn
 */
public class AdminsServiceImpl extends BaseService implements AdminsService, UserDetailsService {

	private static final long serialVersionUID = -5609524598183928386L;

	private Md5PasswordEncoder md5 = new Md5PasswordEncoder();
	
	@Autowired
	private DeptsService deptsService;
	

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
    	// 获取用户
    	Admins user = this.getAdmins(username);
//    	if(user!=null){
//	    	user.setLastLoginIp(lastLoginIp);
//	    	user.setLastLoginTime(new Date());
//	    	user.setVisitCount(user.getVisitCount()+1);
//	    	this.editAdmins(user, false);
//    	}
    	
    	return  user;
	}

	@Override
	public Admins getAdmins(int userId) {
		List<Admins> list = this.getAdminsList(null);
		for(Admins user : list){
			if(user.getUserId().equals(userId))
				return user;
		}
		return null;
	}

	@Override
	public Admins getAdmins(String username) {
		List<Admins> list = this.getAdminsList(null);
		for(Admins user : list){
			if(username.equals(user.getUsername()))
				return user;
		}
		return null;
	}

	@Override
	public Admins getAdmins(String username, String password) {
		List<Admins> list = this.getAdminsList(null);
		for(Admins user : list){
			if(user.getUsername().equals(username) && user.getPassword().equals(md5.encodePassword(password, username)))
				return user;
		}
		return null;
	}

	@Override
	public List<Admins> getAdminsList(Map<String, Object> params) {
		try{
			if(params==null)
				params = new HashMap<String,Object>();
			
			@SuppressWarnings("unchecked")
			List<Admins> list = this.getSqlMapClientAss().queryForList("Admins.getAdmins", params);
			return list;
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"用户"});
		}
	}
	
	@Override
	public List<Admins> getAdminsListByRoleId(int roleId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleId", roleId);
		
		List<Admins> list = this.getAdminsList(params);
		return list;
	}
	
	@Override
	public List<Admins> getAdminsListByDeptId(int deptId) {
		Depts dept = deptsService.getDepts(deptId);
		List<Admins> list = this.getAdminsListByDeptCode(dept.getDeptCode());
		return list;
	}
	
	@Override
	public List<Admins> getAdminsListByDeptCode(String deptCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("deptCode", deptCode);
		
		List<Admins> list = this.getAdminsList(params);
		return list;
	}

	@Override
	@Transactional
	public void delAdmins(int userId) {
		this.delAdmins(new int[]{userId});
	}	

	@Override
	@Transactional
	public void delAdmins(int[] userIds) {
		try{
			if(userIds==null || userIds.length<=0)
				throw new ServiceException(100, new String[]{"userIds"});
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userIds", userIds);
			
			this.getSqlMapClientAss().delete("Admins.delAdmins", params);
			this.getSqlMapClientAss().delete("AdminsRoles.delAdminsRoles", params);
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"用户"});
		}
	}
	
	@Override
	@Transactional
	public void addAdmins(final Admins users) {
		this.addAdmins(users, false, false, false);		
	}
	
	@Override
	@Transactional
	public void addAdmins(final Admins users, boolean syncDepts, boolean syncRoles, boolean syncModules) {
		try{
			// 添加用户记录
			int userId = (Integer)this.getSqlMapClientAss().insert("Admins.addAdmins", users);			

			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userIds", new int[]{userId});
			
			// 添加部门记录
			if(syncRoles){								
				this.getSqlMapClientAss().delete("AdminsDepts.delAdminsDepts", params);
				this.getSqlMapClientAss().execute(new SqlMapClientCallback<Integer>(){
					@Override
					public Integer doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						
						for(Depts depts : users.getDepts()){
							AdminsDepts adminsDepts = new AdminsDepts();
							adminsDepts.setUserId(users.getUserId());
							adminsDepts.setDeptId(depts.getDeptId());
							executor.insert("AdminsDepts.addAdminsDepts", adminsDepts);
						}
						
						return executor.executeBatch();
					}
				});
			}
			
			// 添加角色记录
			if(syncRoles){
				this.getSqlMapClientAss().delete("AdminsRoles.delAdminsRoles", params);
				this.getSqlMapClientAss().execute(new SqlMapClientCallback<Integer>(){
					@Override
					public Integer doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						
						for(Roles roles : users.getRoles()){
							AdminsRoles adminsRoles = new AdminsRoles();
							adminsRoles.setUserId(users.getUserId());
							adminsRoles.setRoleId(roles.getRoleId());
							executor.insert("AdminsRoles.addAdminsRoles", adminsRoles);
						}
						
						return executor.executeBatch();
					}
				});
			}			

			// 添加模块记录
			if(syncModules){
				this.getSqlMapClientAss().delete("AdminsModules.delAdminsModules", params);
				this.getSqlMapClientAss().execute(new SqlMapClientCallback<Integer>(){
					@Override
					public Integer doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						
						for(Modules modules : users.getModules()){
							AdminsModules adminsModules = new AdminsModules();
							adminsModules.setUserId(users.getUserId());
							adminsModules.setModuleId(modules.getModuleId());
							executor.insert("AdminsModules.addAdminsModules", adminsModules);
						}
						
						return executor.executeBatch();
					}
				});
			}
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"用户"});
		}
	}

	@Override
	@Transactional
	public void editAdmins(final Admins users) {
		this.editAdmins(users, false, false, false);
	}

	@Override
	@Transactional
	public void editAdmins(final Admins users, boolean syncDepts, boolean syncRoles, boolean syncModules) {
		try{
			// 编辑用户记录
			this.getSqlMapClientAss().update("Admins.editAdmins", users);

			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userIds", new int[]{users.getUserId()});
			
			
			// 编辑部门记录
			if(syncDepts){				
				this.getSqlMapClientAss().delete("AdminsDepts.delAdminsDepts", params);
				this.getSqlMapClientAss().execute(new SqlMapClientCallback<Integer>(){
					@Override
					public Integer doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						
						for(Depts depts : users.getDepts()){
							AdminsDepts adminsDepts = new AdminsDepts();
							adminsDepts.setUserId(users.getUserId());
							adminsDepts.setDeptId(depts.getDeptId());
							executor.insert("AdminsDepts.addAdminsDepts", adminsDepts);
						}
						
						return executor.executeBatch();
					}
				});
			}
			

			// 编辑角色记录
			if(syncRoles){
				this.getSqlMapClientAss().delete("AdminsRoles.delAdminsRoles", params);
				this.getSqlMapClientAss().execute(new SqlMapClientCallback<Integer>(){
					@Override
					public Integer doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						
						for(Roles roles : users.getRoles()){
							AdminsRoles adminsRoles = new AdminsRoles();
							adminsRoles.setUserId(users.getUserId());
							adminsRoles.setRoleId(roles.getRoleId());
							executor.insert("AdminsRoles.addAdminsRoles", adminsRoles);
						}
						
						return executor.executeBatch();
					}
				});
			}
			
			// 添加模块记录
			if(syncModules){
				this.getSqlMapClientAss().delete("AdminsModules.delAdminsModules", params);
				this.getSqlMapClientAss().execute(new SqlMapClientCallback<Integer>(){
					@Override
					public Integer doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						
						for(Modules modules : users.getModules()){
							AdminsModules adminsModules = new AdminsModules();
							adminsModules.setUserId(users.getUserId());
							adminsModules.setModuleId(modules.getModuleId());
							executor.insert("AdminsModules.addAdminsModules", adminsModules);
						}
						
						return executor.executeBatch();
					}
				});
			}
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"用户"});
		}
	}
	
}
