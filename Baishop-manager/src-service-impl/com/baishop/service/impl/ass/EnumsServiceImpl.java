package com.baishop.service.impl.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.ass.Enums;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.EnumsService;

public class EnumsServiceImpl extends BaseService implements EnumsService {

	private static final long serialVersionUID = 6431588222617603394L;

	@Override
	public Enums getEnums(int enumsId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("enumsId", enumsId);
			
			Enums enums = (Enums)this.getSqlMapClientAss().queryForObject("Enums.getEnums", params);
			return enums;			
		}catch(Exception e){
			throw new ServiceException(898001, e);
		}
	}

	@Override
	public Enums getEnums(String enumsType, String enumsCode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("enumsType", enumsType);
			params.put("enumsCode", enumsCode);
			
			Enums enums = (Enums)this.getSqlMapClientAss().queryForObject("Enums.getEnums", params);
			return enums;
		}catch(Exception e){
			throw new ServiceException(898001, e);
		}
	}

	@Override
	public List<Enums> getEnumsList(String enumsType) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("enumsType", enumsType);

		List<Enums> list = this.getEnumsList(params);		
		return list;
	}
	
	@Override
	public List<Enums> getEnumsList(Map<String, Object> params) {
		try{
			if(params==null)
				params = new HashMap<String,Object>();
			
			@SuppressWarnings("unchecked")
			List<Enums> list = this.getSqlMapClientAss().queryForList("Enums.getEnums", params);
			return list;
		}catch(Exception e){
			throw new ServiceException(898001, e);
		}
	}

	@Override
	public void delEnums(int enumsId) {
		this.delEnums(new int[]{enumsId});
	}

	@Override
	public void delEnums(int[] enumsIds) {
		try{
			this.getSqlMapClientAss().delete("Enums.delEnums", enumsIds);
		}catch(Exception e){
			throw new ServiceException(898002, e);
		}
	}

	@Override
	public void addEnums(Enums enums) {
		try{
			this.getSqlMapClientAss().insert("Enums.addEnums", enums);
		}catch(Exception e){
			throw new ServiceException(898003, e);
		}
	}

	@Override
	public void editEnums(Enums enums) {
		try{
			this.getSqlMapClientAss().update("Enums.editEnums", enums);
		}catch(Exception e){
			throw new ServiceException(898004, e);
		}
	}

}
