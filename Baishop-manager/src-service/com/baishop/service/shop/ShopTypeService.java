package com.baishop.service.shop;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.shop.ShopType;

/**
 * 店铺分类服务接口
 * @author Linpn
 */
public interface ShopTypeService extends Serializable {
	
	/**
	 * 获取店铺分类
	 * @param typeId 店铺分类ID
	 * @return 返回店铺分类对象
	 */
	public ShopType getShopType(int typeId);
	
	/**
	 * 获取店铺分类列表
	 * @return 返回店铺分类列表
	 */
	public List<ShopType> getShopTypeList();
	
	/**
	 * 获取店铺分类列表
	 * @param typeParent 父类别ID
	 * @return 返回店铺分类列表
	 */
	public List<ShopType> getShopTypeList(int typeParent);
	
	/**
	 * 删除店铺分类
	 * @param typeId 店铺分类ID
	 */
	public void delShopType(int typeId);	
	
	/**
	 * 删除店铺分类
	 * @param typeId 店铺分类ID
	 */
	public void delShopTypeByParent(int typeParent);	
	
	/**
	 * 添加店铺分类
	 * @param shopType 店铺分类对象
	 */
	public void addShopType(ShopType shopType);	
	
	/**
	 * 修改店铺分类
	 * @param shopType 店铺分类对象
	 */
	public void editShopType(ShopType shopType);
	
}
