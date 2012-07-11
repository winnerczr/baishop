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
import com.google.code.kaptcha.Constants;

public class Register extends PageBaiController {

	@Autowired
	private UsersService usersService;
	
	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		// TODO Auto-generated method stub

	}
	
	
	public void register(HttpServletExtendRequest request, HttpServletExtendResponse response) 
			throws IOException {
		PrintWriter out = response.getWriter();
		
		try {
			// 判断验证码
			String captcha = request.getParameter("captcha")==null?"":request.getParameter("captcha");
			String kaptcha = (String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
			if(!captcha.equals(kaptcha)) {
				out.print("{success:false, msg:'验证码不正确!'}");
				return;
			}
						
			// 获取参数
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String nickname = request.getParameter("nickname");
			String subscribe = request.getParameter("subscribe");
			
			// 插入数据
			Users user = new Users();
			user.setUsername(username);
			user.setPassword(password);
			user.setNickname(nickname);
			user.setIsSubscribe(Byte.valueOf(subscribe));

			usersService.addUsers(user);
			
			// 注册成功，跳转页面
			String redUrl = request.getParameter("redUrl");						
			response.sendRedirect(redUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{		
			out.close();
		}
	}

}
