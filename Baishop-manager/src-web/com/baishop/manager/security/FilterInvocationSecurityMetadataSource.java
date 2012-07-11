package com.baishop.manager.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;

import com.baishop.entity.ass.Modules;
import com.baishop.entity.ass.Roles;
import com.baishop.service.ass.ModulesService;
import com.baishop.service.ass.RolesService;

/**
 * 
 * 此类在初始化时，应该取到所有资源及其对应角色的定义
 * 
 * @author user
 * 
 */
public class FilterInvocationSecurityMetadataSource implements
org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource {

	@Autowired
	private RolesService rolesService;
	@Autowired
	private ModulesService modulesService;
	
	private UrlMatcher urlMatcher = new AntUrlPathMatcher();
	private static Map<String, Collection<ConfigAttribute>> httpMethodMap = new HashMap<String, Collection<ConfigAttribute>>();

	
	/**
	 * 加载安全资源数据
	 */
	public synchronized void loadSecuritySource() {
		if(httpMethodMap.size()==0){			
			//获取全部资源
			List<Modules> modules = this.modulesService.getModulesList(null);
			for (Modules module : modules) {
				if(ModulesService.MODULE.equals(module.getType()) || ModulesService.FUNCTION.equals(module.getType())){		
					if(module.getUrl().trim().equals(""))
						continue;
					
					//获取资源
					Collection<ConfigAttribute> atts = httpMethodMap.get(module.getUrl());
					if(atts==null){
						httpMethodMap.put(module.getUrl(), new ArrayList<ConfigAttribute>());
					}
				}
			}
			
			//获取角色中的资源
			List<Roles> roles = this.rolesService.getRolesList(null);
			for (Roles role : roles) {
				modules = role.getModules();
				for (Modules module : modules) {
					if(ModulesService.MODULE.equals(module.getType()) || ModulesService.FUNCTION.equals(module.getType())){	
						if(module.getUrl().trim().equals(""))
							continue;
						
						//获取资源
						Collection<ConfigAttribute> atts = httpMethodMap.get(module.getUrl());
						if(atts==null){
							httpMethodMap.put(module.getUrl(), new ArrayList<ConfigAttribute>());
							atts = httpMethodMap.get(module.getUrl());
						}
						
						//添加资源角色，并判断资源中是否已经存在角色
						boolean isHad = false;
						for(ConfigAttribute ca : atts){
							if(ca.getAttribute().equals(role.getRoleName())){
								isHad = true;
								break;
							}
						}
						if(!isHad){
							atts.add(new SecurityConfig(role.getRoleName()));
						}						
					}
				}
			}
		}
	}

	/**
	 * 清空安全资源数据
	 */
	public synchronized void clearSecuritySource(){
		if(httpMethodMap.size()>=0){		
			httpMethodMap.clear();	
		}
	}
	

	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		if(httpMethodMap.size()==0)
			this.loadSecuritySource();
		
		String url = ((FilterInvocation) object).getRequestUrl();
		Iterator<String> ite = httpMethodMap.keySet().iterator();
		while (ite.hasNext()) {
			String resURL = ite.next();
			if (urlMatcher.pathMatchesUrl(url, resURL)) {
				Collection<ConfigAttribute> returnCollection = httpMethodMap.get(resURL);
				return returnCollection;
			}
		}
		
		return null;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}
}
