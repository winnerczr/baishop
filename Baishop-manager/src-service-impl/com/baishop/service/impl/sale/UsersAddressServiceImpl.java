package com.baishop.service.impl.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.sale.Users;
import com.baishop.entity.sale.UsersAddress;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.sale.UsersAddressService;

public class UsersAddressServiceImpl extends BaseService implements UsersAddressService {

	private static final long serialVersionUID = 7076958364231443739L;

	@Override
	public UsersAddress getAddress(Users user, long addressId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("addressId", addressId);
			
			UsersAddress address = (UsersAddress)this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForObject("UsersAddress.getAddress", params);
			return address;			
		}catch(Exception e){
			throw new ServiceException(701001, e);
		}
	}

	@Override
	public List<UsersAddress> getAddressList(Users user, long userId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", userId);
			
			@SuppressWarnings("unchecked")
			List<UsersAddress> list = this.getSqlMapClientSlave(user.getSlaveDatabase()).queryForList("UsersAddress.getAddress", params);
			return list;			
		}catch(Exception e){
			throw new ServiceException(701001, e);
		}
	}

	@Override
	public void delAddress(Users user, long addressId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("addressId", addressId);
			
			this.getSqlMapClientSlave(user.getSlaveDatabase()).delete("UsersAddress.delAddress", params);
		}catch(Exception e){
			throw new ServiceException(701002, e);
		}
	}

	@Override
	public void delAddressByUserId(Users user, long userId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", userId);
			
			this.getSqlMapClientSlave(user.getSlaveDatabase()).delete("UsersAddress.delAddress", params);
		}catch(Exception e){
			throw new ServiceException(701002, e);
		}
	}

	@Override
	public void addAddress(Users user, UsersAddress address) {
		try{
			this.getSqlMapClientSlave(user.getSlaveDatabase()).insert("UsersAddress.addAddress", address);
		}catch(Exception e){
			throw new ServiceException(701003, e);
		}
	}

	@Override
	public void editAddress(Users user, UsersAddress address) {
		try{
			this.getSqlMapClientSlave(user.getSlaveDatabase()).update("UsersAddress.editAddress", address);
		}catch(Exception e){
			throw new ServiceException(701004, e);
		}
	}

}
