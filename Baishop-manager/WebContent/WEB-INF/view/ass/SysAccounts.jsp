<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="../Meta.jsp" %>
<title>${module_title}</title>
<script type="text/javascript">
Ext.onReady(function() {
    Ext.tip.QuickTipManager.init();
    
    //业务请求的URL
    Url = {
    	
    };  	    
    
    
 	//------------------------------------------------------------------------------------//

 	
    //界面布局
	Ext.create('Ext.Viewport', {
		id: 'viewport',
	    layout: 'border',
	    title: '${module_title}',
	    items: [{
	         region: 'north',
	         border: false,
	         html: '<h1 class="title ${module_icon}-big">${module_title}</h1>',
	         height: 45
		},
		Ext.createWidget('tabpanel', {
	      	region: 'center',
	        activeTab: 0,
	        plain: true,
	        defaults :{
	            autoScroll: true
	        },
	        items: [{
	            title: '${module_title}',
	       	    layout: 'fit',
	       	    showed: true
	        }]
	    })],
		renderTo: document.body
	});
	
});
</script>
</head>
<body>
    <!-- 界面加载 -->
    <div id="loading"><span class="title"></span><span class="logo"></span></div>
</body>
</html>