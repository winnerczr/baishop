<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${app_title}</title>
<script type="text/javascript">if(top.location!=self.location) top.location=self.location;</script>
<link href="WEB-RES/css/user_login.css" type=text/css rel=stylesheet />
<script src="WEB-RES/script/jquery/jquery-1.5.2.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		// 用户名焦点
		$("#j_username").focus();
		
		// 登录按钮事件
		$("#ibtnenter").click(function(){
			// 验证输入框
			if($("#j_username").val()==""){
				alert("用户名不能为空！");
				return false;
			}
			if($("#j_password").val()==""){
				alert("密码不能为空！");
				return false;
			}
			if($("#j_captcha").val()==""){
				alert("验证码不能为空！");
				$("#kaptcha").click();
				return false;
			}
			
			// 判断验证码
			var sUrl = "${page_action}?func=checkCaptcha&captcha="+ $("#j_captcha").val();
			var checkResult = $.ajax({ url: sUrl, async: false }).responseText;
			if(checkResult!="true"){
				alert("验证码不正确！");
				$("#kaptcha").click();
				$("#j_captcha").val("");
				return false;
			}
		});
	});
</script>
</head>
<body id=userlogin_body>
	<form id="frmindex" name="frmindex" method="post" action="j_spring_security_check">
		<div></div>
		<div id=user_login>
			<dl>
				<dd id=user_top>
					<ul>
						<li class=user_top_l></li>
						<li class=user_top_c></li>
						<li class=user_top_r></li>
					</ul>
				</dd>
				<dd id=user_main>
					<ul>
						<li class=user_main_l></li>
						<li class=user_main_c>
							<div class=user_main_box>
								<table border="0" cellspacing="0" cellpadding="5">
									<tr>
										<td width="48">用户名：</td>
										<td><input class="txtusernamecssclass" id="j_username" name="j_username" value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}" maxlength=20 tabindex="0" /></td>
									</tr>
									<tr>
										<td>密 码：</td>
										<td><input class="txtpasswordcssclass" id="j_password" name="j_password" type="password" /></td>
									</tr>
									<tr>
										<td>验证码：</td>
										<td>
											<table width="0" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td><input class="txtvalidatecodecssclass" id="j_captcha" name="j_captcha" maxlength="4" /></td>
													<td>
														<img id="kaptcha" src="kaptcha.jpg"
														style="border: none; cursor: pointer; width: 70px; height: 22px"
														onclick="this.src='kaptcha.jpg?'+Math.floor(Math.random()*100);" />
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td align="center">
											<input type="checkbox" name="_spring_security_remember_me" />两周之内不必登陆
										</td>
									</tr>
									<tr>
										<td colspan="2" align="center"><span style="color: #ff0000">${result}</span></td>
									</tr>
								</table>
							</div>
						</li>
						<li class=user_main_r>
							<input type="image" id="ibtnenter" name="ibtnenter" 
								class="ibtnentercssclass"
								src="WEB-RES/images/user_botton.gif"
							/>
						</li>
					</ul>
				</dd>
				<dd id=user_bottom>
					<ul>
						<li class=user_bottom_l></li>
						<li class=user_bottom_c></li>
						<li class=user_bottom_r></li>
					</ul>
				</dd>
				<dd class="copyright">
					${mall_copyright}
					<br />
					<a href="SRV-API/index.html" target="_blank">服务接口API</a>　
					<a href="SRV-API/baishop-service.zip" target="_blank">软件包下载</a>
				</dd>
			</dl>
		</div>
	</form>
</body>
</html>