package com.baishop.manager.web.goods;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.goods.Goods;
import com.baishop.framework.json.JsonConfigGlobal;
import com.baishop.framework.utils.ConvertUtils;
import com.baishop.framework.utils.DateUtils;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;
import com.baishop.service.goods.GoodsService;
import com.baishop.service.goods.GoodsService.GoodsQueryMode;

/**
 * 出售中的商品页面
 * @author Linpn
 */
public class GoodsManage extends PageManagerController {

	@Autowired
	private GoodsService goodsService;
	
	
	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		
	}
	
	
	/**
	 * Ajax请求：获取出售中的商品列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getGoodsList(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String callback=request.getParameter("callback");
			String sort = request.getParameter("sort");
			String dir = request.getParameter("dir");
			long start = request.getParameter("start")==null?0:Integer.valueOf(request.getParameter("start"));
			long limit = request.getParameter("limit")==null?0:Integer.valueOf(request.getParameter("limit"));
			String isOnSale = request.getParameter("isOnSale");
			String isDelete = request.getParameter("isDelete");
			String searchKey = request.getParameter("searchKey", "UTF-8");
			
			//查询参数
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("isOnSale", isOnSale);
			params.put("isDelete", isDelete);
			params.put("searchKey", searchKey);
			
			//排序数据
			Map<String,String> sorters = new HashMap<String,String>();
			sorters.put(sort, dir);
			
			//查询数据
			List<Goods> list = goodsService.getGoodsList(params, sorters, start, limit, GoodsQueryMode.SIMPLE);
			long count = goodsService.getGoodsCount(params);
			
			//组装JSON
			JSONObject json = new JSONObject();
			json.put("count", count);
			json.put("records", new JSONArray());
			
			JSONArray records = json.getJSONArray("records");
			for(Goods good : list) {
				JSONObject record = JSONObject.fromObject(good, JsonConfigGlobal.jsonConfig);							
				record.put("publishTime", DateUtils.format(good.getPublishTime(), "yyyy-MM-dd\nHH:mm").replace("\n", "<br/>"));
				
				records.add(record);
			}
			
			//输出数据
			out.println(callback + "("+ json +")");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：恢复商品
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void recoveryGoods(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String goodsIds = request.getParameter("goodsIds");	
			long[] _goodsIds = ConvertUtils.toArrLong(goodsIds.split(","));	
			
			goodsService.recoveryGoods(_goodsIds);
			
			//输出数据
			out.println("{success: true}");

		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：删除商品
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delGoods(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String goodsIds = request.getParameter("goodsIds");	
			long[] _goodsIds = ConvertUtils.toArrLong(goodsIds.split(","));
			
			goodsService.delGoods(_goodsIds);
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：彻底删除商品
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delRealGoods(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String goodsIds = request.getParameter("goodsIds");	
			long[] _goodsIds = ConvertUtils.toArrLong(goodsIds.split(","));
			
			goodsService.delRealGoods(_goodsIds);
			
			//输出数据
			out.println("{success: true}");

		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：清空已删除的商品
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void clearDeledGoods(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取对象
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("isDelete", 1);			
			List<Goods> list = goodsService.getGoodsList(params, null, null, null, GoodsQueryMode.SIMPLE);
			
			long[] goodsIds = new long[list.size()];
			for(int i=0;i<goodsIds.length;i++){
				Goods goods = (Goods)list.get(i);
				goodsIds[i] = goods.getGoodsId();
			}
			
			goodsService.delRealGoods(goodsIds);
			
			//输出数据
			out.println("{success: true}");

		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
		
	/**
	 * Ajax请求：上架商品
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void upShelfGoods(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String goodsIds = request.getParameter("goodsIds");	
			long[] _goodsIds = ConvertUtils.toArrLong(goodsIds.split(","));
			
			goodsService.upShelfGoods(_goodsIds);
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：下架商品
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void offShelfGoods(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String goodsIds = request.getParameter("goodsIds");	
			long[] _goodsIds = ConvertUtils.toArrLong(goodsIds.split(","));
			
			goodsService.offShelfGoods(_goodsIds);
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	
	/**
	 * Ajax请求：设置价格
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void modifyPrice(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String goodsId = request.getParameter("goodsId");
			String shopPrice = request.getParameter("shopPrice");
			
			goodsService.modifyPrice(Long.valueOf(goodsId), new BigDecimal(shopPrice));
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	
	/**
	 * Ajax请求：设置价格
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void modifyInventory(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String goodsId = request.getParameter("goodsId");
			String goodsNumber = request.getParameter("goodsNumber");
			
			goodsService.modifyInventory(Long.valueOf(goodsId), Integer.valueOf(goodsNumber));
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	


}
