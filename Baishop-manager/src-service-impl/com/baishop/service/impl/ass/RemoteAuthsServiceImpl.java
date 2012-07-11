package com.baishop.service.impl.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.baishop.entity.ass.RemoteAuths;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.RemoteAuthsService;

public class RemoteAuthsServiceImpl extends BaseService implements RemoteAuthsService {

	@Override
	public RemoteAuths getRemoteAuths(int id) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("id", id);
			
			RemoteAuths remoteUsers = (RemoteAuths)this.getSqlMapClientAss().queryForObject("RemoteAuths.getRemoteAuths", params);
			return remoteUsers;			
		}catch(Exception e){
			throw new ServiceException(903001, e);
		}
	}
	
	@Override
	public RemoteAuths getRemoteAuths(String user) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("user", user);
			
			RemoteAuths remoteUsers = (RemoteAuths)this.getSqlMapClientAss().queryForObject("RemoteAuths.getRemoteAuths", params);
			return remoteUsers;			
		}catch(Exception e){
			throw new ServiceException(903001, e);
		}
	}

	@Override
	public List<RemoteAuths> getRemoteAuthsList(Map<String,Object> params) {
		try{
			@SuppressWarnings("unchecked")
			List<RemoteAuths> list = this.getSqlMapClientAss().queryForList("RemoteAuths.getRemoteAuths", params);
			return list;			
		}catch(Exception e){
			throw new ServiceException(903001, e);
		}
	}

	@Override
	public void delRemoteAuths(int id) {
		this.delRemoteAuths(new int[]{id});
	}

	@Override
	public void delRemoteAuths(int[] ids) {
		try{
			this.getSqlMapClientAss().delete("RemoteAuths.delRemoteAuths", ids);
		}catch(Exception e){
			throw new ServiceException(903002, e);
		}
	}

	@Override
	public void addRemoteAuths(final RemoteAuths users) {
		try{
			this.getSqlMapClientAss().insert("RemoteAuths.addRemoteAuths", users);			
		}catch(Exception e){
			throw new ServiceException(903003, e);
		}
	}

	@Override
	public void editRemoteAuths(final RemoteAuths users) {
		try{
			this.getSqlMapClientAss().update("RemoteAuths.editRemoteAuths", users);			
		}catch(Exception e){
			throw new ServiceException(903004, e);
		}
	}

	@Override
	public JSONObject getAuthsConf() {
		List<RemoteAuths> list = this.getRemoteAuthsList(null);
		
		JSONObject json = new JSONObject();
		
		for(RemoteAuths user : list){
			JSONObject item = new JSONObject();
			item.put("password", user.getPassword());
			item.put("provide", user.getProvide());
			item.put("auths", user.getAuths());
			
			json.put(user.getUser(), item);
		}		
		
		return json;
	}

}
