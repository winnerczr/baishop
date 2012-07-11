/**
 * 产品
 */
$(document).ready(function(){
	//图片列表操作
	var smallImgScroll = 0;
	var smallImgHeight = $("#imageMenu li:first").outerHeight(true);
	var imageMenuSize = $("#imageMenu li").size();
	$("#imageMenu").scrollTop(0);
	
	//判断图片列表箭头是否高亮
	if($("#imageMenu").scrollTop()<=0){
		$(".smallImgUp span").removeClass("smallImgUp01").addClass("smallImgUp02");
	}else{
		$(".smallImgUp span").removeClass("smallImgUp02").addClass("smallImgUp01");
	}	
	if(Math.round($("#imageMenu").scrollTop()/smallImgHeight)>=imageMenuSize-5){
		$(".smallImgDown span").removeClass("smallImgDown01").addClass("smallImgDown02");
	}else{
		$(".smallImgDown span").removeClass("smallImgDown02").addClass("smallImgDown01");
	}

	//单击图片列表的上箭头	
	$(".smallImgUp").click(function(){
		if($("#imageMenu").scrollTop()<=0){
			//已到达顶部
			$(".smallImgUp span").removeClass("smallImgUp01").addClass("smallImgUp02");
		}else{
			//未到达顶部
			$("#imageMenu").scrollTop(smallImgScroll-=smallImgHeight);
		}
		
		//如果列表数大于5，则底部箭头亮
		if(imageMenuSize>5){
			$(".smallImgDown span").removeClass("smallImgDown02").addClass("smallImgDown01");
		}
	});

	//单击图片列表的下箭头
	$(".smallImgDown").click(function(){
		if(Math.round($("#imageMenu").scrollTop()/smallImgHeight)>=imageMenuSize-5){
			//已到达底部
			$(".smallImgDown span").removeClass("smallImgDown01").addClass("smallImgDown02");
		}else{
			//未到达底部
			$("#imageMenu").scrollTop(smallImgScroll+=smallImgHeight);
		}
		
		//如果列表数大于5，则顶部箭头亮
		if(imageMenuSize>5){
			$(".smallImgUp span").removeClass("smallImgUp02").addClass("smallImgUp01");
		}
	});
	
	//单击图片列表切换图片
	$("#imageMenu li").click(function(){
		$("#imageMenu li").removeAttr("id");
		$(this).attr("id", "onlickImg");
		
		var src = $(this).find("img").attr("src");
		$("#midimg").attr("src",src.substring(0,src.lastIndexOf("/")+1)+"mid"+src.substring(src.lastIndexOf("/")+6));
		$("#bigView img").attr("src",src.substring(0,src.lastIndexOf("/")+1)+"big"+src.substring(src.lastIndexOf("/")+6));
	}).mouseover(function(){ 
		//鼠标经过图片列表时
		$("#imageMenu li img").removeAttr("style");
		$(this).find("img").attr("style","background: none repeat scroll 0 0 #336699;border: 1px solid #336699;");
		
		var src = $(this).find("img").attr("src");
		$("#midimg").attr("src","").attr("src",src.substring(0,src.lastIndexOf("/")+1)+"mid"+src.substring(src.lastIndexOf("/")+6));
	}).mouseout(function(){ 
		//鼠标离开图片列表时
		$("#imageMenu li img").removeAttr("style");
		
		var src = $("#onlickImg").find("img").attr("src");
		$("#midimg").attr("src",src.substring(0,src.lastIndexOf("/")+1)+"mid"+src.substring(src.lastIndexOf("/")+6));
	});
	
	
	//放大查看图片
	$('#vertical').hover(function(){
	  $(this).mousemove(function(event){
		  var _top = event.clientY+$(window).scrollTop()-$("#midimg").offset().top-$('#winSelector').height()/2;
		  var _left = event.clientX+$(window).scrollLeft()-$("#midimg").offset().left-$('#winSelector').width()/2;
		  
		  if(_top<0)
			  _top = 0;
		  if(_top+$('#winSelector').height()>$("#midimg").height())
			  _top = $("#midimg").height()-$('#winSelector').height();
		  
		  if(_left<0)
			  _left = 0;
		  if(_left+$('#winSelector').width()>$("#midimg").width())
			  _left = $("#midimg").width()-$('#winSelector').width();
		  		  
		  $("#winSelector").css({ top: _top, left: _left}).show();
		  $("#bigView img").css({ top: _top*(-2), left: _left*(-2)});
		  $("#bigView").show();
	  });
	},function(){
		$('#winSelector').hide();
		$("#bigView").hide();
	});
	
	
	//选择颜色
	$(".selColor ul li").click(function(){
		$(".selColor ul li").removeAttr("id");
		$(this).attr("id","onlickColor");
		$(".clothingColor h3 strong").text($(this).attr("title"));
	});
	
	//选择尺码
	$(".selSize ul li").click(function(){
		$(".selSize ul li").removeAttr("id");
		$(this).attr("id","onlickSelSize");
		$(".selSizeArea h3 strong").text($(this).text());
	});	
	
	
	//商品描述区的标签页切换
	$(".RsetTabMenu ul li").click(function(){
		$(".RsetTabMenu ul li.hover").removeClass("hover").addClass("down");
		$(this).removeClass("down").addClass("hover");
		
		$(".RsetTabArea>:gt(0)").hide();
		switch($(this).index()){
		case 0:
			$(".RsetTabArea>*").show();
			break;
		case 1:
			$(".RsetTabArea #gmpl").show();
			break;
		case 2:
			$(".RsetTabArea #yntw").show();	
			break;
		case 3:
			$(".RsetTabArea #shfw").show();	
			break;
		case 4:
			$(".RsetTabArea #rhgm").show();	
			break;
		}
	});
	
	
	
});