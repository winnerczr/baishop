package com.baishop.service.goods;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.goods.Brands;

/**
 * 品牌服务接口
 * @author Linpn
 */
public interface BrandsService extends Serializable {

	/**
	 * 获取品牌
	 * @param brandsId 品牌ID
	 * @return 返回品牌对象
	 */
	public Brands getBrands(int brandsId);

	/**
	 * 获取品牌列表
	 * @return 返回品牌列表
	 */
	public List<Brands> getBrandsList();
	
	/**
	 * 获取品牌列表
	 * @param isShow 是否可见
	 * @return 返回品牌列表
	 */
	public List<Brands> getBrandsList(boolean isShow);
	
	/**
	 * 删除品牌
	 * @param brandsId 品牌ID
	 */
	public void delBrands(int brandsId);	
	
	/**
	 * 添加品牌
	 * @param brands 品牌对象
	 */
	public void addBrands(Brands brands);	
	
	/**
	 * 修改品牌
	 * @param brands 品牌对象
	 */
	public void editBrands(Brands brands);
	
}
