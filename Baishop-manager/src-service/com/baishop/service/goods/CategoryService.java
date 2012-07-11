package com.baishop.service.goods;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.goods.Category;

/**
 * 商品类目服务接口
 * @author Linpn
 */
public interface CategoryService extends Serializable {

	/**
	 * 获取商品类别
	 * @param categoryId 商品类别ID
	 * @return 返回商品类别对象
	 */
	public Category getCategory(int categoryId);
	
	/**
	 * 获取商品类别列表
	 * @return 返回商品类别列表
	 */
	public List<Category> getCategoryList();
	
	
	/**
	 * 删除商品类别
	 * @param categoryId 商品类别ID
	 */
	public void delCategory(int categoryId);	
	
	
	/**
	 * 添加商品类别
	 * @param category 商品类别对象
	 */
	public void addCategory(Category category);	
	
	/**
	 * 修改商品类别
	 * @param category 商品类别对象
	 */
	public void editCategory(Category category);
	
}
