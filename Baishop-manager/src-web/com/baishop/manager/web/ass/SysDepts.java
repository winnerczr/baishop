package com.baishop.manager.web.ass;

import java.io.IOException;
import java.io.PrintWriter;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.entity.ass.Depts;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;
import com.baishop.service.ass.DeptsService;

public class SysDepts extends PageManagerController {

	@Autowired
	protected DeptsService deptsService;	

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		
	}
	
	
	/**
	 * Ajax请求：获取部门列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getDepts(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//组装JSON
			JSONObject json = deptsService.getTreeDeptOfJSON();

			//输出数据
			out.println(json);
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
		
	/**
	 * Ajax请求：保存部门
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void saveDepts(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{			
			//获取对象
			Depts dept = request.getBindObject(Depts.class, "dept");			
			Depts parent = deptsService.getDepts(dept.getDeptPid());

			//判断编号是否正确
			if(parent!=null){
				String code = dept.getDeptCode();
				if(!parent.getDeptCode().equals(code.substring(0, code.length()-2))){
					out.println("{success: false, msg: '编号输入不正确，编号格式为：父编号+两位数字！'}");
					return;
				}
			}
			
			if(dept.getDeptId()==null){
				//添加对象
				deptsService.addDepts(dept);
			}else{
				//编辑对象
				deptsService.editDepts(dept);
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
	 * Ajax请求：删除部门
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delDepts(HttpServletExtendRequest request, 
			HttpServletExtendResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		try{
			//获取参数
			String deptId = request.getParameter("deptId");	
			
			deptsService.delDepts(Integer.valueOf(deptId));
			
			//输出数据
			out.println("{success: true}");
			
		}catch(Exception e){
			out.println("{success: false, msg: '"+ e.getMessage() +"'}");
			e.printStackTrace();
		}
		
		out.close();
	}
	
	
}
