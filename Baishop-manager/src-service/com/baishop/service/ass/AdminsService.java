package com.baishop.service.ass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baishop.entity.ass.Admins;

/**
 * 后台用户服务类
 * @author Linpn
 */
public interface AdminsService extends Serializable {
	/**
	 * 获取后台用户
	 * @param userId 后台用户ID
	 * @return 返回后台用户对象
	 */
	public Admins getAdmins(int userId);

	/**
	 * 获取后台用户
	 * @param username 后台用户名称
	 * @return 返回后台用户对象
	 */
	public Admins getAdmins(String username);

	/**
	 * 获取后台用户
	 * @param username 后台用户名称
	 * @param password 密码
	 * @return 返回后台用户对象
	 */
	public Admins getAdmins(String username, String password);
	
	/**
	 * 获取后台用户列表
	 * @param params 查询参数
	 * @return 返回后台用户列表
	 */
	public List<Admins> getAdminsList(Map<String,Object> params);

	/**
	 * 获取后台角色列表
	 * @param roleId 后台角色ID
	 * @return 返回后台用户列表
	 */
	public List<Admins> getAdminsListByRoleId(int roleId);
	
	/**
	 * 删除后台用户
	 * @param userId 后台用户ID
	 */
	public void delAdmins(int userId);
	
	/**
	 * 删除后台用户
	 * @param userIds 后台用户ID列表
	 */
	public void delAdmins(int[] userIds);

	/**
	 * 添加后台用户
	 * @param users 后台用户对象
	 */
	public void addAdmins(final Admins users);

	/**
	 * 添加后台用户
	 * @param users 后台用户对象
	 * @param syncDepts 是否同步部门
	 * @param syncRoles 是否同步角色
	 * @param syncModules 是否同步模块
	 */
	public void addAdmins(final Admins users, boolean syncDepts, boolean syncRoles, boolean syncModules);

	/**
	 * 修改后台用户
	 * @param users 后台用户对象
	 */
	public void editAdmins(final Admins users);

	/**
	 * 修改后台用户
	 * @param users 后台用户对象
	 * @param syncDepts 是否同步部门
	 * @param syncRoles 是否同步角色
	 * @param syncModules 是否同步模块
	 */
	public void editAdmins(final Admins users, boolean syncDepts, boolean syncRoles, boolean syncModules);
}
