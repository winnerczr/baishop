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
       	getCity: '${page_context}/ass/SysCity.jspx?func=getCity',
       	delCity: '${page_context}/ass/SysCity.jspx?func=delCity',
       	saveCity: '${page_context}/ass/SysCity.jspx?func=saveCity'
    };

    
 	//------------------------------------------------------------------------------------//
 	
    
	//城市添加/编辑窗口
	var winCity = Ext.create('widget.window', {
		id: 'winCity',
		title: '城市信息',
		width: 280,
		height: 255,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmCity',
			bodyStyle:'padding:20px 0 0 23px',
			border: false,
			autoScroll: true,
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 50, 
				msgTarget: 'side',
				width: 220
			},
			items: [{
			    xtype: 'hiddenfield',
			    id: 'hddCityId',
			    name: 'city.cityId',
			    value: ''
			},{
			    xtype: 'hiddenfield',
			    id: 'hddCityPid',
			    name: 'city.cityPid',
			    value: ''
			},{
			    xtype:'textfield',
			    id: 'txtCityName',
			    name: 'city.cityName',
			    fieldLabel: '城市',
			    allowBlank: false
			},{
			    xtype:'textfield',
			    id: 'txtCityCode',
			    name: 'city.cityCode',
			    fieldLabel: '编号',
			    allowBlank: false
			},{
				xtype:'textfield',
				id: 'txtCityPinyin',
				name: 'city.cityPinyin',
				fieldLabel: '拼音'
			},{
			    xtype:'textarea',
			    id: 'txtCityRemark',
			    name: 'city.cityRemark',
			    fieldLabel: '备注'
			}]
		})],
		buttons: [{
			id: "btnOK",
		    text:'确定',
		    width: 80,
		    handler: function(){			    		 		
		    	saveCity(Ext.getCmp("winCity"), Ext.getCmp("frmCity"), Ext.getCmp("treeCity"));
		    }
		},{
			id: "btnCancel",
		    text:'取消',
		    width: 80,
		    handler: function(){
		    	Ext.getCmp("winCity").hide();
		    }
		}]
	});
    

 	//------------------------------------------------------------------------------------//
    
    var treeCity = Ext.create('Ext.tree.Panel', {
		id: 'treeCity',
		border: false,
		split:true,
        animate: true,
		autoscroll: true,
		useArrows: true,
		multiSelect: false,
		singleExpand: false,
		rootVisible: true,
		store: Ext.create('Ext.data.TreeStore', {
	  	    idProperty: 'cityId',
			fields: [
				'cityId', 'cityPid', 'cityCode', 'cityName', 'cityPinyin', 'cityRemark'
			],
			root: {
				id: 0,
				text: "行政区划",
				cityId: 0,
				cityName: "行政区划",
				cityCode: "",
				iconCls: 'icon-pkg',
				expanded: true
			},
			autoSync: true,
	        proxy: {
	            type: 'ajax',
	            url: Url.getCity,
				listeners: {
	    			exception: function(proxy, request, operation, options) {
	    				Ext.Msg.alert("提示", "加载数据出错："+proxy.getReader().rawData['msg'] );
	    			}
	    		}
	        }
	    }),
        columns: [{
            xtype: 'treecolumn',
            text: '城市',
            dataIndex: 'cityName',
            width: 300,
          	sortable: false
        },{
            text: '编号',
            dataIndex: 'cityCode',
            width: 150,
          	sortable: false
        },{
            text: '拼音',
            dataIndex: 'cityPinyin',
            width: 150,
          	sortable: false
        },{
            text: '备注',
            dataIndex: 'cityRemark',
            width: 200,
          	sortable: false
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
				//判断是否选择一条记录
				var rows = Ext.getCmp("treeCity").selModel.getSelection();
				if (rows.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录");
					return;
				}				
				
				//显示窗口
				winCity.show(this,function(){
					this.setTitle("添加城市");
					var frmCity = Ext.getCmp("frmCity");
					frmCity.getForm().reset();
					frmCity.items.get("txtCityName").focus();
					frmCity.items.get("hddCityId").setValue(null);
					frmCity.items.get("hddCityPid").setValue(rows[0].get("cityId"));
					frmCity.items.get("txtCityCode").setValue(rows[0].get("cityCode"));		
				});
			}
		},{
			id: 'btnEdit',
			text: '编辑',
			iconCls: 'icon-edit',
			handler: function(btn, event) {
				//判断是否选择一条记录
				var rows = Ext.getCmp("treeCity").selModel.getSelection();
				if (rows.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录");
					return;
				}
	    	
				//显示窗口
				winCity.show(this,function(){
					this.setTitle("编辑城市");
					var frmCity = Ext.getCmp("frmCity");
					frmCity.items.get("txtCityName").focus();
					frmCity.items.get("hddCityId").setValue(rows[0].get("cityId"));
					frmCity.items.get("hddCityPid").setValue(rows[0].get("cityPid"));
					frmCity.items.get("txtCityName").setValue(rows[0].get("cityName"));
					frmCity.items.get("txtCityCode").setValue(rows[0].get("cityCode"));
					frmCity.items.get("txtCityPinyin").setValue(rows[0].get("cityPinyin"));
					frmCity.items.get("txtCityRemark").setValue(rows[0].get("cityRemark"));		
				});
			}
		},{
			text: '删除',
			iconCls: 'icon-del',
			handler: function(btn, event) {
				delCity(Ext.getCmp("treeCity"));
			}
		}]
	});
    
    
	//------------------------------------------------------------------------------------//
 	    
	
	//保存城市
	function saveCity(win, frm, tree){
		// 提交表单
		frm.submit({
		    url: Url.saveCity,
			waitTitle : "提示",
			waitMsg : "正在保存...",
		    success: function(form, action) {
		    	win.hide();
		    	
		    	var node = tree.store.getNodeById(frm.items.get("hddDeptPid").getValue());
		    	tree.store.load({
		    		node: node
		    	});
		    	node.expand(true);
		    },
		    failure: function(form, action) {
		    	Ext.formFailure(form, action);
		    }
		});
	}
		
		
	//删除城市
	function delCity(tree){
    	var rows = tree.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '删除城市将会删除该城市下的所有节点数据，确定要将选择的城市删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var cityId = rows[0].get('cityId');
    					var cityPid = rows[0].get('cityPid');
    					
    					// 发送请求
    					tree.el.mask("正在删除城市...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delCity,
    						params : "cityId=" + cityId,
    						method : "POST",
    						waitMsg : "正在删除...",
    						success : function(response, options) {
    							tree.el.unmask();
    							var json = Ext.JSON.decode(response.responseText);
								if (json.success) {
									tree.store.load({
							    		node: tree.store.getNodeById(cityPid)
							    	});
								}else{
									Ext.Msg.alert("提示", json.msg);
								}
    						},
    						failure : function(response, options) {
    							tree.el.unmask();
    							Ext.ajaxFailure(response, options);
    						}
    					});
    				}
    			}
    		});

    	} else {
    		Ext.Msg.alert("提示", "请选择城市");
    	}
	}
    
    
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
	       	    items:[treeCity]
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