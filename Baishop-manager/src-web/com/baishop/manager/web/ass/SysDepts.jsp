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
       	getDepts: '${page_context}/ass/SysDepts.jspx?func=getDepts',
       	delDepts: '${page_context}/ass/SysDepts.jspx?func=delDepts',
       	saveDepts: '${page_context}/ass/SysDepts.jspx?func=saveDepts'
    };

    
 	//------------------------------------------------------------------------------------//
 	
    
	//部门添加/编辑窗口
	var winDepts = Ext.create('widget.window', {
		id: 'winDepts',
		title: '部门信息',
		width: 280,
		height: 255,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmDepts',
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
				labelWidth: 50, 
				msgTarget: 'side',
				width: 220
			},
			items: [{
			    xtype: 'hiddenfield',
			    id: 'hddDeptId',
			    name: 'dept.deptId',
			    value: ''
			},{
			    xtype: 'hiddenfield',
			    id: 'hddDeptPid',
			    name: 'dept.deptPid',
			    value: ''
			},{
			    xtype:'textfield',
			    id: 'txtDeptName',
			    name: 'dept.deptName',
			    fieldLabel: '部门',
			    allowBlank: false
			},{
			    xtype:'textfield',
			    id: 'txtDeptCode',
			    name: 'dept.deptCode',
			    fieldLabel: '编码',
			    allowBlank: false
			},{
				xtype:'textfield',
				id: 'txtDeptPinyin',
				name: 'dept.deptPinyin',
				fieldLabel: '拼音'
			},{
			    xtype:'textarea',
			    id: 'txtDeptRemark',
			    name: 'dept.deptRemark',
			    fieldLabel: '备注'
			}]
		})],
		buttons: [{
			id: "btnOK",
		    text:'确定',
		    width: 80,
		    handler: function(){			    		 		
		    	saveDepts(Ext.getCmp("winDepts"), Ext.getCmp("frmDepts"), Ext.getCmp("treeDepts"));
		    }
		},{
			id: "btnCancel",
		    text:'取消',
		    width: 80,
		    handler: function(){
		    	Ext.getCmp("winDepts").hide();
		    }
		}]
	});
    

 	//------------------------------------------------------------------------------------//
    
    var treeDepts = Ext.create('Ext.tree.Panel', {
		id: 'treeDepts',
		border: false,
		split:true,
        animate: true,
		autoscroll: true,
		useArrows: true,
		multiSelect: false,
		singleExpand: false,
		rootVisible: true,
		store: Ext.create('Ext.data.TreeStore', {
	  	    idProperty: 'deptId',
			fields: [
				'deptId', 'deptPid', 'deptCode', 'deptName', 'deptPinyin', 'deptRemark'
			],
			root: {
				id: 0,
				text: "组织架构",
				deptId: 0,
				deptName: "组织架构",
				deptCode: "",
				iconCls: 'icon-pkg',
				expanded: true
			},
			autoSync: true,
	        proxy: {
	            type: 'ajax',
	            url: Url.getDepts,
				listeners: {
	    			exception: function(proxy, request, operation, options) {
	    				Ext.Msg.alert("提示", "加载数据出错："+proxy.getReader().rawData['msg'] );
	    			}
	    		}
	        }
	    }),
        columns: [{
            xtype: 'treecolumn',
            text: '部门',
            dataIndex: 'deptName',
            width: 300,
          	sortable: false
        },{
            text: '编码',
            dataIndex: 'deptCode',
            width: 150,
          	sortable: false
        },{
            text: '拼音',
            dataIndex: 'deptPinyin',
            width: 150,
          	sortable: false
        },{
            text: '备注',
            dataIndex: 'deptRemark',
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
				var rows = Ext.getCmp("treeDepts").selModel.getSelection();
				if (rows.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录");
					return;
				}				
				
				//显示窗口
				winDepts.show(this,function(){
					this.setTitle("添加部门");
					var frmDepts = Ext.getCmp("frmDepts");
					frmDepts.getForm().reset();
					frmDepts.items.get("txtDeptName").focus();
					frmDepts.items.get("hddDeptId").setValue(null);
					frmDepts.items.get("hddDeptPid").setValue(rows[0].get("deptId"));	
					frmDepts.items.get("txtDeptCode").setValue(rows[0].get("deptCode"));	
				});
			}
		},{
			id: 'btnEdit',
			text: '编辑',
			iconCls: 'icon-edit',
			handler: function(btn, event) {
				//判断是否选择一条记录
				var rows = Ext.getCmp("treeDepts").selModel.getSelection();
				if (rows.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录");
					return;
				}
	    	
				//显示窗口
				winDepts.show(this,function(){
					this.setTitle("编辑部门");
					var frmDepts = Ext.getCmp("frmDepts");
					frmDepts.items.get("txtDeptName").focus();
					frmDepts.items.get("hddDeptId").setValue(rows[0].get("deptId"));
					frmDepts.items.get("hddDeptPid").setValue(rows[0].get("deptPid"));
					frmDepts.items.get("txtDeptName").setValue(rows[0].get("deptName"));
					frmDepts.items.get("txtDeptCode").setValue(rows[0].get("deptCode"));
					frmDepts.items.get("txtDeptPinyin").setValue(rows[0].get("deptPinyin"));
					frmDepts.items.get("txtDeptRemark").setValue(rows[0].get("deptRemark"));		
				});
			}
		},{
			text: '删除',
			iconCls: 'icon-del',
			handler: function(btn, event) {
				delDepts(Ext.getCmp("treeDepts"));
			}
		}]
	});
    
    
	//------------------------------------------------------------------------------------//
 	    
	
	//保存部门
	function saveDepts(win, frm, tree){
		// 提交表单
		frm.submit({
		    url: Url.saveDepts,
			waitTitle : "提示",
			waitMsg : "正在保存...",
		    success: function(form, action) {
		    	win.hide();
		    	tree.store.load();
		    },
		    failure: function(form, action) {
		    	Ext.formFailure(form, action);
		    }
		});
	}
		
		
	//删除部门
	function delDepts(tree){
    	var rows = tree.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '删除部门将会删除该部门下的所有节点数据，确定要将选择的部门删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var deptId = rows[0].get('deptId');
    					
    					// 发送请求
    					tree.el.mask("正在删除部门...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delDepts,
    						params : "deptId=" + deptId,
    						method : "POST",
    						waitMsg : "正在删除...",
    						success : function(response, options) {
    							tree.el.unmask();
    							var json = Ext.JSON.decode(response.responseText);
								if (json.success) {
							    	tree.store.load();
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
    		Ext.Msg.alert("提示", "请选择部门");
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
	       	    items:[treeDepts]
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