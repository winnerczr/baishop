package com.baishop.service.impl.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.baishop.entity.goods.Category;
import com.baishop.framework.exception.ServiceException;
import com.baishop.framework.utils.TreeRecursiveHandle;
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

	@Override
	public JSONObject getTreeCategoryOfJSON() {
		final JSONObject json = new JSONObject();
		
		try {
			json.put("id", 0);
			json.put("text", "商品类目");
			json.put("iconCls", "icon-docs");
			json.put("children", new JSONArray());
			json.put("cbbCategory", new JSONArray());
			
			List<Category> list = this.getCategoryList();
			
			//递归加载
			TreeRecursiveHandle<Category> treeRecursiveHandle = new TreeRecursiveHandle<Category>(){
				public void recursive(List<Category> list, JSONObject treeNode) throws JSONException{
					for(Category cate : list){
						if(cate.getCateParent().equals(treeNode.getInt("id"))){
							JSONObject node = JSONObject.fromObject(cate);
							
							node.put("id", cate.getCateId());
							node.put("text", cate.getCateName());
							node.put("expanded", true);
							node.put("leaf", true);
							
							this.recursive(list, node);
							
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
							String name = cate.getCateName();			
							int pid = cate.getCateParent();
							while(pid>0){
								Category _cate = getCategory(pid);
								pid = _cate.getCateParent();
								name = _cate.getCateName() + ">>" + name;
							}
							json.getJSONArray("cbbCategory").add(JSONArray.fromObject(new Object[]{cate.getCateId(), name}));
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
