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
       	getModules: '${page_context}/ass/SysModules.jspx?func=getModules',
       	delModules: '${page_context}/ass/SysModules.jspx?func=delModules',
       	saveModules: '${page_context}/ass/SysModules.jspx?func=saveModules',
       	upModules: '${page_context}/ass/SysModules.jspx?func=upModules',
       	downModules: '${page_context}/ass/SysModules.jspx?func=downModules',
       	getRoles: '${page_context}/ass/SysRoles.jspx?func=getRoles&action=SysModules',
        getRolesByModules: '${page_context}/ass/SysModules.jspx?func=getRolesByModules',
        getUsersByModules: '${page_context}/ass/SysModules.jspx?func=getUsersByModules'
    };

    
 	//------------------------------------------------------------------------------------//
 	
    
	//应用模块添加/编辑窗口
	var winModules = Ext.create('widget.window', {
		id: 'winModules',
		title: '应用模块信息',
		width: 350,
		height: 185,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmModules',
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
				labelWidth: 70, 
				msgTarget: 'side',
				width: 290
			},
			items: [{
			    xtype: 'hiddenfield',
			    id: 'hddModuleId',
			    name: 'module.moduleId',
			    value: ''
			},{
			    xtype: 'hiddenfield',
			    id: 'hddModulePid',
			    name: 'module.modulePid',
			    value: ''
			},{
			    xtype: 'hiddenfield',
			    id: 'hddType',
			    name: 'module.type',
			    value: ''
			},{
			    xtype:'textfield',
			    id: 'txtModuleText',
			    name: 'module.text',
			    fieldLabel: '模块名称',
			    allowBlank: false
			},{
			    xtype:'textfield',
			    id: 'txtUrl',
			    name: 'module.url',
			    fieldLabel: 'URL地址'
			},{
				xtype:'combobox',
				id: 'cbbIconcls',
				name: 'module.iconCls',
				fieldLabel: 'CSS图标',
				store: [
			        ['icon-sub-system','icon-sub-system'],
			        ['icon-module-group','icon-module-group'],
			        ['icon-module-app','icon-module-app'],
			        ['icon-module-function','icon-module-function'],
			        ['icon-module-constant','icon-module-constant']
				]
			},{
				xtype:'combobox',
				id: 'cbbExpanded',
				name: 'module.expanded',
				fieldLabel: '是否展开',
				editable: false,
				store: [[1,'是'],[0,'否']],
				value: 1
			}]
		})],
		buttons: [{
			id: "btnOK",
		    text:'确定',
		    width: 80,
		    handler: function(){			    		 		
		    	saveModules(Ext.getCmp("winModules"), Ext.getCmp("frmModules"), Ext.getCmp("treeModules"));
		    }
		},{
			id: "btnCancel",
		    text:'取消',
		    width: 80,
		    handler: function(){
		    	Ext.getCmp("winModules").hide();
		    }
		}]
	});
 	
 	
	//查看用户窗口
    var winModulesAdmins = Ext.create('widget.window', {
		id: 'winModulesAdmins',
		title: '查看用户',
		width: 300,
		height: 450,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		buttonAlign: 'center',
		userId: null,
		items: [Ext.create('Ext.grid.Panel', {
			id: 'gridModulesAdmins',
			border: false,
			disableSelection: false,
			loadMask: true,
			store: Ext.create('Ext.data.Store', {
		        idProperty: 'userId',
		        fields: [
					'userId', 'username', 'name', 'code'
		        ],
		        proxy: {
		            type: 'jsonp',
		            url: Url.getUsersByModules,
		            reader: {
		                root: 'records'
		            },
		            simpleSortMode: true,
					listeners: {
		    			exception: function(proxy, request, operation, options) {
		    				Ext.Msg.alert("提示", "加载数据出错："+proxy.getReader().rawData['msg'] );
		    			}
		    		}
		        }
		    }),
	        columns:[Ext.create('Ext.grid.RowNumberer'), {
	            text: '用户名',
	            dataIndex: 'username',
	            width: 100,
	            sortable: true
	        },{
	            text: '姓名',
	            dataIndex: 'name',
	            width: 150,
	            sortable: true,
	            renderer: function(value, p, record) {
	            	return "["+ record.get("code") +"] " + value;
				}
	        }]
		})]
	});
 	
 	
	//查看角色窗口
    var winModulesRoles = Ext.create('widget.window', {
		id: 'winModulesRoles',
		title: '查看角色',
		width: 300,
		height: 450,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		buttonAlign: 'center',
		userId: null,
		items: [Ext.create('Ext.tree.Panel', {
			id: 'treeModulesRoles',
	        frame: true,
	        useArrows: true,
	        rootVisible: false,
	        store: Ext.create('Ext.data.TreeStore', {
	            proxy: {
	                type: 'ajax',
	                url: Url.getRoles
	            }
	        }),
			viewConfig : {
				onCheckboxChange : function(e, t) {
				    return false;
				}
			}
	    })]
	});
    

 	//------------------------------------------------------------------------------------//
    
    var treeModules = Ext.create('Ext.tree.Panel', {
		id: 'treeModules',
		border: false,
		split:true,
        animate: true,
		autoscroll: true,
		useArrows: true,
		multiSelect: false,
		singleExpand: false,
		rootVisible: false,
		store: Ext.create('Ext.data.TreeStore', {
	  	    idProperty: 'moduleId',
			fields: [
				'moduleId', 'modulePid', 'text', 'url', 'type', 'iconCls', 'expanded', 'sort'
			],
	        proxy: {
	            type: 'ajax',
	            url: Url.getModules,
				listeners: {
	    			exception: function(proxy, request, operation, options) {
	    				Ext.Msg.alert("提示", "加载数据出错："+proxy.getReader().rawData['msg'] );
	    			}
	    		}
	        }
	    }),
        columns: [{
            xtype: 'treecolumn',
            text: '子系统、模块组、应用模块',
            dataIndex: 'text',
            width: 300,
          	sortable: false
        },{
            text: '模块URL、功能URL、常量值',
            dataIndex: 'url',
            width: 400,
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
			menu: [{
				text: '子系统',
				iconCls: 'icon-sub-system',
				handler: function(btn, event) {					
					//显示窗口
					winModules.show(this,function(){
						this.setTitle("添加子系统");
						var frmModules = Ext.getCmp("frmModules");
						frmModules.getForm().reset();
						frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="子系统:";
						frmModules.items.get("cbbIconcls").setValue("icon-sub-system");
						frmModules.items.get("hddModuleId").setValue(null);
						frmModules.items.get("hddModulePid").setValue(0);
						frmModules.items.get("hddType").setValue("SYSTEM");
						frmModules.items.get("txtUrl").setVisible(false);
						frmModules.items.get("cbbExpanded").setVisible(false);
						winModules.setHeight(155);
					});
				}
			},{
				text: '模块组',
				iconCls: 'icon-module-group',
				handler: function(btn, event) {
					//判断是否选择一条记录
					var rows = Ext.getCmp("treeModules").selModel.getSelection();
					if (rows.length==1 && (rows[0].raw.type=="SYSTEM" || rows[0].raw.type=="GROUP")) {	
						//显示窗口
						winModules.show(this,function(){
							this.setTitle("添加模块组");
							var frmModules = Ext.getCmp("frmModules");
							frmModules.getForm().reset();
							frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="模块组:";
							frmModules.items.get("cbbIconcls").setValue("icon-module-group");
							frmModules.items.get("hddModuleId").setValue(null);
							frmModules.items.get("hddModulePid").setValue(rows[0].get("moduleId"));
							frmModules.items.get("hddType").setValue("GROUP");
							frmModules.items.get("txtUrl").setVisible(false);
							frmModules.items.get("cbbExpanded").setVisible(true);	
							winModules.setHeight(180);				
						});
					}else{
						Ext.Msg.alert("提示", "请选择子系统或模块组记录");
						return;
					}
				}
			},{
				text: '应用模块',
				iconCls: 'icon-module-app',
				handler: function(btn, event) {
					//判断是否选择一条记录
					var rows = Ext.getCmp("treeModules").selModel.getSelection();
					if (rows.length==1 && (rows[0].raw.type=="SYSTEM" || rows[0].raw.type=="GROUP")) {					
						//显示窗口
						winModules.show(this,function(){
							this.setTitle("添加应用模块");
							var frmModules = Ext.getCmp("frmModules");
							frmModules.getForm().reset();
							frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="应用模块:";
							frmModules.items.get("txtUrl").el.dom.childNodes[0].innerHTML ="模块URL:";
							frmModules.items.get("cbbIconcls").setValue("icon-module-app");
							frmModules.items.get("hddModuleId").setValue(null);
							frmModules.items.get("hddModulePid").setValue(rows[0].get("moduleId"));
							frmModules.items.get("hddType").setValue("MODULE");
							frmModules.items.get("txtUrl").setVisible(true);
							frmModules.items.get("cbbExpanded").setVisible(false);	
							winModules.setHeight(180);
						});
					}else{
						Ext.Msg.alert("提示", "请选择子系统或模块组记录");
						return;
					}
				}
			},"-",{
				text: '操作功能',
				iconCls: 'icon-module-function',
				handler: function(btn, event) {
					//判断是否选择一条记录
					var rows = Ext.getCmp("treeModules").selModel.getSelection();
					if (rows.length==1 && rows[0].raw.type=="MODULE") {					
						//显示窗口
						winModules.show(this,function(){
							this.setTitle("添加操作功能");
							var frmModules = Ext.getCmp("frmModules");
							frmModules.getForm().reset();
							frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="功能名:";
							frmModules.items.get("txtUrl").el.dom.childNodes[0].innerHTML ="功能URL:";
							frmModules.items.get("cbbIconcls").setValue("icon-module-function");
							frmModules.items.get("hddModuleId").setValue(null);
							frmModules.items.get("hddModulePid").setValue(rows[0].get("moduleId"));
							frmModules.items.get("hddType").setValue("FUNCTION");
							frmModules.items.get("txtUrl").setVisible(true);
							frmModules.items.get("cbbExpanded").setVisible(false);	
							winModules.setHeight(180);
						});
					}else{
						Ext.Msg.alert("提示", "请选择应用模块记录");
						return;
					}
				}
			},{
				text: '操作常量',
				iconCls: 'icon-module-constant',
				handler: function(btn, event) {
					//判断是否选择一条记录
					var rows = Ext.getCmp("treeModules").selModel.getSelection();
					if (rows.length==1 && rows[0].raw.type=="MODULE") {					
						//显示窗口
						winModules.show(this,function(){
							this.setTitle("添加操作常量");
							var frmModules = Ext.getCmp("frmModules");
							frmModules.getForm().reset();
							frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="常量名:";
							frmModules.items.get("txtUrl").el.dom.childNodes[0].innerHTML ="常量值:";
							frmModules.items.get("cbbIconcls").setValue("icon-module-constant");
							frmModules.items.get("hddModuleId").setValue(null);
							frmModules.items.get("hddModulePid").setValue(rows[0].get("moduleId"));
							frmModules.items.get("hddType").setValue("CONSTANT");
							frmModules.items.get("txtUrl").setVisible(true);
							frmModules.items.get("cbbExpanded").setVisible(false);	
							winModules.setHeight(180);
						});
					}else{
						Ext.Msg.alert("提示", "请选择应用模块记录");
						return;
					}
				}
			}]
		},{
			id: 'btnEdit',
			text: '编辑',
			iconCls: 'icon-edit',
			handler: function(btn, event) {
				//判断是否选择一条记录
				var rows = Ext.getCmp("treeModules").selModel.getSelection();
				if (rows.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录");
					return;
				}
	    	
				//显示窗口
				winModules.show(this,function(){
					var frmModules = Ext.getCmp("frmModules");
					frmModules.items.get("hddModuleId").setValue(rows[0].get("moduleId"));
					frmModules.items.get("hddModulePid").setValue(rows[0].get("modulePid"));
					frmModules.items.get("hddType").setValue(rows[0].get("type"));
					frmModules.items.get("txtModuleText").setValue(rows[0].get("text"));
					frmModules.items.get("txtUrl").setValue(rows[0].get("url"));
					frmModules.items.get("cbbIconcls").setValue(rows[0].get("iconCls"));
					frmModules.items.get("cbbExpanded").setValue(rows[0].raw.nExpanded);
					
					switch(rows[0].raw.type){
					case "SYSTEM":
						this.setTitle("编辑子系统");
						frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="子系统:";
						frmModules.items.get("txtUrl").setVisible(false);
						frmModules.items.get("cbbExpanded").setVisible(false);
						winModules.setHeight(155);						
						break;
					case "GROUP":
						this.setTitle("编辑模块组");
						frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="模块组:";
						frmModules.items.get("txtUrl").setVisible(false);
						frmModules.items.get("cbbExpanded").setVisible(true);
						winModules.setHeight(180);						
						break;
					case "MODULE":
						this.setTitle("编辑应用模块");
						frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="应用模块:";
						frmModules.items.get("txtUrl").el.dom.childNodes[0].innerHTML ="模块URL:";
						frmModules.items.get("txtUrl").setVisible(true);
						frmModules.items.get("cbbExpanded").setVisible(false);	
						winModules.setHeight(180);							
						break;
					case "FUNCTION":
						this.setTitle("编辑操作功能");
						frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="功能名:";
						frmModules.items.get("txtUrl").el.dom.childNodes[0].innerHTML ="功能URL:";
						frmModules.items.get("txtUrl").setVisible(true);
						frmModules.items.get("cbbExpanded").setVisible(false);	
						winModules.setHeight(180);							
						break;
					case "CONSTANT":
						this.setTitle("编辑操作常量");
						frmModules.items.get("txtModuleText").el.dom.childNodes[0].innerHTML ="常量值:";
						frmModules.items.get("txtUrl").el.dom.childNodes[0].innerHTML ="常量值:";
						frmModules.items.get("txtUrl").setVisible(true);
						frmModules.items.get("cbbExpanded").setVisible(false);	
						winModules.setHeight(180);							
						break;	
					}					
				});
			}
		},{
			text: '删除',
			iconCls: 'icon-del',
			handler: function(btn, event) {
				delModules(Ext.getCmp("treeModules"));
			}
		},'-',{
			text: '上移',
			iconCls: 'icon-up',
			handler: function(btn, event) {
				upModules(Ext.getCmp("treeModules"));
			}
		},{
			text: '下移',
			iconCls: 'icon-down',
			handler: function(btn, event) {
				downModules(Ext.getCmp("treeModules"));				
			}
		},'-',{
            text: '查看用户',
            iconCls: 'icon-search',
		    handler: function(btn, event) {
				//判断是否选择一条记录
				var rows = Ext.getCmp("treeModules").selModel.getSelection();
				if (rows.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录");
					return;
				}
		    	
		    	winModulesAdmins.show(this,function(_this, options){
		    		winModulesAdmins.items.get("gridModulesAdmins").getStore().load({
		    			params:{
		    				moduleId: rows[0].get('moduleId')
		    			}
		    		});
		    	});	    			    	
		    }
        },{
            text: '查看角色',
            iconCls: 'icon-search',
		    handler: function(btn, event) {
				//判断是否选择一条记录
				var rows = Ext.getCmp("treeModules").selModel.getSelection();
				if (rows.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录");
					return;
				}
		    	
		    	winModulesRoles.show(this,function(_this, options){
		    		Ext.Ajax.request({
						url : Url.getRolesByModules,
						params : "moduleId=" + rows[0].get('moduleId'),
						method : "POST",
						waitMsg : '正在载入数据...',
						success : function(response, options) {
		                	//勾选角色列表
				    		var roleIds = Ext.decode(response.responseText);
				    		winModulesRoles.items.get("treeModulesRoles").getStore().getRootNode().cascadeBy(function(node){
								if(node.raw){
									node.set('checked', false);
									for(var i=0;i<roleIds.length;i++){
										if(node.raw.roleId==Number(roleIds[i])){
											node.set('checked', true);
											node.parentNode.set('checked', true);
											break;
										}
									}
								}
							});	  
						},
						failure : function(response, options) {
							Ext.ajaxFailure(response, options);
						}
					});
		    	});
		    }
        }]
	});
    
    
	//------------------------------------------------------------------------------------//
 	    
	
	//保存应用模块
	function saveModules(win, frm, tree){
		// 提交表单
		frm.submit({
		    url: Url.saveModules,
			waitTitle : "提示",
			waitMsg : "正在保存...",
		    success: function(form, action) {
		    	win.hide();
		    	tree.store.load();
		    	/* window.setTimeout(function(){
		    	    treeModules.collapseAll();
		    		treeModules.expandAll();
		    	}, 100); */
		    },
		    failure: function(form, action) {
		    	Ext.formFailure(form, action);
		    }
		});
	}
		
		
	//删除应用模块
	function delModules(tree){
    	var rows = tree.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的应用模块删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "moduleId=" + rows[0].get('moduleId');
    					
    					// 发送请求
    					tree.el.mask("正在删除应用模块...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delModules,
    						params : ajaxparams,
    						method : "POST",
    						waitMsg : "正在删除...",
    						success : function(response, options) {
    							tree.el.unmask();
    							var json = Ext.JSON.decode(response.responseText);
								if (json.success) {
							    	tree.store.load();
							    	/* window.setTimeout(function(){
							    	    treeModules.collapseAll();
							    		treeModules.expandAll();
							    	}, 100); */
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
    		Ext.Msg.alert("提示", "请选择应用模块");
    	}
	}
	
	
	//上移应用模块
	function upModules(tree){
    	var rows = tree.selModel.getSelection();
    	if (rows.length > 0) {
    		//找到上一个节点
			var node = tree.store.getNodeById(rows[0].get('moduleId'));
			var prevModuleId = "";
			for(var i=0;i<node.parentNode.childNodes.length;i++){
				if(node.parentNode.childNodes[i]==node){
					if(i-1<0){
						return;
					}else{
						prevModuleId = node.parentNode.childNodes[i-1].data.id;
						break;
					}
				}
			}    					

			// 构建Ajax参数
			var ajaxparams = "moduleId=" + rows[0].get('moduleId') + "&prevModuleId=" + prevModuleId;    					
			
			// 发送请求
			tree.el.mask("正在上移应用模块...", 'x-mask-loading');
			Ext.Ajax.request({
				url : Url.upModules,
				params : ajaxparams,
				method : "POST",
				waitMsg : "正在上移...",
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

    	} else {
    		Ext.Msg.alert("提示", "请选择应用模块");
    	}
	}
	
	//下移应用模块
	function downModules(tree){
    	var rows = tree.selModel.getSelection();
    	if (rows.length > 0) {
    		//找到下一个节点
			var node = tree.store.getNodeById(rows[0].get('moduleId'));
			var nextModuleId = "";
			for(var i=0;i<node.parentNode.childNodes.length;i++){
				if(node.parentNode.childNodes[i]==node){
					if(i+1>=node.parentNode.childNodes.length){
						return;
					} else {    							
						nextModuleId = node.parentNode.childNodes[i+1].data.id;
						break;
					}
				}
			}    					

			// 构建Ajax参数
			var ajaxparams = "moduleId=" + rows[0].get('moduleId') + "&nextModuleId=" + nextModuleId;    					
			
			// 发送请求
			tree.el.mask("正在下移应用模块...", 'x-mask-loading');
			Ext.Ajax.request({
				url : Url.downModules,
				params : ajaxparams,
				method : "POST",
				waitMsg : "正在下移...",
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

    	} else {
    		Ext.Msg.alert("提示", "请选择应用模块");
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
	       	    items:[treeModules]
	        }]
	    })],
		renderTo: document.body
	});

 	
	window.setTimeout(function(){
	    treeModules.collapseAll();
		treeModules.expandAll();
	}, 100);
	
	
});
</script>
</head>
<body>
    <!-- 界面加载 -->
    <div id="loading"><span class="title"></span><span class="logo"></span></div>
</body>
</html>