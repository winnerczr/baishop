package com.baishop.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.controller.PageBaiController;
import com.baishop.entity.goods.Goods;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
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

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		
		List<Goods> goodsList = goodsService.getGoodsList(GoodsQueryMode.SIMPLE);
		modeview.addObject("goodsList", goodsList);
		
		System.out.println(goodsList);
	}

	
}
