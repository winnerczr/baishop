package com.baishop.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.controller.PageBaiController;
import com.baishop.entity.goods.Goods;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.service.ass.SyslogService;
import com.baishop.service.goods.GoodsService;
import com.baishop.service.goods.GoodsService.GoodsQueryMode;

/**
 * 首页
 * @author Linpn
 *
 */
public class Index extends PageBaiController {
	
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private SyslogService syslogService;

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		
		try{
			List<Goods> goodsList = goodsService.getGoodsList(GoodsQueryMode.SIMPLE);
			modeview.addObject("goodsList", goodsList);
			
			System.out.println(goodsList);
			
			syslogService.logger("记录日志");
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
