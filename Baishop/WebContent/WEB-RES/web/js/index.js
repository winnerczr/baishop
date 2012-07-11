/**
 * 首页
 */
$import('/WEB-RES/script/jquery/jquery-extend-AdAdvance.js');
$import('/WEB-RES/script/jquery/jquery-extend-TrunAd2.js');
$import('/WEB-RES/script/jquery/jquery-extend-TrunAd3.js');

$(document).ready(function(){
	$(".lnav-index a").css("background-image","url('/WEB-RES/image/nav_index_L2.gif')");
	
	//广告切换
	$('#play_list').trunAd2();
	$("#shoesPlayList").trunAd3();
	
	
	$(".kind-area").mouseover(function () {
		$(this).attr("id", "kind-area-hover");
	}).mouseout(function () {
		$(this).attr("id", "");
	});

	
	var changeShoesTab = function(index){
        for(var i=1; i<=5 ; i++){
            if(i == index){
                $('#shoesTab' + i).show();
				$('#shoesTab' + i + 'Header').addClass('shoescheced');
    		} else {
                $('#shoesTab' + i).hide();
				$('#shoesTab' + i + 'Header').removeClass('shoescheced');
    		}
        }
    };	
    $("#shoesTab1Header a").mouseover(function(){changeShoesTab(1);});
	$("#shoesTab2Header a").mouseover(function(){changeShoesTab(2);});
    $("#shoesTab3Header a").mouseover(function(){changeShoesTab(3);});
    $("#shoesTab4Header a").mouseover(function(){changeShoesTab(4);});
	$("#shoesTab5Header a").mouseover(function(){changeShoesTab(5);});

});