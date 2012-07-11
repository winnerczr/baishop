/**
 * 登录
 */
$(document).ready(function(){
	// 跳转URL
	var redUrl = "";	
	if(location.href.indexOf("?")>=0){
		redUrl = location.href.substring(location.href.indexOf("?")+1);
	}
	
	// 设置注册按钮事件
	$("#btnZC").click(function(){
		if(redUrl==""){
			location.href = "/register.html";
		}else{
			location.href = "/register.html?"+ redUrl;
		}
	});
	
	// 设置跳转URL提交项
	$("#redUrl").attr("value",redUrl);

});