package com.baishop.manager.web;

import org.springframework.web.servlet.ModelAndView;

import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;

public class Main extends PageManagerController {

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		//执行首页的公共代码
		super.doMainPage(request, response, modeview);
	}

}
