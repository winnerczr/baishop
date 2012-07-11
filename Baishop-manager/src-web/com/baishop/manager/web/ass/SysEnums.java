package com.baishop.manager.web.ass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.ass.Enums;
import com.baishop.framework.utils.ConvertUtils;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;

public class SysEnums extends PageManagerController {

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
	
	}
	

	/**
	 * Ajax请求：获取系统枚举列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getEnums(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String callback=request.getParameter("callback");
		
		try{
			//获取参数
			String searchKey = request.getParameter("searchKey", "UTF-8");
			
			//查询枚举
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("searchKey", searchKey);
			
			//查询数据
			List<Enums> list = enumsService.getEnumsList(params);
			
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
	 * Ajax请求：保存枚举
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveEnums(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{						
			//创建对象
			Enums enums = request.getBindObject(Enums.class, "enums");
			
			if(enums.getEnumsId()==null){
				//添加对象
				enumsService.addEnums(enums);
			}else{
				//编辑对象
				enumsService.editEnums(enums);
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
	 * Ajax请求：删除枚举
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delEnums(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String enumsIds = request.getParameter("enumsIds");	
			int[] _enumsIds = ConvertUtils.toArrInteger(enumsIds.split(","));
			
			enumsService.delEnums(_enumsIds);
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}

}
