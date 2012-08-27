package com.baishop.service.impl.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;

import com.baishop.entity.ass.Params;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.ParamsService;

public class ParamsServiceImpl extends BaseService implements ParamsService {

	private static final long serialVersionUID = 2378024773454144950L;


	@Override
	public Params getParams(int paramsId) {
		List<Params> list = this.getParamsList(new HashMap<String,Object>());
		for(Params params : list){
			if(params.getParamsId().equals(paramsId))
				return params;
		}
		return null;
	}
	

	@Override
	public Params getParams(String paramsName) {
		List<Params> list = this.getParamsList(new HashMap<String,Object>());
		for(Params params : list){
			if(params.getParamsName().equals(paramsName))
				return params;
		}
		return null;
	}


	@Override
	public List<Params> getParamsList() {
		return this.getParamsList(null);
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
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"系统参数"});
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
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"系统参数"});
		}
	}

	
	@Override
	public void addParams(Params params) {
		try{
			this.getSqlMapClientAss().insert("Params.addParams",params);		

        }catch(DuplicateKeyException e){
			throw new ServiceException(112, e, new String[]{"参数名"});	
        } catch (Exception e) {
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"系统参数"});
		}
	}
	

	@Override
	public void editParams(Params params) {
		try{
			this.getSqlMapClientAss().update("Params.editParams",params);		

        }catch(DuplicateKeyException e){
			throw new ServiceException(112, e, new String[]{"参数名"});	
        } catch (Exception e) {
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"系统参数"});
		}
	}


}
