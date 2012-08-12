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
    	getRemoteAuths: '${page_context}/ass/SysRemote.jspx?func=getRemoteAuths',
    	delRemoteAuths: '${page_context}/ass/SysRemote.jspx?func=delRemoteAuths',
    	saveRemoteAuths: '${page_context}/ass/SysRemote.jspx?func=saveRemoteAuths'
    };
  	    
  	    
	//------------------------------------------------------------------------------------//
  	    
	//接口权限添加/编辑窗口
    var winRemoteAuths = Ext.create('widget.window', {
    	id: 'winRemoteAuths',
		title: '接口权限信息',
		width: 280,
		height: 255,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmRemoteAuths',
			bodyStyle:'padding:20px 0 0 23px',
			border: false,
			autoScroll: true,
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 70, 
				msgTarget: 'side',
				width: 220
			},
			items: [{
			    xtype: 'hiddenfield',
			    id: 'id',
			    name: 'remoteAuths.id',
			    value: ''
			},{
				xtype:'textfield',
				id: 'txtUser',
				name: 'remoteAuths.user',
				fieldLabel: '用户名',
				allowBlank: false
			},{
				xtype:'textfield',
				id: 'txtPassword',
				name: 'remoteAuths.password',
				fieldLabel: '密码'
			},{
				xtype:'textfield',
				id: 'txtProvide',
				name: 'remoteAuths.provide',
				fieldLabel: '用途'
			},{
				xtype:'textarea',
				id: 'txtAuths',
				name: 'remoteAuths.auths',
				fieldLabel: '权限列表'
			}]
		})],
		buttons: [{
			id: "btnOK",
			text:'确定',
			width: 80,
			handler: function(){			    		 		
				saveRemoteAuths(Ext.getCmp("winRemoteAuths"), Ext.getCmp("frmRemoteAuths"), Ext.getCmp("gridRemoteAuths"));
			}
		},{
			id: "btnCancel",
			text:'取消',
			width: 80,
			handler: function(){
				Ext.getCmp("winRemoteAuths").hide();
			}
		}]
	});
	
  	    
	//------------------------------------------------------------------------------------//

	//接口权限列表
	var gridRemoteAuths = Ext.create('Ext.grid.Panel', {
		id: 'gridRemoteAuths',
		border: false,
		disableSelection: false,
		loadMask: true,
		store: Ext.create('Ext.data.Store', {
			idProperty: 'id',
			fields: [
				'id', 'user', 'password', 'provide', 'auths'
			],
			proxy: {
				type: 'jsonp',
				url: Url.getRemoteAuths,
				reader: {
				    root: 'records'
				},
				simpleSortMode: true,
				listeners: {
	    			exception: function(proxy, request, operation, options) {
	    				Ext.Msg.alert("提示", "加载数据出错："+proxy.getReader().rawData['msg'] );
	    			}
	    		}
			},
			listeners: {
				beforeload: function(store, operation, options) {
					// 设置查询参数
					Ext.apply(store.proxy.extraParams, { 
						searchKey: Ext.getCmp("searchKey").getValue()
					});
				}
			}
		}),
		selModel: Ext.create('Ext.selection.CheckboxModel'),
		columns:[{
			text: '用户名',
			dataIndex: 'user',
			width: 200,
			sortable: false
		},{
			text: '用途',
			dataIndex: 'provide',
			width: 200,
			sortable: false
		},{
			text: '权限列表',
			dataIndex: 'auths',
			width: 450,
			sortable: false,
            renderer: function(value, p, record) {
            	return Ext.encode(value);
			}			
		}],
        listeners : {
        	itemdblclick : function(view, record, item, index, e, options){
        		Ext.getCmp("btnEdit").handler();	
        	}
        },
		tbar: [{
			text: '添加',
			iconCls: 'icon-add',
			handler: function(btn, event) {
				//显示窗口
				winRemoteAuths.show(this,function(){
					this.setTitle("添加接口权限");
					var frmRemoteAuths = Ext.getCmp("frmRemoteAuths");
					frmRemoteAuths.getForm().reset();
					frmRemoteAuths.items.get("id").setValue(null);
				});
			}
		},{
			id: 'btnEdit',
			text: '编辑',
			iconCls: 'icon-edit',
			handler: function(btn, event) {
				//判断是否选择一条记录
				var rows = Ext.getCmp("gridRemoteAuths").selModel.getSelection();
				if (rows.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录");
					return;
				}
				
				//显示窗口
				winRemoteAuths.show(this,function(){
					this.setTitle("编辑接口权限");
					var frmRemoteAuths = Ext.getCmp("frmRemoteAuths");
					frmRemoteAuths.items.get("id").setValue(rows[0].get("id"));
					frmRemoteAuths.items.get("txtUser").setValue(rows[0].get("user"));
					frmRemoteAuths.items.get("txtPassword").setValue(rows[0].get("password"));
					frmRemoteAuths.items.get("txtProvide").setValue(rows[0].get("provide"));
					frmRemoteAuths.items.get("txtAuths").setValue(rows[0].get("auths"));
				});
			}
		},{
			text: '删除',
			iconCls: 'icon-del',
			handler: function(btn, event) {
				delRemoteAuths(Ext.getCmp("gridRemoteAuths"));
			}
        },'->',{
        	id: 'searchKey',
            xtype: 'textfield',
            name: 'searchKey',
            emptyText: '请输入用户名或用途',
            listeners : {
            	specialkey: function(obj,e){
					if(e.getCharCode()==e.ENTER)
						Ext.getCmp("btnSearch").handler();
				}
			}
        },{
        	id: 'btnSearch',
            text: '搜索',
            iconCls: 'icon-search',
		    handler: function(btn, event) {
		    	Ext.getCmp("gridRemoteAuths").getStore().loadPage(1);
		    }
        }]
    });
		
  	    
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
				showed: true,
				items:[gridRemoteAuths]
			}]
		})],
		renderTo: document.body
	});
	    
	    	    
	//------------------------------------------------------------------------------------//
 	    
	
	//保存接口权限
	function saveRemoteAuths(win, frm, grid){
		// 提交表单
		frm.submit({
		    url: Url.saveRemoteAuths,
			waitTitle : "提示",
			waitMsg : "正在保存...",
		    success: function(form, action) {
		    	grid.store.loadPage('limit');
		    	win.hide();
		    },
		    failure: function(form, action) {
		    	Ext.formFailure(form, action);
		    }
		});
	}
		
		
	//删除接口权限
	function delRemoteAuths(grid){
    	var rows = grid.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的接口权限删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "ids=";
    					for ( var i = 0; i < rows.length; i++) {
    						ajaxparams += rows[i].get('id');
    						if (i < rows.length - 1) 
    							ajaxparams += ",";
    					}
    					
    					// 发送请求
    					grid.el.mask("正在删除接口权限...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delRemoteAuths,
    						params : ajaxparams,
    						method : "POST",
    						waitMsg : "正在删除...",
    						success : function(response, options) {
    							grid.el.unmask();
    							var json = Ext.JSON.decode(response.responseText);
								if (json.success) {
									grid.store.load();
								}else{
									Ext.Msg.alert("提示", json.msg);
								}
    						},
    						failure : function(response, options) {
    							grid.el.unmask();
    							Ext.ajaxFailure(response, options);
    						}
    					});
    				}
    			}
    		});

    	} else {
    		Ext.Msg.alert("提示", "请选择接口权限");
    	}
	}
  	 	
  	 	
	//搜索接口权限
	Ext.getCmp("btnSearch").handler();	
  		
});
</script>
</head>
<body>
    <!-- 界面加载 -->
    <div id="loading"><span class="title"></span><span class="logo"></span></div>
</body>
</html>