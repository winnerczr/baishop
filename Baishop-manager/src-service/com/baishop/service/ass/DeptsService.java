package com.baishop.service.ass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.baishop.entity.ass.Depts;

/**
 * 组织架构服务接口
 * @author Linpn
 */
public interface DeptsService extends Serializable {

	/**
	 * 获取部门
	 * @param deptId 部门ID
	 * @return 返回部门对象
	 */
	public Depts getDepts(int deptId);

	/**
	 * 获取部门
	 * @param deptCode 部门编码
	 * @return 返回部门对象
	 */
	public Depts getDepts(String deptCode);
	
	/**
	 * 获取部门列表
	 * @param params 查询参数
	 * @return 返回部门列表
	 */
	public List<Depts> getDeptsList(Map<String,Object> params);
	
	/**
	 * 通过父节点获取部门列表
	 * @param deptPid 爷节点ID
	 * @return 返回部门列表
	 */
	public List<Depts> getDeptsListByPid(int deptPid);
	
	/**
	 * 删除部门,及该部门下的子节点
	 * @param deptId 部门ID
	 */
	public void delDepts(int deptId);
	

	/**
	 * 删除部门，只删除deptIds所列的部门，不包括子部门
	 * @param deptIds 部门ID列表
	 */
	public void delDepts(int[] deptIds);
	
	/**
	 * 添加部门
	 * @param dept 部门对象
	 */
	public void addDepts(Depts dept);	
	
	/**
	 * 修改部门
	 * @param dept 部门对象
	 */
	public void editDepts(Depts dept);	
	
	/**
	 * 获取JSON格式的树型部门,EXTJS中使用
	 * @return 返回JSON对象，json.get("cbbDept")可以获取combobox所需要的格式
	 */
	public JSONObject getTreeDeptOfJSON();
	
}
