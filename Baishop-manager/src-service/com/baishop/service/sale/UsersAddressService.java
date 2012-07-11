package com.baishop.service.sale;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.sale.Users;
import com.baishop.entity.sale.UsersAddress;

/**
 * 用户地址服务接口
 * @author Linpn
 */
public interface UsersAddressService extends Serializable {
	
	/**
	 * 获取用户地址
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param addressId 用户地址ID
	 * @return 返回用户地址对象
	 */
	public UsersAddress getAddress(Users user, long addressId);
	
	/**
	 * 获取用户地址列表
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param userId 用户ID
	 * @return 返回用户地址列表
	 */
	public List<UsersAddress> getAddressList(Users user, long userId);
	
	/**
	 * 删除用户地址
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param addressId 用户地址ID
	 */
	public void delAddress(Users user, long addressId);
	
	/**
	 * 删除用户地址
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param userId 用户ID
	 */
	public void delAddressByUserId(Users user, long userId);
		
	/**
	 * 添加用户地址
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param address 用户地址对象
	 */
	public void addAddress(Users user, UsersAddress address);	
	
	/**
	 * 修改用户地址
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param address 用户地址对象
	 */
	public void editAddress(Users user, UsersAddress address);	
}
