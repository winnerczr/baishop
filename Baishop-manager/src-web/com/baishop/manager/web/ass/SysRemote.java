package com.baishop.manager.web.ass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.ass.RemoteAuths;
import com.baishop.framework.utils.ConvertUtils;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;
import com.baishop.service.ass.RemoteAuthsService;

public class SysRemote extends PageManagerController {
	
	@Autowired
	private RemoteAuthsService remoteAuthsService;

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {

	}
	
	
	/**
	 * Ajax请求：获取接口权限列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getRemoteAuths(HttpServletExtendRequest request, 
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
			List<RemoteAuths> list = remoteAuthsService.getRemoteAuthsList(params);
			
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
	 * Ajax请求：保存接口权限
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveRemoteAuths(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{			
			//创建对象
			RemoteAuths remoteAuths = request.getBindObject(RemoteAuths.class, "remoteAuths");
			
			if(remoteAuths.getId()==null){
				//添加对象
				remoteAuthsService.addRemoteAuths(remoteAuths);
			}else{
				//编辑对象
				remoteAuthsService.editRemoteAuths(remoteAuths);
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
	 * Ajax请求：删除接口权限
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delRemoteAuths(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			int[] ids = ConvertUtils.toArrInteger(request.getParameter("ids").split(","));
			
			remoteAuthsService.delRemoteAuths(ids);
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
}
