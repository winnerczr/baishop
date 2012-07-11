package com.baishop.service.sale;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.sale.Users;
import com.baishop.entity.sale.UsersCredits;

/**
 * 信用等级服务接口
 * @author Linpn
 */
public interface UsersCreditsService extends Serializable {

	/**
	 * 获取信用等级
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param creditsId 信用等级ID
	 * @return 返回信用等级对象
	 */
	public UsersCredits getCredits(Users user, int creditsId);
	
	/**
	 * 获取信用等级列表
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @return 返回信用等级列表
	 */
	public List<UsersCredits> getCreditsList(Users user);
	
	/**
	 * 删除信用等级
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param creditsId 信用等级ID
	 */
	public void delCredits(Users user, int creditsId);
		
	/**
	 * 添加信用等级
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param credits 信用等级对象
	 */
	public void addCredits(Users user, UsersCredits credits);	
	
	/**
	 * 修改信用等级
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param credits 信用等级对象
	 */
	public void editCredits(Users user, UsersCredits credits);	

}
