package com.baishop.service.impl.goods;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.baishop.entity.goods.Goods;
import com.baishop.entity.goods.GoodsProps;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.goods.GoodsService;
import com.ibatis.sqlmap.client.SqlMapExecutor;

public class GoodsServiceImpl extends BaseService implements GoodsService {

	private static final long serialVersionUID = -7267739899354961862L;
	
//	@Autowired
//	private ValangValidator goodsValidator;
	

	@Override
	public Goods getGoods(long goodsId, GoodsQueryMode mode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("goodsId", goodsId);
			
			// 查询语句
			String queryName = getGoodsQueryName(mode);
			
			Goods goods = (Goods)this.getSqlMapClientShop().queryForObject(queryName, params);
			
			return goods;
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100001, e);
		}
	}


	@Override
	public Goods getGoods(String goodsSn, GoodsQueryMode mode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("goodsSn", goodsSn);
			
			// 查询语句
			String queryName = getGoodsQueryName(mode);
			
			Goods goods = (Goods)this.getSqlMapClientShop().queryForObject(queryName, params);
			
			return goods;
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100001, e);
		}
	}


	@Override
	public List<Goods> getGoodsList(GoodsQueryMode mode) {
		try{
			List<Goods> list = this.getGoodsList(null, null, null, null, mode);			
			return list;
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100001, e);
		}
	}


	@Override
	public List<Goods> getGoodsListByCateId(int cateId, GoodsQueryMode mode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("cateId", cateId);

			List<Goods> list = this.getGoodsList(params, null, null, null, mode);			
			return list;
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100001, e);
		}
	}


	@Override
	public List<Goods> getGoodsListByBrandId(int brandId, GoodsQueryMode mode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("brandId", brandId);

			List<Goods> list = this.getGoodsList(params, null, null, null, mode);			
			return list;
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100001, e);
		}
	}


	@Override
	public List<Goods> getGoodsListByTypeId(int typeId, GoodsQueryMode mode) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("typeId", typeId);
			
			List<Goods> list = this.getGoodsList(params, null, null, null, mode);			
			return list;
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100001, e);
		}
	}
	
	
	@Override
	public List<Goods> getGoodsList(Map<String, Object> params, Map<String,String> sorters, 
			Long start, Long limit, GoodsQueryMode mode) {
		try{
			if(params==null)
				params = new HashMap<String,Object>();

			if(params.get("isDelete")==null)
				params.put("isDelete", 0);			
			if(!params.get("isDelete").toString().equals("1") && params.get("isOnSale")==null)
				params.put("isOnSale", 1);
			
			params.put("sort", this.getDbSort(sorters));
			params.put("start", start);
			params.put("limit", limit);
			
			// 查询语句
			String queryName = getGoodsQueryName(mode);
			
			@SuppressWarnings("unchecked")
			List<Goods> list = this.getSqlMapClientShop().queryForList(queryName, params);
			return list;
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100001, e);
		}
	}


	@Override
	public long getGoodsCount(Map<String, Object> params) {
		try{
			if(params==null)
				params = new HashMap<String,Object>();
			
			if(params.get("isDelete")==null)
				params.put("isDelete", 0);			
			if(!params.get("isDelete").equals(1) && params.get("isOnSale")==null)
				params.put("isOnSale", 1);
			
			long count = (Long)this.getSqlMapClientShop().queryForObject("Goods.getGoodsCount", params);			
			return count;
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100001, e);
		}
	}
	
	
	@Override
	public void recoveryGoods(long[] goodsIds) {
		try{
			if(goodsIds==null || goodsIds.length<=0)
				throw new ServiceException(100, new String[]{"goodsIds"});
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("goodsIdList", goodsIds);
			
			this.getSqlMapClientShop().delete("Goods.recoveryGoods", params);
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100002, e);
		}
	}

	
	@Override
	public void delGoods(long[] goodsIds) {
		try{
			if(goodsIds==null || goodsIds.length<=0)
				throw new ServiceException(100, new String[]{"goodsIds"});
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("goodsIdList", goodsIds);
			
			this.getSqlMapClientShop().delete("Goods.delGoods", params);
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100002, e);
		}
	}

	
	@Override
	public void delRealGoods(long[] goodsIds) {
		try{
			if(goodsIds==null || goodsIds.length<=0)
				throw new ServiceException(100, new String[]{"goodsIds"});
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("goodsIdList", goodsIds);
			
			this.getSqlMapClientShop().delete("Goods.delRealGoods", params);
			this.getSqlMapClientShop().delete("GoodsImgs.delGoodsImgs", params);
			this.getSqlMapClientShop().delete("GoodsProps.delGoodsProps", params);
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100002, e);
		}
	}

	
	@Override
	public void addGoods(final Goods goods) {
		try{
			if(goods==null)
				throw new ServiceException(100, new String[]{"goods"});
			
			// 添加商品记录
			this.getSqlMapClientShop().insert("Goods.addGoods", goods);
			
			//TODO: 商品属性批量插入需要改造
			// 批量添加商品属性
			this.getSqlMapClientShop().execute(new SqlMapClientCallback<Integer>(){
				@Override
				public Integer doInSqlMapClient(SqlMapExecutor executor)
						throws SQLException {

					if(goods.getProperties()==null || goods.getProperties().size()<=0)
						return 0;
					
					executor.startBatch();					
					for(GoodsProps goodsProps : goods.getProperties()){
						executor.insert("GoodsProps.addGoodsProps", goodsProps);
					}					
					return executor.executeBatch();
				}
			});
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100003, e);
		}
	}

	
	@Override
	public void editGoods(final Goods goods) {
		try{
			if(goods==null)
				throw new ServiceException(100, new String[]{"goods"});
			
			// 更新商品
			this.getSqlMapClientShop().update("Goods.editGoods", goods);
			
			//TODO: 商品属性批量更新需要改造
			// 批量更新商品属性
			this.getSqlMapClientShop().execute(new SqlMapClientCallback<Integer>(){
				@Override
				public Integer doInSqlMapClient(SqlMapExecutor executor)
						throws SQLException {
					
					if(goods.getProperties()==null || goods.getProperties().size()<=0)
						return 0;
					
					executor.startBatch();
					System.out.println(goods.getProperties());
					for(GoodsProps goodsProps : goods.getProperties()){
						executor.update("GoodsProps.editGoodsProps", goodsProps);
					}					
					return executor.executeBatch();
				}
			});
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100004, e);
		}
	}
	

	@Override
	public void upShelfGoods(final long[] goodsIds) {
		try{
			this.getSqlMapClientShop().execute(new SqlMapClientCallback<Integer>(){
				@Override
				public Integer doInSqlMapClient(SqlMapExecutor executor)
						throws SQLException {
					executor.startBatch();	
					
					for(long goodsId : goodsIds){
						Goods goods = new Goods();
						goods.setGoodsId(goodsId);
						goods.setIsOnSale((byte)1);
						
						editGoods(goods);
					}
					
					return executor.executeBatch();
				}
			});
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100005, e);
		}
	}
	

	@Override
	public void offShelfGoods(final long[] goodsIds) {
		try{
			this.getSqlMapClientShop().execute(new SqlMapClientCallback<Integer>(){
				@Override
				public Integer doInSqlMapClient(SqlMapExecutor executor)
						throws SQLException {
					executor.startBatch();	
					
					for(long goodsId : goodsIds){
						Goods goods = new Goods();
						goods.setGoodsId(goodsId);
						goods.setIsOnSale((byte)0);
						
						editGoods(goods);
					}
					
					return executor.executeBatch();
				}
			});
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100006, e);
		}
	}


	@Override
	public void modifyPrice(long goodsId, BigDecimal shopPrice) {
		try{
			if(shopPrice.doubleValue()>99999999.99)
				throw new ServiceException(100009);
			
			Goods goods = new Goods();
			goods.setGoodsId(goodsId);
			goods.setShopPrice(shopPrice);
			
			editGoods(goods);
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100007, e);
		}
	}


	@Override
	public void modifyInventory(long goodsId, int goodsNumber) {
		try{
			if(goodsNumber>999999999)
				throw new ServiceException(100010);
			
			Goods goods = new Goods();
			goods.setGoodsId(goodsId);
			goods.setGoodsNumber(goodsNumber);
			
			editGoods(goods);
			
		}catch(Exception e){
			if(e instanceof ServiceException)
				throw (ServiceException)e;
			throw new ServiceException(100008, e);
		}
	}

	


	/**
	 * 获取查询的sqlmap名称
	 * @param mode
	 * @return
	 */
	private String getGoodsQueryName(GoodsQueryMode mode) {
		String queryName;			
		switch(mode){
		case WITH_IMGS:
			queryName = "Goods.getGoodsWithImgs";
			break;
		case WITH_PROPS:
			queryName = "Goods.getGoodsWithProps";
			break;
		case WITH_BLOB:
			queryName = "Goods.getGoodsWithBLOB";
			break;
		case ALL:
			queryName = "Goods.getGoodsAll";
			break;
		default:
			// SIMPLE:
			queryName = "Goods.getGoods";
			break;
		}
		
		return queryName;
	}

	
}
