package com.baishop.web;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.controller.PageBaiController;
import com.baishop.entity.sale.Users;
import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.service.sale.UsersService;

public class Login extends PageBaiController {
	
	@Autowired
	private UsersService usersService;

	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		// TODO Auto-generated method stub

	}
	
	
	public void login(HttpServletExtendRequest request, HttpServletExtendResponse response) 
			throws IOException {
		PrintWriter out = response.getWriter();
		
		try {
			//获取参数
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			//获取用户
			Users user = usersService.getUsers(username, password);
			if(user!=null){
				request.getSession().setAttribute("username", user.getUsername());
				request.getSession().setAttribute("password", user.getPassword());
				
				//登录成功，跳转页面
				String redUrl = request.getParameter("redUrl");						
				response.sendRedirect(redUrl);
				
			}else{
				out.print("{success:false, msg:'用户名或密码不正确!'}");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{		
			out.close();
		}
	}

}
