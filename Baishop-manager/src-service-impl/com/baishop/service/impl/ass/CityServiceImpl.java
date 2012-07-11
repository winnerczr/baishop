package com.baishop.service.impl.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.ass.City;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.CityService;

public class CityServiceImpl extends BaseService implements CityService {

	private static final long serialVersionUID = -868771914812382473L;

	@Override
	public City getCity(int cityId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("cityId", cityId);
			
			City city = (City)this.getSqlMapClientAss().queryForObject("City.getCity", params);
			return city;			
		}catch(Exception e){
			throw new ServiceException(800001, e);
		}
	}

	@Override
	public City getCity(String cityCode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("cityCode", cityCode);
			
			City city = (City)this.getSqlMapClientAss().queryForObject("City.getCity", params);
			return city;			
		}catch(Exception e){
			throw new ServiceException(800001, e);
		}
	}

	@Override
	public List<City> getCityList(Map<String,Object> params) {
		try{
			@SuppressWarnings("unchecked")
			List<City> list = this.getSqlMapClientAss().queryForList("City.getCity", params);
			return list;			
		}catch(Exception e){
			throw new ServiceException(800001, e);
		}
	}

	@Override
	public List<City> getCityListByPid(int cityPid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("cityPid", cityPid);
		List<City> list = this.getCityList(params);
		return list;
	}


	@Override
	public void delCity(int cityId) {		
		City city = this.getCity(cityId);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("cityParentCode",city.getCityCode());
		
		List<City>	list = this.getCityList(params);		
		int[] cityIds = new int[list.size()+1];
		
		int i;
		for(i=0;i<list.size();i++){
			cityIds[i] = list.get(i).getCityId();
		}
		cityIds[i] = cityId;		
		
		this.delCity(cityIds);
	}
	
	@Override
	public void delCity(int[] cityIds) {
		try{
			this.getSqlMapClientAss().delete("City.delCity", cityIds);
		}catch(Exception e){
			throw new ServiceException(800002, e);
		}
	}


	@Override
	public void addCity(City city) {
		try{
			this.getSqlMapClientAss().insert("City.addCity", city);
		}catch(Exception e){
			throw new ServiceException(800003, e);
		}
	}

	@Override
	public void editCity(City city) {
		try{
			this.getSqlMapClientAss().update("City.editCity", city);
		}catch(Exception e){
			throw new ServiceException(800004, e);
		}
	}

}
