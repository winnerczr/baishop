package com.baishop.manager.web.ass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.ass.City;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;

public class SysCity extends PageManagerController {

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		
	}
	
	
	/**
	 * Ajax请求：获取用户应用模块列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getCity(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			int node = request.getIntParameter("node");
			
			//组装JSON
			List<City> list = cityService.getCityListByPid(node);
			JSONArray json = new JSONArray();
			
			for(City city : list){
				JSONObject record = JSONObject.fromObject(city);
				record.put("id", city.getCityId());
				record.put("text", city.getCityName());
				record.put("iconCls", "icon-cls");
				json.add(record);
			}

			//输出数据
			out.println(json);
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
		
	/**
	 * Ajax请求：保存应用模块
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveCity(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{			
			//获取对象
			City city = request.getBindObject(City.class, "city");			
			City parent = cityService.getCity(city.getCityPid());
			
			//判断编码是否正确
			if(parent!=null){
				String code = city.getCityCode();
				if(!parent.getCityCode().equals(code.substring(0, code.length()-2))){
					out.println("{success: false, msg: '编码输入不正确，编码格式为：父编码+两位数字！'}");
					return;
				}
			}
			
			if(city.getCityId()==null){
				//添加对象
				cityService.addCity(city);
			}else{
				//编辑对象
				cityService.editCity(city);
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
	 * Ajax请求：删除应用模块
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delCity(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String cityId = request.getParameter("cityId");	
			
			cityService.delCity(Integer.valueOf(cityId));
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
}
