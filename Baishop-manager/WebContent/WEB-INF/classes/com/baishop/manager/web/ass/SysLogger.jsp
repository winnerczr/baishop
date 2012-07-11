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
    	getSyslog: '${page_context}/ass/SysLogger.jspx?func=getSyslog',
    	getSyslogFilter: '${page_context}/ass/SysLogger.jspx?func=getSyslogFilter',
    	saveSyslogFilter: '${page_context}/ass/SysLogger.jspx?func=saveSyslogFilter'
    };

	
	//------------------------------------------------------------------------------------//
  	    
	//日志过滤器窗口
	var winSetFilter = Ext.create('widget.window', {
		id: 'winSetFilter',
		title: '日志过滤器',
		width: 300,
		height: 350,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		autoScroll: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmSetFilter',
			layout: 'fit',
			border: false,
			autoScroll: false,
			items: [{
			    xtype:'textarea',
			    id: 'txtFilter',
			    name: 'filter'
			}]
		})],
		buttons: [{
			id: "btnOK",
		    text:'确定',
		    width: 80,
		    handler: function(){
		    	// 提交表单
				Ext.getCmp("frmSetFilter").submit({
				    url: Url.saveSyslogFilter,
					waitTitle : "提示",
					waitMsg : "正在保存...",
				    success: function(form, action) {
				    	Ext.getCmp("winSetFilter").hide();
				    },
				    failure: function(form, action) {
				    	Ext.formFailure(form, action);
				    }
				});
		    }
		},{
			id: "btnCancel",
		    text:'取消',
		    width: 80,
		    handler: function(){
		    	Ext.getCmp("winSetFilter").hide();
		    }
		}]
	});
  	    
	//------------------------------------------------------------------------------------//
	
    var storeSyslog = Ext.create('Ext.data.Store', {
        pageSize: 50,
        remoteSort: true,
		idProperty: 'id',
		fields: [
			'id', 'user', 'source', 'type', 'method', 'args', 'description', 'date'
		],
        proxy: {
            type: 'jsonp',
            url: Url.getSyslog,
            reader: {
                root: 'records',
                totalProperty: 'count'
            },
            simpleSortMode: true,
			listeners: {
    			exception: function(proxy, request, operation, options) {
    				Ext.Msg.alert("提示", "加载数据出错："+proxy.getReader().rawData['msg'] );
    			}
    		}
        },
        sorters: [{
            property: 'date',
            direction: 'desc'
        }],
        listeners: {
        	beforeload: function(store, operation, options) {
        		// 设置查询参数
                Ext.apply(store.proxy.extraParams, { 
                	searchKey: Ext.getCmp("searchKey").getValue(),
                	source: Ext.getCmp("cbbSource").getValue(),
                	startDate: Ext.getCmp("startDate").getValue(),
                	endDate: Ext.getCmp("endDate").getValue()
                });
			}
		}
    });

	//接口权限列表
	var gridSyslog = Ext.create('Ext.grid.Panel', {
		id: 'gridSyslog',
		border: false,
		disableSelection: false,
		loadMask: true,
		store: storeSyslog,
		columns:[Ext.create('Ext.grid.RowNumberer'), {
			text: '用户名',
			dataIndex: 'user',
			width: 150,
			sortable: true
		},{
			text: '调用来源',
			dataIndex: 'source',
			width: 90,
			sortable: true,
            renderer: function(value, p, record) {
            	switch(Number(value)){
            	case 0:
            		return "手动插入";
            	case 1:
            		return "本地调用";
            	case 2:
            		return "远程调用";
            	}
			}
		},{
			text: '执行时间',
			dataIndex: 'date',
			width: 150,
			sortable: true
		},{
			text: '接口类型名',
			dataIndex: 'type',
			width: 350,
			sortable: true
		},{
			text: '方法签名',
			dataIndex: 'method',
			width: 300,
			sortable: true
		},{
			text: '参数列表',
			dataIndex: 'args',
			width: 800,
			sortable: true
		},{
			text: '日志描述',
			dataIndex: 'description',
			width: 200,
			sortable: true
		}],
		tbar: [{
        	id: 'searchKey',
            xtype: 'textfield',
            labelWidth: 60,
            width: 200,
            fieldLabel: '查询条件',
            emptyText: '用户、接口、方法',
            listeners : {
            	specialkey: function(obj,e){
					if(e.getCharCode()==e.ENTER)
						Ext.getCmp("btnSearch").handler();
				}
			}
        },{
        	xtype: 'label',
			width: 10
		},{
            xtype:'combobox',
            id: 'cbbSource',
            width: 160,
            labelWidth: 60,
	        editable: false,
            fieldLabel: '调用来源',
            emptyText: "全部",
            store:  [['','全部'],['0','手动插入'],['1','本地调用'],['2','远程调用']]
        },{
        	xtype: 'label',
			width: 70,
			text: '　时间范围:'
		},{
            xtype:'datefield',
            id: 'startDate',
            width: 95,
            format: 'Y-m-d',
            maxValue: new Date(),
            value: Ext.Date.format(new Date(), 'Y-m-01')
        },{
        	xtype: 'label',
			width: 5,
			text: '-'
		},{
            xtype:'datefield',
            id: 'endDate',
            width: 95,
            format: 'Y-m-d',
            maxValue: new Date(),
            value: Ext.Date.format(new Date(), 'Y-m-d')
        },{
        	xtype: 'label',
			width: 10
		},{
        	id: 'btnSearch',
            text: '搜索',
            iconCls: 'icon-search',
		    handler: function(btn, event) {
		    	Ext.getCmp("gridSyslog").getStore().loadPage(1);
		    }
        },{
        	id: 'btnSetFilter',
            text: '设置过滤器',
            iconCls: 'icon-set',
		    handler: function(btn, event) {
		    	winSetFilter.show(this,function(){
		    		Ext.getCmp("frmSetFilter").load( {
		                url : Url.getSyslogFilter,
		                waitMsg : '正在载入数据...',
		                success : function(form,action) {
		                	Ext.getCmp("frmSetFilter").items.get("txtFilter").setValue(action.result.data.replace(/\t/g,"\n").trim());
		                },
		                failure : function(form,action) {
		                	Ext.formFailure(form,action);
		                }
		            });
		    	});
		    }
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: storeSyslog,
            displayInfo: true,
            displayMsg: '显示日志 {0} - {1} 共 {2}',
            emptyMsg: "没有可显示的日志"
        })
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
				items:[gridSyslog]
			}]
		})],
		renderTo: document.body
	});
  	 	
  	 	
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