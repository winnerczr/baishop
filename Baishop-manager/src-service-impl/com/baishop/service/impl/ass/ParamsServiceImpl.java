package com.baishop.service.impl.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.ass.Params;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.ParamsService;

public class ParamsServiceImpl extends BaseService implements ParamsService {

	private static final long serialVersionUID = 2378024773454144950L;


	@Override
	public Params getParams(int paramsId) {
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("paramsId", paramsId);
			
			Params params = (Params) this.getSqlMapClientAss().queryForObject("Params.getParams", map);		
			return params;
		}catch(Exception e){
			throw new ServiceException(899001, e);
		}
	}
	

	@Override
	public Params getParams(String paramsName) {
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("paramsName", paramsName);
			
			Params params = (Params) this.getSqlMapClientAss().queryForObject("Params.getParams", map);		
			return params;
		}catch(Exception e){
			throw new ServiceException(899001, e);
		}
	}


	@Override
	public List<Params> getParamsList(Map<String, Object> map) {
		try{
			if(map==null)
				map = new HashMap<String,Object>();
			
			@SuppressWarnings("unchecked")
			List<Params> list = this.getSqlMapClientAss().queryForList("Params.getParams", map);		
			return list;
		}catch(Exception e){
			throw new ServiceException(899001, e);
		}
	}
	

	@Override
	public void delParams(int paramsId) {
		this.delParams(new int[]{paramsId});
	}


	@Override
	public void delParams(int[] paramsIds) {
		try{
			this.getSqlMapClientAss().delete("Params.delParams",paramsIds);		
		}catch(Exception e){
			throw new ServiceException(899002, e);
		}
	}

	
	@Override
	public void addParams(Params params) {
		try{
			this.getSqlMapClientAss().insert("Params.addParams",params);		
		}catch(Exception e){
			throw new ServiceException(899003, e);
		}
	}
	

	@Override
	public void editParams(Params params) {
		try{
			this.getSqlMapClientAss().update("Params.editParams",params);		
		}catch(Exception e){
			throw new ServiceException(899004, e);
		}
	}

}
