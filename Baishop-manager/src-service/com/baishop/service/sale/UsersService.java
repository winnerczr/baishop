package com.baishop.service.sale;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.sale.Users;

/**
 * 用户接口
 * @author Linpn
 */
public interface UsersService extends Serializable {

	/**
	 * 获取用户
	 * @param userId 用户ID
	 * @return 返回用户对象
	 */
	public Users getUsers(long userId);
	
	/**
	 * 获取用户
	 * @param username 用户名
	 * @return 返回用户对象
	 */
	public Users getUsers(String username);
	
	/**
	 * 获取用户
	 * @param username 用户名
	 * @param password 密码
	 * @return 返回用户对象
	 */
	public Users getUsers(String username, String password);
	
	/**
	 * 获取用户列表
	 * @param start 查询的起始记录
	 * @param limit 查询的总记录数, 如果值为-1表示查询到最后
	 * @return
	 */
	public List<Users> getUsersList(int start, int limit);
	
	/**
	 * 删除用户
	 * @param userId 用户ID
	 */
	public void delUsers(long userId);
	
	/**
	 * 删除用户
	 * @param username 用户名
	 */
	public void delUsers(String username);
	
	/**
	 * 添加用户
	 * @param user 用户对象
	 */
	public void addUsers(Users user);	
	
	/**
	 * 修改用户
	 * @param user 用户对象
	 */
	public void editUsers(Users user);

}
