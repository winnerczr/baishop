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
		List<Enums> list = this.getEnumsList(new HashMap<String,Object>());
		for(Enums enums : list){
			if(enums.getEnumsId().equals(enumsId))
				return enums;
		}
		return null;
	}

	@Override
	public Enums getEnums(String enumsType, String enumsCode) {
		List<Enums> list = this.getEnumsList(new HashMap<String,Object>());
		for(Enums enums : list){
			if(enums.getEnumsType().equals(enumsType) && enums.getEnumsCode().equals(enumsCode))
				return enums;
		}
		return null;
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
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"系统枚举"});
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
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"系统枚举"});
		}
	}

	@Override
	public void addEnums(Enums enums) {
		try{
			this.getSqlMapClientAss().insert("Enums.addEnums", enums);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"系统枚举"});
		}
	}

	@Override
	public void editEnums(Enums enums) {
		try{
			this.getSqlMapClientAss().update("Enums.editEnums", enums);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"系统枚举"});
		}
	}

}
