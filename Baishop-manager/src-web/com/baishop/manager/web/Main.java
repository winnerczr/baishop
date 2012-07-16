package com.baishop.manager.web;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;
import com.baishop.service.ass.ModulesService;

public class Main extends PageManagerController {

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		// 获取模块
		JSONObject modules = this.getTreeModulesOfJSON(this.getCurrUser(),
															this.appConf.getProperty("app.name", "", "UTF-8"),
															new String[] { 
																ModulesService.SYSTEM,
																ModulesService.GROUP,
																ModulesService.MODULE
															});
		modeview.addObject("treeModules", modules.getJSONArray("children").get(0));
		modeview.addObject("leafModules", this.getLeafModulesOfJSON());
		modeview.addObject("listSystems", this.getListSystemsOfJSON());

	}

}
