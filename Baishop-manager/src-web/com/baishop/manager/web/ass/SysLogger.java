package com.baishop.manager.web.ass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.aspect.SyslogAspect;
import com.baishop.entity.ass.Params;
import com.baishop.entity.ass.Syslog;
import com.baishop.framework.json.JsonConfigGlobal;
import com.baishop.framework.utils.DateUtils;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;
import com.baishop.service.ass.SyslogService;

public class SysLogger extends PageManagerController {
	
	@Autowired
	private SyslogService syslogService;
	@Autowired
	private SyslogAspect syslogAspect;

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {

	}
	
	
	/**
	 * Ajax请求：获取日志列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getSyslog(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String callback=request.getParameter("callback");
		
		try{
			//获取参数
			String sort = request.getParameter("sort");
			String dir = request.getParameter("dir");
			Long start = request.getLongParameter("start");
			Long limit = request.getLongParameter("limit");
			String searchKey = request.getParameter("searchKey", "UTF-8");
			Integer source = request.getIntParameter("source");
			Date startDate = request.getDateParameter("startDate");
			Date endDate = request.getDateParameter("endDate");
			
			if(endDate!=null)
				endDate = DateUtils.addDay(endDate,1);
						
			//查询参数
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("searchKey", searchKey);
			params.put("source", source);
			params.put("startDate", startDate);
			params.put("endDate", endDate);
			
			//排序数据
			Map<String,String> sorters = new HashMap<String,String>();
			sorters.put(sort, dir);
						
			//查询数据
			List<Syslog> list = syslogService.getSyslogList(params, sorters, start, limit);
			long count = syslogService.getSyslogCount(params);
			
			//组装JSON
			JSONObject json = new JSONObject();
			json.put("count", count);
			json.put("records", JSONArray.fromObject(list, JsonConfigGlobal.jsonConfig));
			
			//输出数据
			out.println(callback + "("+ json +")");
			
		}catch(Exception e){
			out.println(callback + "({success: false, msg: '"+ e.getMessage() +"'})");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
	/**
	 * Ajax请求：获取日志列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getSyslogFilter(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取过滤数据
			Params params = paramsService.getParams("logger_filter");
			String filter = params.getParamsValue();
			
			//输出数据
			out.println("{success: true, data: '"+ filter.replaceAll("\n","\t") +" '}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}	
	
	
	/**
	 * Ajax请求：保存日志过滤器
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveSyslogFilter(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取过滤数据			
			String filter = request.getParameter("filter");			
			Params params = paramsService.getParams("logger_filter");
			params.setParamsValue(filter);
			paramsService.editParams(params);			
			syslogAspect.clearFilterCache();
			
			//输出数据
			out.println("{success: true}");

		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
}
