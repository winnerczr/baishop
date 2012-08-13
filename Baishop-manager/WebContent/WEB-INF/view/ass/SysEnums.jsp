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
		getEnums: '${page_context}/ass/SysEnums.jspx?func=getEnums',
		delEnums: '${page_context}/ass/SysEnums.jspx?func=delEnums',
		saveEnums: '${page_context}/ass/SysEnums.jspx?func=saveEnums'
	};
  	    
  	    
	//------------------------------------------------------------------------------------//
  	    
	//枚举添加/编辑窗口
	var winEnums = Ext.create('widget.window', {
		id: 'winEnums',
		title: '枚举信息',
		width: 310,
		height: 300,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmEnums',
			bodyStyle:'padding:20px 0 0 23px',
			border: false,
			autoScroll: true,
			defaults: { 
				listeners: {
					specialkey: function(obj,e){
						 if (e.getKey() == Ext.EventObject.ENTER) {
							Ext.getCmp("btnOK").handler();
						}
					}
				}
			},
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 60, 
				msgTarget: 'side',
				width: 250
			},
			items: [{
			    xtype: 'hiddenfield',
			    id: 'enumsId',
			    name: 'enums.enumsId',
			    value: ''
			},{
			    xtype:'textfield',
			    id: 'txtEnumsType',
			    name: 'enums.enumsType',
			    fieldLabel: '枚举类型',
			    allowBlank: false
			},{
			    xtype:'textfield',
			    id: 'txtEnumsCode',
			    name: 'enums.enumsCode',
			    fieldLabel: '枚举码',
			    allowBlank: false
			},{
			    xtype:'textarea',
			    id: 'txtEnumsText',
			    name: 'enums.enumsText',
			    fieldLabel: '枚举值',
			    allowBlank: false
			},{
			    xtype:'textarea',
			    id: 'txtEnumsRemark',
			    name: 'enums.enumsRemark',
			    fieldLabel: '说明'
			}]
		})],
		buttons: [{
			id: "btnOK",
		    text:'确定',
		    width: 80,
		    handler: function(){			    		 		
		    	saveEnums(Ext.getCmp("winEnums"), Ext.getCmp("frmEnums"), Ext.getCmp("gridEnums"));
		    }
		},{
			id: "btnCancel",
		    text:'取消',
		    width: 80,
		    handler: function(){
		    	Ext.getCmp("winEnums").hide();
		    }
		}]
	});
  	    
  	    
	//------------------------------------------------------------------------------------//
	
    //枚举列表
    var gridEnums = Ext.create('Ext.grid.Panel', {
		id: 'gridEnums',
		border: false,
		disableSelection: false,
		loadMask: true,
		store: Ext.create('Ext.data.Store', {
	        idProperty: 'enumsId',
	        fields: [
				'enumsId', 'enumsType', 'enumsCode', 'enumsText', 'enumsRemark'
	        ],
	        groupField: 'enumsType',
	        proxy: {
	            type: 'jsonp',
	            url: Url.getEnums,
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
        features: [Ext.create('Ext.grid.feature.Grouping',{
            groupHeaderTpl: '枚举类型: {name}'
        })],
		selModel: Ext.create('Ext.selection.CheckboxModel'),
        columns:[{
            text: '枚举类型',
            dataIndex: 'enumsType',
            width: 200,
            hidden: true,
            sortable: true
        },{
            text: '枚举码',
            dataIndex: 'enumsCode',
            width: 200,
            sortable: true
        },{
            text: '枚举值',
            dataIndex: 'enumsText',
            width: 300,
            sortable: true
        },{
            text: '说明',
            dataIndex: 'enumsRemark',
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
		    	winEnums.show(this,function(){
		    		this.setTitle("添加枚举");
		    		var frmEnums = Ext.getCmp("frmEnums");
		    		frmEnums.getForm().reset();
		    		frmEnums.items.get("enumsId").setValue(null);
		    	});
		    }
        },{
        	id: 'btnEdit',
            text: '编辑',
            iconCls: 'icon-edit',
		    handler: function(btn, event) {
	    		//判断是否选择一条记录
	    		var rows = Ext.getCmp("gridEnums").selModel.getSelection();
		    	if (rows.length != 1) {
		    		Ext.Msg.alert("提示", "请选择一条记录");
		    		return;
		    	}
		    	
		    	//显示窗口
		    	winEnums.show(this,function(){
		    		this.setTitle("编辑枚举");
		    		var frmEnums = Ext.getCmp("frmEnums");
		    		frmEnums.items.get("enumsId").setValue(rows[0].get("enumsId"));
		    		frmEnums.items.get("txtEnumsType").setValue(rows[0].get("enumsType"));
		    		frmEnums.items.get("txtEnumsCode").setValue(rows[0].get("enumsCode"));
		    		frmEnums.items.get("txtEnumsText").setValue(rows[0].get("enumsText"));
		    		frmEnums.items.get("txtEnumsRemark").setValue(rows[0].get("enumsRemark"));
		    	});
		    }
        },{
            text: '删除',
            iconCls: 'icon-del',
		    handler: function(btn, event) {
		    	delEnums(Ext.getCmp("gridEnums"));
		    }
        },'->',{
        	id: 'searchKey',
            xtype: 'textfield',
            name: 'searchKey',
            emptyText: '请输入枚举类型、键、值',
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
		    	Ext.getCmp("gridEnums").getStore().loadPage(1);
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
	       	    items:[gridEnums]
	        }]
	    })],
		renderTo: document.body
	});
	    
	    	    
	//------------------------------------------------------------------------------------//
 	    
	
	//保存枚举
	function saveEnums(win, frm, grid){
		// 提交表单
		frm.submit({
		    url: Url.saveEnums,
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
	
	
	//删除枚举
	function delEnums(grid){
    	var rows = grid.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的枚举删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax枚举
    					var ajaxparams = "enumsIds=";
    					for ( var i = 0; i < rows.length; i++) {
    						ajaxparams += rows[i].get('enumsId');
    						if (i < rows.length - 1) 
    							ajaxparams += ",";
    					}
    					
    					// 发送请求
    					grid.el.mask("正在删除枚举...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delEnums,
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
    		Ext.Msg.alert("提示", "请选择枚举");
    	}
	}		
  	 	
  	 	
	//搜索枚举
	Ext.getCmp("btnSearch").handler();	
	
});
</script>
</head>
<body>
    <!-- 界面加载 -->
    <div id="loading"><span class="title"></span><span class="logo"></span></div>
</body>
</html>