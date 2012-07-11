$.fn.extend({     
   AdAdvance:function(){
	var listobj=this;
        var objs =$('dt',this);
	var view =objs.length-1;//parseInt( Math.random()*objs.length);
	objs.each(function(i){
	$(this).mouseover(function(){ $('dd',listobj).hide();$('.dropList-hover',listobj).attr("class",""); $(this).children("p").attr("class","dropList-hover");$(this).next().show()})
	if(i!=view)
	{
		$(this).next().hide();
	}
	else
	{
		$(this).next().show();
		$(this).children("p").attr("class","dropList-hover");
	}
	});
    }
}); 