package com.baishop.service.impl.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baishop.entity.goods.Category;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.goods.CategoryService;

public class CategoryServiceImpl extends BaseService implements CategoryService {

	private static final long serialVersionUID = 656076932275873218L;

	@Override
	public Category getCategory(int cateId) {
		List<Category> list = this.getCategoryList();
		for(Category cate : list){
			if(cate.getCateId().equals(cateId))
				return cate;
		}
		return null;
	}

	@Override
	public List<Category> getCategoryList() {
		try{
			@SuppressWarnings("unchecked")
			List<Category> list = this.getSqlMapClientShop().queryForList("Category.getCategory");
			return list;
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(101, e, new String[]{"商品类目"});
		}
	}


	@Override
	public void delCategory(int cateId) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("cateId", cateId);
			
			this.getSqlMapClientShop().delete("Category.delCategory", params);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(102, e, new String[]{"商品类目"});
		}
	}


	@Override
	public void addCategory(Category category) {
		try{
			this.getSqlMapClientShop().insert("Category.addCategory", category);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(103, e, new String[]{"商品类目"});
		}
	}

	@Override
	public void editCategory(Category category) {
		try{
			this.getSqlMapClientShop().update("Category.editCategory", category);
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(104, e, new String[]{"商品类目"});
		}
	}
	
}
