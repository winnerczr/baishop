package com.baishop.service.impl.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.sale.OrderOption;
import com.baishop.entity.sale.Users;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.sale.OrderOptionService;

public class OrderOptionServiceImpl extends BaseService implements
		OrderOptionService {

	private static final long serialVersionUID = 7293437055211576836L;

	@Override
	public OrderOption getOrderOption(Users user, long optionId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("optionId", optionId);
			
			OrderOption orderOption = (OrderOption)this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForObject("OrderOption.getOrderOption", params);
			return orderOption;			
		}catch(Exception e){
			throw new ServiceException(201001, e);
		}
	}

	@Override
	public List<OrderOption> getOrderOptionList(Users user, long orderId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderId", orderId);
			
			@SuppressWarnings("unchecked")
			List<OrderOption> list = this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForList("OrderOption.getOrderOption", params);
			return list;			
		}catch(Exception e){
			throw new ServiceException(201001, e);
		}
	}

	@Override
	public void delOrderOption(Users user, long optionId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("optionId", optionId);
			
			this.getSqlMapClientSlave(user.getSlaveDatabase()).delete("OrderOption.delOrderOption", params);
		}catch(Exception e){
			throw new ServiceException(201002, e);
		}
	}

	@Override
	public void delOrderOptionByOrderId(Users user, long orderId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderId", orderId);
			
			this.getSqlMapClientSlave(user.getSlaveDatabase()).delete("OrderOption.delOrderOption", params);
		}catch(Exception e){
			throw new ServiceException(201002, e);
		}
	}

	@Override
	public void addOrderOption(Users user, OrderOption orderOption) {
		try{
			this.getSqlMapClientSlave(user.getSlaveDatabase()).insert("OrderOption.addOrderOption", orderOption);
		}catch(Exception e){
			throw new ServiceException(201003, e);
		}
	}

	@Override
	public void editOrderOption(Users user, OrderOption orderOption) {
		try{
			this.getSqlMapClientSlave(user.getSlaveDatabase()).update("OrderOption.editOrderOption", orderOption);
		}catch(Exception e){
			throw new ServiceException(201004, e);
		}
	}

}
