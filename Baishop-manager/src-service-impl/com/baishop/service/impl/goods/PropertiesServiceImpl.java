package com.baishop.service.impl.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.baishop.entity.goods.Category;
import com.baishop.entity.goods.Properties;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.goods.CategoryService;
import com.baishop.service.goods.PropertiesService;

public class PropertiesServiceImpl extends BaseService implements
		PropertiesService {

	private static final long serialVersionUID = 3839340628092602371L;

	@Autowired
	private CategoryService categoryService;
	

	@Override
	public Properties getProperties(int propsId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("propsId", propsId);
			
			Properties props = (Properties)this.getSqlMapClientShop().queryForObject("Properties.getProperties", params);
			return props;			
		}catch(Exception e){
			throw new ServiceException(101001, e);
		}
	}

	@Override
	public List<Properties> getPropertiesList() {
		try{
			@SuppressWarnings("unchecked")
			List<Properties> list = this.getSqlMapClientShop().queryForList("Properties.getProperties");
			return list;
		}catch(Exception e){
			throw new ServiceException(101001, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Properties> getPropertiesList(int cateId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			
			List<Properties> list = new ArrayList<Properties>();
			Category cate = categoryService.getCategory(cateId);
			
			while(cate!=null){
				params.put("cateId", cate.getCateId());
				list = this.getSqlMapClientShop().queryForList("Properties.getProperties", params);
				if(list.size()>0){
					break;
				}
				cate = categoryService.getCategory(cate.getCateParent());
			}
						
			return list;
			
		}catch(Exception e){
			throw new ServiceException(101001, e);
		}
	}

	@Override
	public void delProperties(int propsId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("propsId", propsId);
			
			this.getSqlMapClientShop().delete("Properties.delProperties", params);
		}catch(Exception e){
			throw new ServiceException(101002, e);
		}
	}

	@Override
	public void delPropertiesByCateId(int cateId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("cateId", cateId);
			
			this.getSqlMapClientShop().delete("Properties.delProperties", params);
		}catch(Exception e){
			throw new ServiceException(101002, e);
		}
	}

	@Override
	public void addProperties(Properties properties) {
		try{
			this.getSqlMapClientShop().insert("Properties.addProperties", properties);
		}catch(Exception e){
			throw new ServiceException(101003, e);
		}
	}

	@Override
	public void editProperties(Properties properties) {
		try{
			this.getSqlMapClientShop().update("Properties.editProperties", properties);
		}catch(Exception e){
			throw new ServiceException(101004, e);
		}
	}
	

}
