package com.baishop.service.impl.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.baishop.entity.ass.Depts;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.DeptsService;

public class DeptsServiceImpl extends BaseService implements DeptsService {

	private static final long serialVersionUID = -7713295501664179256L;

	@Override
	public Depts getDepts(int deptId) {
		List<Depts> list = this.getDeptsList(null);
		for(Depts dept : list){
			if(dept.getDeptId().equals(deptId))
				return dept;
		}
		return null;
	}

	@Override
	public Depts getDepts(String deptCode) {
		List<Depts> list = this.getDeptsList(null);
		for(Depts dept : list){
			if(dept.getDeptCode().equals(deptCode))
				return dept;
		}
		return null;
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
		}catch(Exception e){
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
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"部门"});
		}
	}

}
