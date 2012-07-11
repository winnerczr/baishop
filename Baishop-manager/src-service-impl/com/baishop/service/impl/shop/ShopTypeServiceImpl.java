package com.baishop.service.impl.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.shop.ShopType;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.shop.ShopTypeService;

public class ShopTypeServiceImpl extends BaseService implements ShopTypeService {

	private static final long serialVersionUID = -6117835781457094313L;

	@Override
	public ShopType getShopType(int typeId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("typeId", typeId);
			
			ShopType shopType = (ShopType)this.getSqlMapClientShop().queryForObject("ShopType.getShopType", params);
			return shopType;			
		}catch(Exception e){
			throw new ServiceException(104001, e);
		}
	}

	@Override
	public List<ShopType> getShopTypeList() {
		try{
			@SuppressWarnings("unchecked")
			List<ShopType> list = this.getSqlMapClientShop().queryForList("ShopType.getShopType");
			return list;
		}catch(Exception e){
			throw new ServiceException(104001, e);
		}
	}

	@Override
	public List<ShopType> getShopTypeList(int typeParent) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("typeParent", typeParent);
			
			@SuppressWarnings("unchecked")
			List<ShopType> list = this.getSqlMapClientShop().queryForList("ShopType.getShopType", params);
			return list;
		}catch(Exception e){
			throw new ServiceException(104001, e);
		}
	}

	@Override
	public void delShopType(int typeId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("typeId", typeId);
			
			this.getSqlMapClientShop().delete("ShopType.delShopType", params);
		}catch(Exception e){
			throw new ServiceException(104002, e);
		}
	}

	@Override
	public void delShopTypeByParent(int typeParent) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("typeParent", typeParent);
			
			this.getSqlMapClientShop().delete("ShopType.delShopType", params);
		}catch(Exception e){
			throw new ServiceException(104002, e);
		}
	}

	@Override
	public void addShopType(ShopType shopType) {
		try{
			this.getSqlMapClientShop().insert("ShopType.addShopType", shopType);
		}catch(Exception e){
			throw new ServiceException(104003, e);
		}
	}

	@Override
	public void editShopType(ShopType shopType) {
		try{
			this.getSqlMapClientShop().update("ShopType.editShopType", shopType);
		}catch(Exception e){
			throw new ServiceException(104004, e);
		}
	}

}
