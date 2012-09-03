package com.baishop.service.ass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.baishop.entity.ass.Admins;
import com.baishop.entity.ass.Modules;

/**
 * 功能模块服务类
 * @author Linpn
 */
public interface ModulesService extends Serializable {
	
	/** 子系统 */
	public final static String SYSTEM = "SYSTEM";
	/** 模块组 */
	public final static String GROUP = "GROUP";
	/** 应用模块 */
	public final static String MODULE = "MODULE";
	/** 操作功能 */
	public final static String FUNCTION = "FUNCTION";
	/** 操作常量 */
	public final static String CONSTANT = "CONSTANT";

	
	/**
	 * 获取功能模块
	 * @param moduleId 功能模块ID
	 * @return 返回功能模块对象
	 */
	public Modules getModules(int moduleId);	
	
	/**
	 * 获取功能模块
	 * @param text 功能模块名称
	 * @return 返回功能模块对象
	 */
	public Modules getModules(String text);
	
	/**
	 * 获取功能模块
	 * @param text 功能模块名称
	 * @return 返回功能模块对象,如果返回空,表示找不到该模块或存在url重复的模块
	 */
	public Modules getModulesByUrl(String url);

	/**
	 * 获取功能模块列表
	 * @return 返回功能模块列表
	 */
	public List<Modules> getModulesList();	

	/**
	 * 获取功能模块列表
	 * @param params 查询参数
	 * @return 返回功能模块列表
	 */
	public List<Modules> getModulesList(Map<String,Object> params);	

	/**
	 * 获取从角色id列表中获取功能模块列表，并去除重复
	 * @param roleIds 角色id列表
	 * @return 返回功能模块列表
	 */
	public List<Modules> getModulesListByRoleIds(int[] roleIds);
	
	/**
	 * 获取从用户中获取功能模块列表，并去除重复
	 * @param user 后台用户对象
	 * @param requery 是否重新查询， 如果为false，则从user中获取模块列表
	 * @return 返回功能模块列表
	 */
	public List<Modules> getModulesListByUser(Admins user, boolean requery);
	
	/**
	 * 删除功能模块，不包括子模块
	 * @param moduleId 功能模块ID
	 */
	public void delModules(int moduleId);
	
	/**
	 * 删除功能模块，只删除moduleIds所列的模块，不包括子模块
	 * @param moduleIds 功能模块ID列表
	 */
	public void delModules(int[] moduleIds);

	/**
	 * 添加功能模块
	 * @param modules 功能模块对象
	 */
	public void addModules(Modules modules);

	/**
	 * 修改功能模块
	 * @param modules 功能模块对象
	 */
	public void editModules(Modules modules);
	
	

	/**
	 * 获取子系统的map集合
	 * @return 返回JSON对象
	 */
	public JSONArray getListSystemsOfJSON();	
	
	/**
	 * 获取子系统、模块、操作的map集合
	 * @return 返回JSON对象
	 */
	public JSONObject getLeafModulesOfJSON();
	
	/**
	 * 获取JSON格式的树型模块
	 * @param user 后台用户对象,如果user为null，则查出所有的模块
	 * @param subsystem 后台子系统,如果为null，则查出所有子系统的模块
	 * @param types 模块类型,值为"SYSTEM"、"GROUP"、"MODULE"、"FUNCTION"， 如果为null，则查出所有的模块
	 * @return 返回JSON对象
	 */
	public JSONObject getTreeModulesOfJSON(Admins user, final String subsystem, final String[] types);
	
}
