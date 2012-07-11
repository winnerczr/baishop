package com.baishop.service.sale;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.sale.OrderOption;
import com.baishop.entity.sale.Users;

/**
 * 订单操作服务接口
 * @author Linpn
 */
public interface OrderOptionService extends Serializable {

	/**
	 * 获取订单操作
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param optionId 订单操作ID
	 * @return 返回订单操作对象
	 */
	public OrderOption getOrderOption(Users user, long optionId);
	
	/**
	 * 获取订单操作列表
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderId 订单ID
	 * @return 返回订单操作列表
	 */
	public List<OrderOption> getOrderOptionList(Users user, long orderId);
	
	/**
	 * 删除订单操作
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param optionId 订单操作ID
	 */
	public void delOrderOption(Users user, long optionId);
	
	/**
	 * 删除订单操作
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderId 订单ID
	 */
	public void delOrderOptionByOrderId(Users user, long orderId);
		
	/**
	 * 添加订单操作
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderOption 订单操作对象
	 */
	public void addOrderOption(Users user, OrderOption orderOption);	
	
	/**
	 * 修改订单操作
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderOption 订单操作对象
	 */
	public void editOrderOption(Users user, OrderOption orderOption);	

}
