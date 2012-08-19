package com.baishop.manager.web;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.servlet.ModelAndView;

import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.HttpServletExtendResponse;
import com.baishop.manager.controller.PageManagerController;

public class Login extends PageManagerController {
	
	@Override
	public void execute(HttpServletExtendRequest request,
			HttpServletExtendResponse response, ModelAndView modeview) {
		
		// 输出错误信息
		String result = "";
		Exception e = (Exception)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

		if(e!=null){
			result = e.getMessage();
		}		
		if(e instanceof AuthenticationServiceException){
			result = "用户名不存在";
		} else 
		if(e instanceof BadCredentialsException){
			result = "密码错误";
		} else 
		if(e instanceof DisabledException){
			result = "账号未启用";
		}else
		if(e instanceof LockedException){
			result = "账号不允许登录";
		}
		
		modeview.addObject("result", result);
	}
	
	
//	/**
//	 * 判断验证码
//	 */
//	public void checkCaptcha(HttpServletExtendRequest request, 
//			HttpServletExtendResponse response) throws IOException {
//
//		PrintWriter out = response.getWriter();
//		
//		String captcha = request.getParameter("captcha");
//		String kaptcha = (String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
//		
//		if(captcha!=null && kaptcha!=null & kaptcha.toLowerCase().equals(captcha.toLowerCase())){
//			out.print(true);
//		}else{
//			out.print(false);
//		}
//		
//		out.close();
//	}

}
