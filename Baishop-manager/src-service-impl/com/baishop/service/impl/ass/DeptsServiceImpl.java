package com.baishop.service.impl.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import com.baishop.entity.ass.Depts;
import com.baishop.framework.exception.ServiceException;
import com.baishop.framework.utils.TreeRecursiveHandle;
import com.baishop.service.BaseService;
import com.baishop.service.ass.DeptsService;

public class DeptsServiceImpl extends BaseService implements DeptsService {

	private static final long serialVersionUID = -7713295501664179256L;

	@Override
	public Depts getDepts(int deptId) {
		List<Depts> list = this.getDeptsList();
		for(Depts dept : list){
			if(dept.getDeptId().equals(deptId))
				return dept;
		}
		return null;
	}

	@Override
	public Depts getDepts(String deptCode) {
		List<Depts> list = this.getDeptsList();
		for(Depts dept : list){
			if(dept.getDeptCode().equals(deptCode))
				return dept;
		}
		return null;
	}

	@Override
	public List<Depts> getDeptsList() {
		return this.getDeptsList(null);
	}	

	@Override
	public List<Depts> getDeptsList(Map<String,Object> params) {
		try{
			@SuppressWarnings("unchecked")
			List<Depts> list = this.getSqlMapClientAss().queryForList("Depts.getDepts", params);
			return list;			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"部门"});
		}
	}

	@Override
	public List<Depts> getDeptsListByPid(int deptPid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("deptPid", deptPid);
		List<Depts> list = this.getDeptsList(params);
		return list;
	}


	@Override
	@Transactional
	public void delDepts(int deptId) {		
		Depts dept = this.getDepts(deptId);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("deptParentCode",dept.getDeptCode());
		
		List<Depts>	list = this.getDeptsList(params);		
		int[] deptIds = new int[list.size()+1];
		
		int i;
		for(i=0;i<list.size();i++){
			deptIds[i] = list.get(i).getDeptId();
		}
		deptIds[i] = deptId;		
		
		this.delDepts(deptIds);
	}
	
	@Override
	@Transactional
	public void delDepts(int[] deptIds) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("deptIds", deptIds);
			
			this.getSqlMapClientAss().delete("Depts.delDepts", deptIds);
			this.getSqlMapClientAss().delete("AdminsDepts.delAdminsDepts", params);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"部门"});
		}
	}


	@Override
	@Transactional
	public void addDepts(Depts dept) {
		try{
			this.getSqlMapClientAss().insert("Depts.addDepts", dept);

        }catch(DuplicateKeyException e){
			throw new ServiceException(112, e, new String[]{"部门编号"});	
        } catch (Exception e) {
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"部门"});
		}
	}

	@Override
	@Transactional
	public void editDepts(Depts dept) {
		try{
			this.getSqlMapClientAss().update("Depts.editDepts", dept);

        }catch(DuplicateKeyException e){
			throw new ServiceException(112, e, new String[]{"部门编号"});	
        } catch (Exception e) {
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"部门"});
		}
	}
	
	
	
	@Override
	public JSONObject getTreeDeptOfJSON() {
		final JSONObject json = new JSONObject();
		
		try {
			json.put("id", 0);
			json.put("text", "组织架构");
			json.put("deptId", 0);
			json.put("deptName", "组织架构");
			json.put("iconCls", "icon-dept");
			json.put("children", new JSONArray());
			json.put("cbbDept", new JSONArray());
			
			List<Depts> list = this.getDeptsList();			
			
			//递归加载
			TreeRecursiveHandle<Depts> treeRecursiveHandle = new TreeRecursiveHandle<Depts>(){
				public void recursive(List<Depts> list, JSONObject treeNode) throws JSONException{
					for(Depts dept : list){
						if(dept.getDeptPid().equals(treeNode.getInt("id"))){					
							
							JSONObject node = JSONObject.fromObject(dept);
							
							node.put("id", dept.getDeptId());
							node.put("text", dept.getDeptName());
							node.put("expanded", true);
							node.put("leaf", true);
							node.put("iconCls", "icon-dept");
							
							//递归
							this.recursive(list, node);
							
							//添加到树中
							JSONArray children;
							try {
								children = treeNode.getJSONArray("children");
							} catch (JSONException e) {
								treeNode.put("children", new JSONArray());
								children = treeNode.getJSONArray("children");
							}							
							children.add(node);
							treeNode.put("leaf", false);						
							
							//添加节点到列表中
							json.getJSONArray("cbbDept").add(JSONArray.fromObject(new Object[]{dept.getDeptId(), dept.getDeptName()}));
						}
					}
				}
			};
			
			treeRecursiveHandle.recursive(list, json);
			
		} catch (Exception e) {
			throw new ServiceException(902001, e);
		}
		
		return json;
	}

}
