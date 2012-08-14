<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${app_title}</title>
<script type="text/javascript">if(top.location!=self.location) top.location=self.location;</script>
<link href="WEB-RES/css/login/login.css" type=text/css rel=stylesheet />
<script src="WEB-RES/script/jquery/jquery-1.5.2.js" type="text/javascript"></script>
<script src="WEB-RES/script/jquery/jquery.cookie.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#j_username").val($.cookie("remember_j_username"));
		
		if($.cookie("remember_j_username")){
			$("#j_password").focus();
		}else{
			$("#j_username").focus();
		}
		
		$("#frmindex").submit(function(){
			$.cookie("remember_j_username", $("#j_username").val(), {expires: 10});
		});
	});
</script>
</head>
<body class="loginBody">
	<div class="loginMain">
		<div class="fl erp_img erp_logo"></div>
		<fieldset class="fl erp_img erp_login">
			<form id="frmindex" name="frmindex" method="post" action="j_spring_security_check">
				<table class="erp_tableLogin">
					<tr>
						<td class="td1"></td><td>
							<span id="msg" class="loginEor fz14">${result}</span>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="td1 fz18"><label for="name">用户名：</label></td><td>
							<input class="input_text" id="j_username" name="j_username" value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}" maxlength=20 tabindex="0" />
						</td>
					</tr>
					<tr>
						<td class="td1 fz18"><label for="pwd">密　码：</label></td><td>
							<input class="input_text" id="j_password" name="j_password" type="password" />						
						</td>
					</tr>
					<tr>
						<td class="td1"></td>
						<td>
							<label>
								<input type="checkbox" name="_spring_security_remember_me" class="fl checkBox"/>
								<span class="checkBoxTips">两周之内不必登陆</span>
							</label>
						</td>
					</tr>
					<tr>
						<td class="td1"></td>
						<td>
	                        <input class="subBtn" name="submit" accesskey="l" value="" tabindex="4" type="submit" />							
						</td>
					</tr>
				</table>
			</form>
		</fieldset>
		<div class="fl erp_img erp_rightDiv"></div>
	</div>
</body>
</html>