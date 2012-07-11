/**
 * header script
 */
$(document).ready(function(){
	//动态菜单
	$("#floor_nav ul li a").hover(
		function(){
			$("#"+$(this).attr("name")).fadeIn("fast");
		},
		function(){
			$("#"+$(this).attr("name")).hide();
		}
	);
	$("#sub_floor_menus>div.sub_floor").hover(
		function(){
			$(this).show();
		},
		function(){
			$(this).hide();
		}
	);
});