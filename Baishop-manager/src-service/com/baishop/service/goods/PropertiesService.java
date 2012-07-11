package com.baishop.service.goods;

import java.io.Serializable;
import java.util.List;

import com.baishop.entity.goods.Properties;

/**
 * 商品属性服务接口
 * @author Linpn
 */
public interface PropertiesService extends Serializable {

	/**
	 * 获取商品属性
	 * @param propsId 商品属性ID
	 * @return 返回商品属性对象
	 */
	public Properties getProperties(int propsId); 
	
	/**
	 * 获取商品属性列表
	 * @return 返回商品属性列表
	 */
	public List<Properties> getPropertiesList();
	
	/**
	 * 获取商品属性列表
	 * @param cateId 商品类别
	 * @return 返回商品属性列表
	 */
	public List<Properties> getPropertiesList(int cateId);
	
	/**
	 * 删除商品属性
	 * @param propsId 商品属性ID
	 */
	public void delProperties(int propsId);
	
	/**
	 * 删除商品属性
	 * @param cateId 商品类别
	 */
	public void delPropertiesByCateId(int cateId);
	
	/**
	 * 添加商品属性
	 * @param properties 商品属性对象
	 */
	public void addProperties(Properties properties);	
	
	/**
	 * 修改商品属性
	 * @param properties 商品属性对象
	 */
	public void editProperties(Properties properties);	
		
}
