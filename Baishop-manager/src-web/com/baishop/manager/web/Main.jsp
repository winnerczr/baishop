<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="Meta.jsp" %>
<title>${app_title}</title>
<script type="text/javascript">
  	Ext.ns("Baishop"); 
  	
  	Ext.Loader.setConfig({
  	    enabled: true,
  	    paths: {
  	        'Baishop': 'WEB-RES/script/app' 
  	    }
  	});
  	Ext.require('Baishop.Application');
  	
  	Ext.onReady(function() {
  	    Ext.create('Baishop.Application');
  	    
  	    //加载模块数据
  	    Baishop.treeModules = ${treeModules};
  	    Baishop.leafModules = ${leafModules};
  	  	Baishop.listSystems = ${listSystems};
  	});
</script>
</head>
<body id="ext-body" class="iScroll">
    <!-- 界面加载 -->
    <div id="loading"><span class="title"></span><span class="logo"></span></div>
    
    <!-- 历史菜单 -->
    <form id="history-form" class="x-hide-display">
        <input type="hidden" id="x-history-field" />
        <iframe id="x-history-frame"></iframe>
    </form>


	<!-- 首页 -->
    <div id="home-space" style="display: none;">
    	<h1 class="title">首页</h1>
    </div>

</body>
</html>