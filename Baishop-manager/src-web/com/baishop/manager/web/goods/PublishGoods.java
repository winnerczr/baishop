package com.baishop.manager.web.goods;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.goods.Brands;
import com.baishop.entity.goods.Goods;
import com.baishop.entity.goods.GoodsProps;
import com.baishop.entity.goods.Properties;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;
import com.baishop.service.goods.BrandsService;
import com.baishop.service.goods.GoodsService;
import com.baishop.service.goods.PropertiesService;
import com.baishop.service.goods.GoodsService.GoodsQueryMode;

/**
 * 我要卖、编辑商品 页面
 * @author Linpn
 */
public class PublishGoods extends PageManagerController {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private BrandsService brandsService;
	@Autowired
	private PropertiesService propertiesService;
	
	@Autowired
	private Validator goodsValidator;
	
	
	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {

		//判断是添加不是编辑
		String action = request.getParameter("action");
		if(action!=null && action.equals("edit")){
			String goodsId = request.getParameter("goodsId");
			if(goodsId==null)
				return;
			
			Goods goods = goodsService.getGoods(Long.valueOf(goodsId), GoodsQueryMode.ALL);			
			modeview.addObject("goods", goods);
			modeview.addObject("module_title", "编辑商品");
		}
		
		
		//商品类目树和叶子列表
		String treeCategory = this.getTreeCategoryOfJSON().toString();
		modeview.addObject("treeCategory", treeCategory);
		
		//加载商品品牌列表
		List<Brands> cbbBrands = brandsService.getBrandsList();
		modeview.addObject("cbbBrands", JSONArray.fromObject(cbbBrands));
		
	}
		
	
	/**
	 * Ajax请求：保存商品
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveGoods(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String goodsId = request.getParameter("goodsId");			//商品ID
			String cateId = request.getParameter("cateId");				//商品所属类别
			String brandId = request.getParameter("brandId");			//品牌
			String typeId = request.getParameter("typeId");				//店铺自定义分类
			String goodsName = request.getParameter("goodsName");		//商品的名称
			String goodsImage = request.getParameter("goodsImage");		//商品图片URL
			String goodsNumber = request.getParameter("goodsNumber");	//商品库存数量
			String weight = request.getParameter("weight");				//商品的重量
			String marketPrice = request.getParameter("marketPrice");	//市场售价
			String shopPrice = request.getParameter("shopPrice");		//本店售价
			String goodsBrief = request.getParameter("goodsBrief");		//商品的简短描述
			String goodsDesc = request.getParameter("goodsDesc");		//商品的详细描述
			String sellerNote = request.getParameter("sellerNote");		//商家备注
			String isReal = request.getParameter("isReal");				//是否是实物
			String isOnSale = request.getParameter("isOnSale");			//是否上架销售
			String hasInvoice = request.getParameter("hasInvoice");		//是否有发票
			String hasWarranty = request.getParameter("hasWarranty");	//是否有保修
			
			//创建对象
			Goods goods = new Goods();
			goods.setCateId(Integer.valueOf(cateId));
			goods.setBrandId(Integer.valueOf(brandId));
			goods.setTypeId(Integer.valueOf(typeId));
			goods.setGoodsName(goodsName);
			goods.setGoodsImage(goodsImage);
			goods.setGoodsNumber(Integer.valueOf(goodsNumber));
			goods.setWeight(new BigDecimal(weight));
			goods.setMarketPrice(new BigDecimal(marketPrice));
			goods.setShopPrice(new BigDecimal(shopPrice));
			goods.setGoodsBrief(goodsBrief);
			goods.setGoodsDesc(goodsDesc);
			goods.setSellerNote(sellerNote);
			goods.setIsReal(Byte.valueOf(isReal));
			goods.setIsOnSale(Byte.valueOf(isOnSale));
			goods.setHasInvoice(Byte.valueOf(hasInvoice));
			goods.setHasWarranty(Byte.valueOf(hasWarranty));
			goods.setPublishTime(goods.getIsOnSale().byteValue()==1?new Date():null);
						
			//验证对象
			Errors errors = new BindException(goods,"goods");
			goodsValidator.validate(goods, errors);			
			if (errors.hasErrors()) {
				out.println("{success: false, msg: '"+ errors.getAllErrors().get(0).getDefaultMessage() +"'}");
				return;
			}	
			
			if(goodsId==null || goodsId.equals("")){
				//添加对象
				goods.setGoodsSn(UUID.randomUUID().toString().toUpperCase());
				goods.setCreateTime(new Date());
				goodsService.addGoods(goods);
			}else{
				//编辑对象
				goods.setGoodsId(Long.valueOf(goodsId));
				goods.setUpdateTime(new Date());
				goodsService.editGoods(goods);
			}
			
			//输出数据
			out.println("{success: true}");

		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：获取商品属性
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getGoodsProps(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			String cateId = request.getParameter("cateId");
			String goodsId = request.getParameter("goodsId");	
						
			List<Properties> propsList = propertiesService.getPropertiesList(Integer.valueOf(cateId));
			List<GoodsProps> goodsProps = new ArrayList<GoodsProps>();
			
			if(goodsId!=null && !goodsId.equals("")){
				Goods goods = goodsService.getGoods(Long.valueOf(goodsId),GoodsQueryMode.WITH_PROPS);
				if(goods!=null)
					goodsProps = goods.getProperties();
			}			
		
			//输出数据
			JSONObject json = new JSONObject();
			json.put("success", true);
			
			JSONObject data = new JSONObject();			
			for(Properties prop : propsList){
				String value = "";
				for(GoodsProps goodsProp : goodsProps){
					if(prop.getPropsId().equals(goodsProp.getPropsId()))
						value = goodsProp.getPropsValue();
				}
				data.put(prop.getPropsName(), value);
			}			
			json.put("data", data);
						
			out.println(json.toString());			
	
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	

	

	

}
