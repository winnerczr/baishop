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
		getParams: '${page_context}/ass/SysParams.jspx?func=getParams',
		delParams: '${page_context}/ass/SysParams.jspx?func=delParams',
		saveParams: '${page_context}/ass/SysParams.jspx?func=saveParams'
	};
  	    
  	    
	//------------------------------------------------------------------------------------//
  	    
	//参数添加/编辑窗口
	var winParams = Ext.create('widget.window', {
		id: 'winParams',
		title: '参数信息',
		width: 300,
		height: 270,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmParams',
			bodyStyle:'padding:20px 0 0 23px',
			border: false,
			autoScroll: true,
			fieldDefaults: {
			labelAlign: 'left',
				labelWidth: 50, 
				msgTarget: 'side',
				width: 240
			},
			items: [{
			    xtype: 'hiddenfield',
			    id: 'paramsId',
			    name: 'params.paramsId',
			    value: ''
			},{
			    xtype:'textfield',
			    id: 'txtParamsName',
			    name: 'params.paramsName',
			    fieldLabel: '参数名',
			    allowBlank: false
			},{
			    xtype:'textarea',
			    id: 'txtParamsValue',
			    name: 'params.paramsValue',
			    fieldLabel: '参数值',
			    allowBlank: false
			},{
			    xtype:'textarea',
			    id: 'txtParamsRemark',
			    name: 'params.paramsRemark',
			    fieldLabel: '说明'
			}]
		})],
		buttons: [{
			id: "btnOK",
		    text:'确定',
		    width: 80,
		    handler: function(){			    		 		
		    	saveParams(Ext.getCmp("winParams"), Ext.getCmp("frmParams"), Ext.getCmp("gridParams"));
		    }
		},{
			id: "btnCancel",
		    text:'取消',
		    width: 80,
		    handler: function(){
		    	Ext.getCmp("winParams").hide();
		    }
		}]
	});
  	    
  	    
	//------------------------------------------------------------------------------------//
	
    //参数列表
    var gridParams = Ext.create('Ext.grid.Panel', {
		id: 'gridParams',
		border: false,
		disableSelection: false,
		loadMask: true,
		store: Ext.create('Ext.data.Store', {
	        idProperty: 'paramsId',
	        fields: [
				'paramsId', 'paramsName', 'paramsValue', 'paramsRemark'
	        ],
	        proxy: {
	            type: 'jsonp',
	            url: Url.getParams,
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
            text: '参数名',
            dataIndex: 'paramsName',
            width: 200,
            sortable: true
        },{
            text: '参数值',
            dataIndex: 'paramsValue',
            width: 300,
            sortable: true
        },{
            text: '说明',
            dataIndex: 'paramsRemark',
            width: 300,
            sortable: true
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
		    	winParams.show(this,function(){
		    		this.setTitle("添加参数");
		    		var frmParams = Ext.getCmp("frmParams");
		    		frmParams.getForm().reset();
		    		frmParams.items.get("paramsId").setValue(null);
		    	});
		    }
        },{
        	id: 'btnEdit',
            text: '编辑',
            iconCls: 'icon-edit',
		    handler: function(btn, event) {
	    		//判断是否选择一条记录
	    		var rows = Ext.getCmp("gridParams").selModel.getSelection();
		    	if (rows.length != 1) {
		    		Ext.Msg.alert("提示", "请选择一条记录");
		    		return;
		    	}
		    	
		    	//显示窗口
		    	winParams.show(this,function(){
		    		this.setTitle("编辑参数");
		    		var frmParams = Ext.getCmp("frmParams");
		    		frmParams.items.get("paramsId").setValue(rows[0].get("paramsId"));
		    		frmParams.items.get("txtParamsName").setValue(rows[0].get("paramsName"));
		    		frmParams.items.get("txtParamsValue").setValue(rows[0].get("paramsValue"));
		    		frmParams.items.get("txtParamsRemark").setValue(rows[0].get("paramsRemark"));
		    	});
		    }
        },{
            text: '删除',
            iconCls: 'icon-del',
		    handler: function(btn, event) {
		    	delParams(Ext.getCmp("gridParams"));
		    }
        },'->',{
        	id: 'searchKey',
            xtype: 'textfield',
            name: 'searchKey',
            emptyText: '请输入参数名或参数值',
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
		    	Ext.getCmp("gridParams").getStore().loadPage(1);
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
	       	    items:[gridParams]
	        }]
	    })],
		renderTo: document.body
	});
	    
	    	    
	//------------------------------------------------------------------------------------//
 	    
	
	//保存参数
	function saveParams(win, frm, grid){
		// 提交表单
		frm.submit({
		    url: Url.saveParams,
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
	
	
	//删除参数
	function delParams(grid){
    	var rows = grid.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的参数删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "paramsIds=";
    					for ( var i = 0; i < rows.length; i++) {
    						ajaxparams += rows[i].get('paramsId');
    						if (i < rows.length - 1) 
    							ajaxparams += ",";
    					}
    					
    					// 发送请求
    					grid.el.mask("正在删除参数...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delParams,
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
    		Ext.Msg.alert("提示", "请选择参数");
    	}
	}		
  	 	
  	 	
	//搜索参数
	Ext.getCmp("btnSearch").handler();	
	
});
</script>
</head>
<body>
    <!-- 界面加载 -->
    <div id="loading"><span class="title"></span><span class="logo"></span></div>
</body>
</html>