package com.baishop.service.impl.ass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baishop.entity.ass.Modules;
import com.baishop.entity.ass.Roles;
import com.baishop.entity.ass.Admins;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.ModulesService;
import com.baishop.service.ass.AdminsService;

/**
 * 功能模块服务类
 * @author Linpn
 */
public class ModulesServiceImpl extends BaseService implements ModulesService {

	private static final long serialVersionUID = 2218426418456488310L;
	
	@Autowired
	private AdminsService adminsService;
	

	/**
	 * 获取功能模块
	 * @param moduleId 功能模块ID
	 * @return 返回功能模块对象
	 */
	@Override
	public Modules getModules(int moduleId) {
		List<Modules> list = this.getModulesList(null);
		for(Modules module : list){
			if(module.getModuleId().equals(moduleId))
				return module;
		}
		return null;
	}
	
	@Override
	public Modules getModules(String text) {
		List<Modules> list = this.getModulesList(null);
		for(Modules module : list){
			if(module.getText().equals(text))
				return module;
		}
		return null;
	}
	
	@Override
	public Modules getModulesByUrl(String url) {
		List<Modules> list = this.getModulesList(null);
		for(Modules module : list){
			if(module.getUrl().equals(url))
				return module;
		}
		return null;
	}

	
	@Override
	public List<Modules> getModulesList(Map<String,Object> params) {
		try{
			@SuppressWarnings("unchecked")
			List<Modules> list = this.getSqlMapClientAss().queryForList("Modules.getModules", params);
			return list;			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"模块"});
		}
	}
	
	@Override
	public List<Modules> getModulesListByRoleIds(int[] roleIds) {
		if(roleIds==null || roleIds.length<=0)
			return new ArrayList<Modules>();
		
		// 模块列表
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleIds", roleIds);			
		List<Modules> list = this.getModulesList(params);
		
		//去除重复
		list = removeRepeatModule(list);
		
		return list;
	}	
	
	@Override
	public List<Modules> getModulesListByUser(Admins user, boolean requery) {
		// 模块列表
		List<Modules> list = new ArrayList<Modules>();
		
		// 重新查询
		if(requery){
			user = adminsService.getAdmins(user.getUserId());
		}
		
		// 获取用户中的模块列表
		list.addAll(user.getModules());
		
		// 获取用色中的模块列表
		List<Roles> roles = user.getRoles();
		for(Roles role : roles){
			list.addAll(role.getModules());
		}			

		//去除重复
		list = removeRepeatModule(list);
		
		return list;
	}	
	
	
	@Override
	@Transactional
	public void delModules(int moduleId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("modulePid", moduleId);
		
		List<Modules>	list = this.getModulesList(params);
		if(list.size()>0){
			//请先删除子节点
			throw new ServiceException(110);
		}
		
		this.delModules(new int[]{moduleId});
	}
	
	@Override
	@Transactional
	public void delModules(int[] moduleIds) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("moduleIds", moduleIds);
			
			this.getSqlMapClientAss().delete("Modules.delModules", params);
			this.getSqlMapClientAss().delete("RolesModules.delRolesModules", params);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"模块"});
		}
	}

	
	@Override
	@Transactional
	public void addModules(Modules modules) {
		try{
			int moduleId = (Integer)this.getSqlMapClientAss().insert("Modules.addModules", modules);
			
			//更新排序
			modules.setModuleId(moduleId);
			modules.setSort(Integer.valueOf(modules.getModulePid().toString() + moduleId));
			this.getSqlMapClientAss().update("Modules.editModules", modules);
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"模块"});
		}
	}

	@Override
	@Transactional
	public void editModules(Modules modules) {
		try{
			this.getSqlMapClientAss().update("Modules.editModules", modules);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"模块"});
		}
	}
	
	
	

	
	/**
	 * 去除重复模块
	 * @param list 模块列表
	 * @return
	 */
	private List<Modules> removeRepeatModule(List<Modules> list) {
		// 去除重复
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getModuleId().equals(list.get(i).getModuleId()))
					list.remove(j);
			}
		}
		
		// 排序
		Collections.sort(list, new Comparator<Modules>() {
			@Override
			public int compare(Modules o1, Modules o2) {
				return o1.getSort().compareTo(o2.getSort());
			}
		});		

		return list;
	} 

}
