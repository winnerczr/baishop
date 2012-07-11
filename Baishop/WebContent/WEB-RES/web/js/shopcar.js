/**
 * 购物车
 */
$(document).ready(function(){
	//选择分类时样式
	$(".selectareali").mouseover(function() { 
		$(this).attr("id", "selectlihover"); 
	}).mouseout(function() { 
		$(this).attr("id", ""); 
	});
    
    
    //宝贝详情	
	var s = null;	
	$(".pic").mouseover(function() {
		var _this = this;
		$("#detail #pic").attr("src","");
		s = setTimeout(function(){
			$("#detail #pic").attr("src", $(_this).find(".productInfo .pic").text());
			$("#detail #productName").text($(_this).find(".productInfo .productName").text());
			$("#detail #productCode").text($(_this).find(".productInfo .productCode").text());
			$("#detail #sprice").text($(_this).find(".productInfo .sprice").text());
			$("#detail #vprice").text($(_this).find(".productInfo .vprice").text());
			$("#detail #price").text($(_this).find(".productInfo .price").text());
			$("#detail #commentsNumber").html($(_this).find(".productInfo .commentsNumber").html());
			$("#detail #comments").text($(_this).find(".productInfo .comments").text());			
			$("#imgICO").css({"left": $(_this).offset().left+$(_this).width() + "px", "top": $(_this).offset().top + $(_this).height()/2-10 + "px"}).fadeIn("fast");
			$("#detail").css({"left": $(_this).offset().left + $(_this).width() + 12 + "px", "top": $(_this).offset().top - 480/2 + $(_this).width()/2 + "px"}).fadeIn("fast");					
		}, 800);
	}).mouseout(function() {		
		if(!s) return;
		clearTimeout(s);		
		$("#imgICO").hide();
		$("#detail").hide();
	});

});