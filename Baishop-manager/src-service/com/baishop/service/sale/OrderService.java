package com.baishop.service.sale;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.sale.Order;
import com.baishop.entity.sale.Users;

/**
 * 订单服务接口
 * @author Linpn
 */
public interface OrderService extends Serializable {

	/**
	 * 获取订单
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderId 订单ID
	 * @param mode 订单查询模式
	 * @return
	 */
	public Order getOrder(Users user, long orderId, OrderQueryMode mode);

	/**
	 * 获取订单
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderSn 订单编码
	 * @param mode 订单查询模式
	 * @return 返回订单对象
	 */
	public Order getOrder(Users user, String orderSn, OrderQueryMode mode);
	
	/**
	 * 获取订单列表
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param start 查询的起始记录
	 * @param limit 查询的总记录数, 如果值为-1表示查询到最后
	 * @param mode 订单查询模式
	 * @return 返回订单列表
	 */
	public List<Order> getOrderList(Users user, int start, int limit, OrderQueryMode mode);
	
	/**
	 * 获取订单列表
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param userId 订单类别ID
	 * @param start 查询的起始记录
	 * @param limit 查询的总记录数, 如果值为-1表示查询到最后
	 * @param mode 订单查询模式
	 * @return 返回订单列表
	 */
	public List<Order> getOrderListByUserId(Users user, long userId, int start, int limit, OrderQueryMode mode);
	
	
	
	/**
	 * 删除订单，只做虚拟删除
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderId 订单ID
	 */
	public void delOrder(Users user, long orderId);
	
	/**
	 * 删除订单，只做虚拟删除
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderSn 订单编码
	 */
	public void delOrder(Users user, String orderSn);
	
	/**
	 * 删除订单，只做虚拟删除
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param userId 用户ID
	 */
	public void delOrderByUserId(Users user, long userId);
	
	/**
	 * 删除订单，只做虚拟删除
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderIdList 订单ID列表
	 */
	public void delOrderByOrderIdList(Users user, List<Long> orderIdList);
	
	/**
	 * 真实的删除订单
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param orderIdList 订单ID列表
	 */
	public void delRealOrder(Users user, List<Integer> orderIdList);
	
	
	/**
	 * 添加订单
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param order 订单对象
	 */
	public void addOrder(Users user, Order order);	
	
	/**
	 * 修改订单
	 * @param user 用户对象，用于选择该用户所在的数据库
	 * @param order 订单对象
	 */
	public void editOrder(Users user, Order order);	
	
	
	/**
	 * 订单查询模式
	 * @author Linpn
	 */
	public static enum OrderQueryMode {
		/**
		 * 简单查询，适用于列表查询，该查询不查订单中的商品(orderGoods)
		 */
		SIMPLE,
		/**
		 * 带订单中的商品查询，该查询将查出订单中的商品(orderGoods)
		 */
		WITH_GOODS
	}

}
