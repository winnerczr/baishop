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
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("brandsId", brandsId);
			
			Brands brands = (Brands)this.getSqlMapClientShop().queryForObject("Brands.getBrands", params);
			return brands;			
		}catch(Exception e){
			throw new ServiceException(103001, e);
		}
	}

	@Override
	public List<Brands> getBrandsList() {
		try{
			@SuppressWarnings("unchecked")
			List<Brands> list = this.getSqlMapClientShop().queryForList("Brands.getBrands");
			return list;
		}catch(Exception e){
			throw new ServiceException(103001, e);
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
			throw new ServiceException(103002, e);
		}
	}

	@Override
	public void addBrands(Brands brands) {
		try{
			this.getSqlMapClientShop().insert("Brands.addBrands", brands);
		}catch(Exception e){
			throw new ServiceException(103003, e);
		}
	}

	@Override
	public void editBrands(Brands brands) {
		try{
			this.getSqlMapClientShop().update("Brands.editBrands", brands);
		}catch(Exception e){
			throw new ServiceException(103004, e);
		}
	}
	

}
