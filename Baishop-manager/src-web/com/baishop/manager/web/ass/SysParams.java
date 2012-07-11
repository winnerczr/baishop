package com.baishop.manager.web.ass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.ass.Params;
import com.baishop.framework.utils.ConvertUtils;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;

public class SysParams extends PageManagerController {

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
	
	}
	

	/**
	 * Ajax请求：获取系统参数列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getParams(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String callback=request.getParameter("callback");
		
		try{
			//获取参数
			String searchKey = request.getParameter("searchKey", "UTF-8");
			
			//查询参数
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("searchKey", searchKey);
			
			//查询数据
			List<Params> list = paramsService.getParamsList(params);
			
			//组装JSON
			JSONObject json = new JSONObject();
			json.put("records", JSONArray.fromObject(list));
			
			//输出数据
			out.println(callback + "("+ json +")");
			
		}catch(Exception e){
			out.println(callback + "({success: false, msg: '"+ e.getMessage() +"'})");
			e.printStackTrace();
		}
		
		out.close();
	}
	
		
	/**
	 * Ajax请求：保存参数
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveParams(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{						
			//创建对象
			Params params = request.getBindObject(Params.class, "params");
			
			if(params.getParamsId()==null){
				//添加对象
				paramsService.addParams(params);
			}else{
				//编辑对象
				paramsService.editParams(params);
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
	 * Ajax请求：删除参数
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delParams(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String paramsIds = request.getParameter("paramsIds");	
			int[] _paramsIds = ConvertUtils.toArrInteger(paramsIds.split(","));
			
			paramsService.delParams(_paramsIds);
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}

}
