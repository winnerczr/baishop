package com.baishop.service.ass;

import java.util.List;
import java.util.Map;

import junit.remoting.RemoteAuthsConfigurer;

import com.baishop.entity.ass.RemoteAuths;

/**
 * 远程服务访问用户的权限控制 
 * @author Linpn
 */
public interface RemoteAuthsService extends RemoteAuthsConfigurer {
	
	/**
	 * 获取服务service用户
	 * @param id 服务service用户ID
	 * @return 返回服务service用户对象
	 */
	public RemoteAuths getRemoteAuths(int id);
	
	/**
	 * 获取服务service用户
	 * @param user 服务service用户名称
	 * @return 返回服务service用户对象
	 */
	public RemoteAuths getRemoteAuths(String user);

	/**
	 * 获取服务service用户列表
	 * @param params 查询参数
	 * @return 返回服务service用户列表
	 */
	public List<RemoteAuths> getRemoteAuthsList(Map<String,Object> params);

	/**
	 * 删除服务service用户
	 * @param id 服务service用户id
	 */
	public void delRemoteAuths(int id);

	/**
	 * 删除服务service用户
	 * @param ids 服务service用户id列表
	 */
	public void delRemoteAuths(int[] ids);

	/**
	 * 添加服务service用户
	 * @param users 服务service用户对象
	 */
	public void addRemoteAuths(final RemoteAuths users);

	/**
	 * 修改服务service用户
	 * @param users 服务service用户对象
	 */
	public void editRemoteAuths(final RemoteAuths users);

}
