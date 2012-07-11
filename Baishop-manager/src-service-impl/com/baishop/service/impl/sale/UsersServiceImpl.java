package com.baishop.service.impl.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.sale.Users;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.sale.UsersService;

public class UsersServiceImpl extends BaseService implements UsersService {

	private static final long serialVersionUID = 3720143127575149415L;

	@Override
	public Users getUsers(long userId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", userId);
			
			Users users = (Users)this.getSqlMapClientAss().queryForObject("Users.getUsers", params);
			return users;			
		}catch(Exception e){
			throw new ServiceException(700001, e);
		}
	}

	@Override
	public Users getUsers(String username) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("username", username);
			
			Users users = (Users)this.getSqlMapClientAss().queryForObject("Users.getUsers", params);
			return users;			
		}catch(Exception e){
			throw new ServiceException(700001, e);
		}
	}

	@Override
	public Users getUsers(String username, String password) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("username", username);
			params.put("password", password);
			
			Users users = (Users)this.getSqlMapClientAss().queryForObject("Users.getUsers", params);
			return users;			
		}catch(Exception e){
			throw new ServiceException(700001, e);
		}
	}

	@Override
	public List<Users> getUsersList(int start, int limit) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("start", start);
			params.put("limit", limit);
			
			@SuppressWarnings("unchecked")
			List<Users> list = this.getSqlMapClientAss().queryForList("Users.getUsers");
			return list;			
		}catch(Exception e){
			throw new ServiceException(700001, e);
		}
	}

	@Override
	public void delUsers(long userId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", userId);
			
			this.getSqlMapClientAss().delete("Users.delUsers", params);
		}catch(Exception e){
			throw new ServiceException(700002, e);
		}
	}

	@Override
	public void delUsers(String username) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("username", username);
			
			this.getSqlMapClientAss().delete("Users.delUsers", params);
		}catch(Exception e){
			throw new ServiceException(700002, e);
		}
	}

	@Override
	public void addUsers(Users user) {
		try{
			this.getSqlMapClientAss().insert("Users.addUsers", user);
		}catch(Exception e){
			throw new ServiceException(700003, e);
		}
	}

	@Override
	public void editUsers(Users user) {
		try{
			this.getSqlMapClientAss().update("Users.editUsers", user);
		}catch(Exception e){
			throw new ServiceException(700004, e);
		}
	}

}
