/**
 * 注册
 */

$import('/WEB-RES/script/Regexp.js');

$(document).ready(function(){
	// 跳转URL
	var redUrl = "";	
	if(location.href.indexOf("?")>=0){
		redUrl = location.href.substring(location.href.indexOf("?")+1);
	}
	
	// 设置注册按钮事件
	$("#btnZC").click(function(){
		if(redUrl==""){
			location.href = "/login.html";
		}else{
			location.href = "/login.html?"+ redUrl;
		}
	});
	
	// 设置跳转URL提交项
	$("#redUrl").attr("value",redUrl);
	
	
	//账户输入校验
	$("#username").focus();
	$("#username").focusout(function(){
		var val = $(this).val();
		if(val==null || val==""){
			$(this).addClass("highlight");
			$("#username-hint").addClass("wrong");
			$("#username-hint").text("请输入Email");
		}else if(!RegexpEnum.REG_EMAIL.test(val)){
			$(this).addClass("highlight");
			$("#username-hint").addClass("wrong");
			$("#username-hint").text("Email格式有误，请重新输入");
		}else{
			$(this).removeClass("highlight");
			$("#username-hint").removeClass("wrong");
			$("#username-hint").text("");
		}
	}).keydown(function(event){
		if(event.keyCode==13){
			$("#nickname").focus();
			return false;
		}
	});
	
	//昵称输入检验
	$("#nickname").focusout(function(){
		var val = $(this).val();
		if(val==null || val==""){
			$(this).addClass("highlight");
			$("#nickname-hint").addClass("wrong");
			$("#nickname-hint").text("请输入昵称");
		}else if(val.length<2){
			$(this).addClass("highlight");
			$("#nickname-hint").addClass("wrong");
			$("#nickname-hint").text("昵称太短，最少 2 个字符");			
		}else if(!RegexpEnum.REG_USERNAME.test(val)){
			$(this).addClass("highlight");
			$("#nickname-hint").addClass("wrong");
			$("#nickname-hint").text("昵称格式有误，请重新输入");
		}else{
			$(this).removeClass("highlight");
			$("#nickname-hint").removeClass("wrong");
			$("#nickname-hint").text("");
		}
	}).keydown(function(event){
		if(event.keyCode==13){
			$("#password").focus();
			return false;
		}
	});
	
	//密码输入检验
	$("#password").focusout(function(){
		var val = $(this).val();
		if(val==null || val==""){
			$(this).addClass("highlight");
			$("#password-hint").addClass("wrong");
			$("#password-hint").text("请输入密码");	
		}else if(!RegexpEnum.REG_PASSWORD.test(val)){
			$(this).addClass("highlight");
			$("#password-hint").addClass("wrong");
			$("#password-hint").text("请设为6-16位字母或数字");
		}else{
			$(this).removeClass("highlight");
			$("#password-hint").removeClass("wrong");
			$("#password-hint").text("");
		}
	}).keydown(function(event){
		if(event.keyCode==13){
			$("#password2").focus();
			return false;
		}
	});
	
	//确认密码输入检验
	$("#password2").focusout(function(){
		var val = $(this).val();
		if(val==null || val==""){
			$(this).addClass("highlight");
			$("#password2-hint").addClass("wrong");
			$("#password2-hint").text("请输入确认密码");	
		}else if(val!=$("#password").val()){
			$(this).addClass("highlight");
			$("#password2-hint").addClass("wrong");
			$("#password2-hint").text("两次密码不一致，请重新输入");
		}else{
			$(this).removeClass("highlight");
			$("#password2-hint").removeClass("wrong");
			$("#password2-hint").text("");
		}
	}).keydown(function(event){
		if(event.keyCode==13){
			$("#captcha").focus();
			return false;
		}
	});
	
	//验证码输入检验
	$("#captcha").focusout(function(){
		var val = $(this).val();
		if(val==null || val==""){
			$(this).addClass("highlight");
			$("#captcha-hint").addClass("wrong");
			$("#captcha-hint").text("请输入验证码");	
		}else{
			$(this).removeClass("highlight");
			$("#captcha-hint").removeClass("wrong");
			$("#captcha-hint").text("");
		}
	}).keydown(function(event){
		if(event.keyCode==13){
			return $("#btnRegister").click();
		}
	});
	
	
	//完成注册按钮
	$("#btnRegister").click(function(){
		alert(1);
		
		return false;
	});
	
	
});