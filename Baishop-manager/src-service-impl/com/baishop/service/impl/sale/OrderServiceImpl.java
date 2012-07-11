package com.baishop.service.impl.sale;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.baishop.entity.sale.Order;
import com.baishop.entity.sale.OrderGoods;
import com.baishop.entity.sale.Users;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.sale.OrderService;
import com.ibatis.sqlmap.client.SqlMapExecutor;

public class OrderServiceImpl extends BaseService implements OrderService {

	private static final long serialVersionUID = 5680536314275393988L;

	@Override
	public Order getOrder(Users user, long orderId, OrderQueryMode mode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderId", orderId);
			
			// 查询语句
			String queryName = getOrderQueryName(mode);
			
			Order order = (Order)this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForObject(queryName, params);
			
			return order;
		}catch(Exception e){
			throw new ServiceException(200001, e);
		}
	}

	@Override
	public Order getOrder(Users user, String orderSn, OrderQueryMode mode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderSn", orderSn);
			
			// 查询语句
			String queryName = getOrderQueryName(mode);
			
			Order order = (Order)this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForObject(queryName, params);
			
			return order;
		}catch(Exception e){
			throw new ServiceException(200001, e);
		}
	}

	@Override
	public List<Order> getOrderList(Users user, int start, int limit, OrderQueryMode mode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("start", start);
			params.put("limit", limit);
			
			// 查询语句
			String queryName = getOrderQueryName(mode);
			
			@SuppressWarnings("unchecked")
			List<Order> list = this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForList(queryName, params);
			
			return list;
		}catch(Exception e){
			throw new ServiceException(200001, e);
		}
	}

	@Override
	public List<Order> getOrderListByUserId(Users user, long userId, int start, int limit,
			OrderQueryMode mode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", userId);
			params.put("start", start);
			params.put("limit", limit);
			
			// 查询语句
			String queryName = getOrderQueryName(mode);
			
			@SuppressWarnings("unchecked")
			List<Order> list = this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForList(queryName, params);
			
			return list;
		}catch(Exception e){
			throw new ServiceException(200001, e);
		}
	}

	@Override
	public void delOrder(Users user, long orderId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderId", orderId);
			
			this.getSqlMapClientSlave(user.getSlaveDatabase()).delete("Order.delOrder", params);
		}catch(Exception e){
			throw new ServiceException(200002, e);
		}
	}

	@Override
	public void delOrder(Users user, String orderSn) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderSn", orderSn);
			
			this.getSqlMapClientSlave(user.getSlaveDatabase()).delete("Order.delOrder", params);
		}catch(Exception e){
			throw new ServiceException(200002, e);
		}
	}

	@Override
	public void delOrderByUserId(Users user, long userId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", userId);
			
			this.getSqlMapClientSlave(user.getSlaveDatabase()).delete("Order.delOrder", params);
		}catch(Exception e){
			throw new ServiceException(200002, e);
		}
	}

	@Override
	public void delOrderByOrderIdList(Users user, List<Long> orderIdList) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderIdList", orderIdList);
			
			this.getSqlMapClientSlave(user.getSlaveDatabase()).delete("Order.delOrder", params);
		}catch(Exception e){
			throw new ServiceException(200002, e);
		}
	}

	@Override
	public void delRealOrder(Users user, List<Integer> orderIdList) {
		try{
			if(orderIdList==null || orderIdList.size()<=0){
				throw new ServiceException(100, new String[]{"orderIdList"});
			}
			
			SqlMapClientTemplate sqlMapClientTemplate = this.getSqlMapClientSlave(user.getSlaveDatabase());
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderIdList", orderIdList);
			
			sqlMapClientTemplate.delete("Order.delRealOrder", params);
			sqlMapClientTemplate.delete("OrderGoods.delOrderGoods", params);
		}catch(Exception e){
			throw new ServiceException(200002, e);
		}
	}

	@Override
	public void addOrder(Users user, final Order order) {
		try{
			if(order==null){
				throw new ServiceException(100, new String[]{"order"});
			}

			SqlMapClientTemplate sqlMapClientTemplate = this.getSqlMapClientSlave(user.getSlaveDatabase());
			
			// 添加订单记录
			sqlMapClientTemplate.insert("Order.addOrder", order);
			
			//TODO: 订单商品批量插入需要改造
			// 批量添加订单商品
			sqlMapClientTemplate.execute(new SqlMapClientCallback<Integer>(){
				@Override
				public Integer doInSqlMapClient(SqlMapExecutor executor)
						throws SQLException {
					executor.startBatch();
					for(OrderGoods orderGoods : order.getOrderGoods()){
						executor.insert("OrderGoods.addOrderGoods", orderGoods);
					}	
					return executor.executeBatch();
				}
			});
			
		}catch(Exception e){
			throw new ServiceException(200003, e);
		}
	}

	@Override
	public void editOrder(Users user, final Order order) {
		try{
			if(order==null){
				throw new ServiceException(100, new String[]{"order"});
			}

			SqlMapClientTemplate sqlMapClientTemplate = this.getSqlMapClientSlave(user.getSlaveDatabase());
			
			// 更新商品
			sqlMapClientTemplate.update("Order.editOrder", order);
			
			//TODO: 订单商品批量更新需要改造
			// 批量更新订单商品
			sqlMapClientTemplate.execute(new SqlMapClientCallback<Integer>(){
				@Override
				public Integer doInSqlMapClient(SqlMapExecutor executor)
						throws SQLException {
					executor.startBatch();					
					for(OrderGoods orderGoods : order.getOrderGoods()){
						executor.update("OrderGoods.editOrderGoods", orderGoods);
					}					
					return executor.executeBatch();
				}
			});
			
		}catch(Exception e){
			throw new ServiceException(200004, e);
		}
	}

	
	
	
	private String getOrderQueryName(OrderQueryMode mode) {
		String queryName;			
		switch(mode){
		case WITH_GOODS:
			queryName = "Order.getOrderWithGoods";
			break;
		default:
			// SIMPLE:
			queryName = "Order.getOrder";
			break;
		}
		
		return queryName;
	}
	
}
