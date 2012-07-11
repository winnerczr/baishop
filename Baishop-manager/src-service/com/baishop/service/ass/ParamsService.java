package com.baishop.service.ass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baishop.entity.ass.Params;


/**
 * 系统参数服务接口
 * @author Linpn
 */
public interface ParamsService extends Serializable {

	/**
	 * 获取参数
	 * @param paramsId 参数ID
	 * @return 返回参数对象
	 */
	public Params getParams(int paramsId);	
	
	/**
	 * 获取参数
	 * @param paramsName 参数名
	 * @return 返回参数对象
	 */
	public Params getParams(String paramsName);	
	
	/**
	 * 获取参数列表
	 * @param params 查询参数
	 * @param sorters 记录的排序，如sorters.put("id","desc")，该参数如果为空表示按默认排序
	 * @return 返回参数列表
	 */
	public List<Params> getParamsList(Map<String,Object> params);
	
	/**
	 * 删除参数
	 * @param paramsId 参数ID
	 */
	public void delParams(int paramsId);
	
	/**
	 * 删除后台用户
	 * @param paramsId 参数ID列表
	 */
	public void delParams(int[] paramsIds);
	
	/**
	 * 添加参数
	 * @param params 参数对象
	 */
	public void addParams(Params params);	
	
	/**
	 * 修改参数
	 * @param params 参数对象
	 */
	public void editParams(Params params);

}
