package com.baishop.service.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baishop.entity.goods.Goods;

/**
 * 商品服务接口
 * @author Linpn
 */
public interface GoodsService extends Serializable {

	/**
	 * 获取商品
	 * @param goodsId 商品ID
	 * @param mode 商品查询模式
	 * @return
	 */
	public Goods getGoods(long goodsId, GoodsQueryMode mode);

	/**
	 * 获取商品
	 * @param goodsSn 商品编号
	 * @param mode 商品查询模式
	 * @return 返回商品对象
	 */
	public Goods getGoods(String goodsSn, GoodsQueryMode mode);
	
	/**
	 * 获取商品列表
	 * @param start 查询的起始记录
	 * @param limit 查询的总记录数, 如果值为-1表示查询到最后
	 * @param mode 商品查询模式
	 * @return 返回商品列表
	 */
	public List<Goods> getGoodsList(GoodsQueryMode mode);
	
	/**
	 * 获取商品列表
	 * @param cateId 商品类别ID
	 * @param start 查询的起始记录
	 * @param limit 查询的总记录数, 如果值为-1表示查询到最后
	 * @param mode 商品查询模式
	 * @return 返回商品列表
	 */
	public List<Goods> getGoodsListByCateId(int cateId, GoodsQueryMode mode);
	
	/**
	 * 获取商品列表
	 * @param brandId 商品品牌ID
	 * @param start 查询的起始记录
	 * @param limit 查询的总记录数, 如果值为-1表示查询到最后
	 * @param mode 商品查询模式
	 * @return 返回商品列表
	 */
	public List<Goods> getGoodsListByBrandId(int brandId, GoodsQueryMode mode);
	
	/**
	 * 获取商品列表
	 * @param typeId 商品店铺分类ID
	 * @param start 查询的起始记录
	 * @param limit 查询的总记录数, 如果值为-1表示查询到最后
	 * @param mode 商品查询模式
	 * @return 返回商品列表
	 */
	public List<Goods> getGoodsListByTypeId(int typeId, GoodsQueryMode mode);
	
	/**
	 * 获取商品列表
	 * @param params 查询参数
	 * @param sorters 记录的排序，如sorters.put("id","desc")，该参数如果为空表示按默认排序
	 * @param start 查询的起始记录,可为null
	 * @param limit 查询的总记录数, 如果值为-1表示查询到最后,可为null
	 * @param mode 商品查询模式
	 * @return 返回商品列表
	 */
	public List<Goods> getGoodsList(Map<String,Object> params, Map<String,String> sorters, Long start, Long limit, GoodsQueryMode mode);
	
	/**
	 * 获取商品数量
	 * @param params 查询参数
	 * @return 返回商品数量
	 */
	public long getGoodsCount(Map<String,Object> params);
	
	
	/**
	 * 恢复商品
	 * @param goodsIds 商品ID列表
	 */
	public void recoveryGoods(long[] goodsIds);
	
	/**
	 * 删除商品，只做虚拟删除
	 * @param goodsIds 商品ID列表
	 */
	public void delGoods(long[] goodsIds);
	
	/**
	 * 彻底删除商品
	 * @param goodsIds 商品ID列表
	 */
	public void delRealGoods(long[] goodsIds);

	
	
	/**
	 * 添加商品
	 * @param goods 商品对象
	 */
	public void addGoods(Goods goods);	
	
	/**
	 * 修改商品
	 * @param goods 商品对象
	 */
	public void editGoods(Goods goods);	
	
	/**
	 * 上架商品
	 * @param goodsIds 商品ID列表
	 */
	public void upShelfGoods(final long[] goodsIds);
	
	/**
	 * 下架架商品
	 * @param goodsIds 商品ID列表
	 */
	public void offShelfGoods(final long[] goodsIds);
	
	/**
	 * 设置价格
	 * @param goodsId 商品ID
	 * @param shopPrice 店铺价格
	 */
	public void modifyPrice(long goodsId, BigDecimal shopPrice);	
	
	/**
	 * 设置价格
	 * @param goodsId 商品ID
	 * @param shopPrice 店铺价格
	 */
	public void modifyInventory(long goodsId, int shopPrice);
	
	
	
	/**
	 * 商品查询模式
	 * @author Linpn
	 */
	public static enum GoodsQueryMode {
		/**
		 * 简单查询，适用于列表查询，该查询不查出扩展属性(properties)和商品描述(goodsDesc)
		 */
		SIMPLE,
		/**
		 * 带商品图片查询，该查询将查出一组商品图片(imageGroups)
		 */
		WITH_IMGS,
		/**
		 * 带扩展属性查询，该查询将查出扩展属性(properties)
		 */
		WITH_PROPS,
		/**
		 * 带商品描述查询，该查询将查出商品描述(goodsDesc)
		 */
		WITH_BLOB,
		/**
		 * 全部查询，该查询将会查出扩展属性(properties)和商品描述(goodsDesc)
		 */
		ALL
	}
	
}
