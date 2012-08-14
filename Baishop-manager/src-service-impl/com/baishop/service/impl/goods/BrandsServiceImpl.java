package com.baishop.service.impl.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.goods.Brands;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.goods.BrandsService;

public class BrandsServiceImpl extends BaseService implements BrandsService {

	private static final long serialVersionUID = -4826601805555629231L;

	@Override
	public Brands getBrands(int brandsId) {
		List<Brands> list = this.getBrandsList();
		for(Brands brands : list){
			if(brands.getBrandId().equals(brandsId))
				return brands;
		}
		return null;
	}

	@Override
	public List<Brands> getBrandsList() {
		try{
			@SuppressWarnings("unchecked")
			List<Brands> list = this.getSqlMapClientShop().queryForList("Brands.getBrands");
			return list;
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"品牌"});
		}
	}

	@Override
	public List<Brands> getBrandsList(boolean isShow) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("isShow", isShow);
			
			@SuppressWarnings("unchecked")
			List<Brands> list = this.getSqlMapClientShop().queryForList("Brands.getBrands", params);
			return list;
		}catch(Exception e){
			throw new ServiceException(103001, e);
		}
	}

	@Override
	public void delBrands(int brandsId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("brandsId", brandsId);
			
			this.getSqlMapClientShop().delete("Brands.delBrands", params);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"品牌"});
		}
	}

	@Override
	public void addBrands(Brands brands) {
		try{
			this.getSqlMapClientShop().insert("Brands.addBrands", brands);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"品牌"});
		}
	}

	@Override
	public void editBrands(Brands brands) {
		try{
			this.getSqlMapClientShop().update("Brands.editBrands", brands);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"品牌"});
		}
	}
	

}
