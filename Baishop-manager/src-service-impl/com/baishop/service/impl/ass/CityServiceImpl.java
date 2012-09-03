package com.baishop.service.impl.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;

import com.baishop.entity.ass.City;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.CityService;

public class CityServiceImpl extends BaseService implements CityService {

	private static final long serialVersionUID = -868771914812382473L;

	@Override
	public City getCity(int cityId) {
		List<City> list = this.getCityList();
		for(City city : list){
			if(city.getCityId().equals(cityId))
				return city;
		}
		return null;
	}

	@Override
	public City getCity(String cityCode) {
		List<City> list = this.getCityList();
		for(City city : list){
			if(city.getCityCode().equals(cityCode))
				return city;
		}
		return null;
	}

	@Override
	public List<City> getCityList() {
		return this.getCityList(null);
	}

	@Override
	public List<City> getCityList(Map<String,Object> params) {
		try{
			@SuppressWarnings("unchecked")
			List<City> list = this.getSqlMapClientAss().queryForList("City.getCity", params);
			return list;			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"城市"});
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
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"城市"});
		}
	}


	@Override
	public void addCity(City city) {
		try{
			this.getSqlMapClientAss().insert("City.addCity", city);

        }catch(DuplicateKeyException e){
			throw new ServiceException(112, e, new String[]{"城市编号"});	
        } catch (Exception e) {
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"城市"});
		}
	}

	@Override
	public void editCity(City city) {
		try{
			this.getSqlMapClientAss().update("City.editCity", city);

        }catch(DuplicateKeyException e){
			throw new ServiceException(112, e, new String[]{"城市编号"});	
        } catch (Exception e) {
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"城市"});
		}
	}

}
