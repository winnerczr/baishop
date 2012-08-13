package com.baishop.service.ass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baishop.entity.ass.Enums;

/**
 * 系统枚举服务接口
 * @author Linpn
 */
public interface EnumsService extends Serializable {

	/**
	 * 获取枚举
	 * @param enumsId 枚举ID
	 * @return 返回枚举对象
	 */
	public Enums getEnums(int enumsId); 
	
	/**
	 * 获取枚举
	 * @param enumsType 枚举类型
	 * @param enumsCode 枚举键
	 * @return 返回枚举对象
	 */
	public Enums getEnums(String enumsType, String enumsCode); 	
	
	/**
	 * 获取枚举列表
	 * @param enumsType 枚举类型
	 * @return 返回枚举列表
	 */
	public List<Enums> getEnumsList(String enumsType);
	
	/**
	 * 获取枚举列表
	 * @param params 查询参数
	 * @param sorters 记录的排序，如sorters.put("id","desc")，该参数如果为空表示按默认排序
	 * @return 返回枚举列表
	 */
	public List<Enums> getEnumsList(Map<String,Object> params);
	
	/**
	 * 删除枚举
	 * @param enumsId 枚举ID
	 */
	public void delEnums(int enumsId);
	
	/**
	 * 删除枚举
	 * @param enumsIds 参数ID列表
	 */
	public void delEnums(int[] enumsIds);
	
	/**
	 * 添加枚举
	 * @param enums 枚举对象
	 */
	public void addEnums(Enums enums);	
	
	/**
	 * 修改枚举
	 * @param enums 枚举对象
	 */
	public void editEnums(Enums enums);	

}
