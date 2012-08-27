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
		getAdmins: '${page_context}/ass/SysAdmins.jspx?func=getAdmins',
		delAdmins: '${page_context}/ass/SysAdmins.jspx?func=delAdmins',
		saveAdmins: '${page_context}/ass/SysAdmins.jspx?func=saveAdmins',
    	getAdminsRoles: '${page_context}/ass/SysRoles.jspx?func=getRoles&action=SysAdmins',
    	getAdminModules: '${page_context}/ass/SysModules.jspx?func=getModules&action=SysAdmins'
	};
  	
	//部门tree与combobox的数据
	var treeDepts = Ext.decode('${treeDepts}');
  	    
	//------------------------------------------------------------------------------------//
  	    
	//用户添加/编辑窗口
	var winAdmins = Ext.create('widget.window', {
		id: 'winAdmins',
		title: '用户信息',
		width: 325,
		height: 477,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmAdmins',
			bodyStyle:'padding:5px',
			border: false,
			autoScroll: false,
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 70, 
				msgTarget: 'side',
				width: 260
			},
			items: [{
				xtype: 'tabpanel',
				id: 'tabAdmins',
		        activeTab: 0, 
		        plain: true,
		        items: [{
		        	id: 'tabAdminsNormal',
		            title: '常规',
		       	    layout: 'fit',
		       	    showed: true,
					bodyStyle:'padding:20px',
					defaults: { 
						listeners: {
							specialkey: function(obj,e){
								 if (e.getKey() == Ext.EventObject.ENTER) {
									Ext.getCmp("btnOK").handler();
								}
							}
						}
					},
		       	    items:[{
					    xtype: 'hiddenfield',
					    id: 'userId',
					    name: 'user.userId',
					    value: ''
					},{
					    xtype:'textfield',
					    id: 'txtUsername',
					    name: 'user.username',
					    fieldLabel: '用户名',
					    allowBlank: false
					},{
					    xtype:'textfield',
					    id: 'txtPassword',
					    name: 'user.password',
					    fieldLabel: '密码',
					    inputType:"password",
					    allowBlank: false
					},{
					    xtype:'textfield',
					    vtype:'repassword',
					    id: 'txtRePassword',
					    name: 'user.rePassword',
					    fieldLabel: '确认密码',
					    inputType:"password",
					    initialPassField:'txtPassword',
					    allowBlank: false
					},{
					    xtype:'textfield',
					    id: 'txtCode',
					    name: 'user.code',
					    fieldLabel: '工号',
					    allowBlank: false
					},{
					    xtype:'textfield',
					    id: 'txtName',
					    name: 'user.name',
					    fieldLabel: '姓名',
					    allowBlank: false
					},{
					    xtype:'combobox',
					    id: 'cbbSex',
					    name: 'user.sex',
					    fieldLabel: '性别',
						editable: false,
						store: [['男','男'],['女','女']]
					},{
					    xtype:'combobox',
					    id: 'cbbDept',
					    name: 'user.depts',
					    fieldLabel: '部门',
				        editable: false,
				        multiSelect: true,
				        tpl: "<tpl for='treeDept'><div id='treeDept' style='height:200px;'></div></tpl>",
		               	store: treeDepts.cbbDept,
						tree: Ext.create('Ext.tree.Panel', {
							width: '100%',
							border:false, 
						    rootVisible: false, 
							store: Ext.create('Ext.data.TreeStore', {
							    root: treeDepts
							})
						}),
				        listeners : {
							expand: function(field, options){
								try{
									field.tree.render('treeDept');
									Ext.get("treeDept").parent().parent().addCls("x-boundlist");
								
								}catch(err){									
								}
							},
							collapse: function(field, options){
								try{
									//获取选中的节点
									var checkedNodes = field.tree.getChecked();									
									var deptIds = [];
									var deptNames = [];
									for(var i=0,j=0; i<checkedNodes.length; i++){
										if(checkedNodes[i].raw){
											deptIds[j] = checkedNodes[i].raw.id;
											deptNames[j] = checkedNodes[i].raw.text;
											j++;
										}
									}
									
									//设置值
									field.setValue(deptIds);
								}catch(err){									
								}
							}
						}
		            },{
					    xtype:'combobox',
					    id: 'cbbPosition',
					    name: 'user.position',
					    fieldLabel: '职务',
						store: [['总裁','总裁'],['总监','总监'],['经理','经理'],['职员','职员']]
					},{
					    xtype:'textfield',
					    vtype:'mobile',
					    id: 'txtMobile',
					    name: 'user.mobile',
					    fieldLabel: '手机'
					},{
					    xtype:'textfield',
					    vtype: 'email',
					    id: 'txtEmail',
					    name: 'user.email',
					    fieldLabel: '邮箱'
					},{
						xtype: 'radiogroup',
						id: 'radIsAllowLogin',
						fieldLabel: '允许登录',
						items: [
						    {boxLabel: '允许', name: 'user.isAllowLogin', inputValue: 1},
						    {boxLabel: '不允许', name: 'user.isAllowLogin', inputValue: 0}
						]
					},{
						xtype: 'radiogroup',
						id: 'radEnable',
						fieldLabel: '是否启用',
						items: [
						    {boxLabel: '启用', name: 'user.enable', inputValue: 1},
						    {boxLabel: '禁用', name: 'user.enable', inputValue: 0}
						]
					}]
		        },{
		        	id: 'tabAdminsRoles',
		            title: '隶属于',
		       	    layout: 'fit',
		       	    showed: true,
					bodyStyle:'padding:5px',
		            tabConfig: {
		                tooltip: '用户隶属于的角色'
		            },
		       	    items:[Ext.create('Ext.tree.Panel', {
		    			id: 'treeAdminsRoles',
			    		height: 368,
		    	        border: false,
		    	        useArrows: true,
		    	        rootVisible: false,
		    	        modified: false,
		    	        store: Ext.create('Ext.data.TreeStore', {
		    	            proxy: {
		    	                type: 'ajax',
		    	                url: Url.getAdminsRoles
		    	            }
		    	        }),
		    			viewConfig : {
		    				onCheckboxChange : function(e, t) {
		    				    var item = e.getTarget(this.getItemSelector(), this.getTargetEl()), record;  
		    				    if (item) {
		    				    	//标记为修改
		    				    	Ext.getCmp("treeAdminsRoles").modified = true;
		    				    	
		    				    	//获取节点
		    						record = this.getRecord(item);  
		    						var check = !record.get('checked');  
		    						record.set('checked', check);
		    						if (check) {
		    							record.bubble(function(parentNode) {
		    								parentNode.set('checked', true);  
		    							});  
		    							record.cascadeBy(function(node) {  
		    								node.set('checked', true);  
		    							});  
		    						} else {  
		    							record.cascadeBy(function(node) {  
		    								node.set('checked', false);  
		    							});
		    						}  
		    				    }  
		    				}
		    			}
		    	    })]
		        },{
		        	id: 'tabAdminsModules',
		            title: '权限',
		       	    layout: 'fit',
		       	    showed: true,
					bodyStyle:'padding:5px',
		            tabConfig: {
		                tooltip: '用户拥有的个性模块权限'
		            },
		       	    items:[Ext.create('Ext.tree.Panel', {
		    			id: 'treeAdminsModules',
			    		height: 368,
		    	        border: false,
		    	        useArrows: true,
		    	        rootVisible: false,
		    	        modified: false,
		    	        store: Ext.create('Ext.data.TreeStore', {
		    	            proxy: {
		    	                type: 'ajax',
		    	                url: Url.getAdminModules
		    	            }
		    	        }),
		    			viewConfig : {
		    				onCheckboxChange : function(e, t) {
		    				    var item = e.getTarget(this.getItemSelector(), this.getTargetEl()), record;  
		    				    if (item) {
		    				    	//标记为修改
		    				    	Ext.getCmp("treeAdminsModules").modified = true;
		    				    	
		    				    	//获取节点
		    						record = this.getRecord(item);  
		    						var check = !record.get('checked');						
		    						
		    						//勾选角色模块权限
		    						var roleModuleIds = Ext.getCmp("gridAdmins").selModel.getSelection()[0].get("roleModuleIds");
		    						for(var i=0;i<roleModuleIds.length;i++){
		    							if(record.raw.moduleId==Number(roleModuleIds[i])){
		    								check = true;
		    							}
		    						}

		    						record.set('checked', check);
		    						if (check) {
		    							record.bubble(function(parentNode) {
		    								parentNode.set('checked', true);  
		    							});  
		    							record.cascadeBy(function(node) {  
		    								node.set('checked', true);  
		    							});  
		    						} else {  
		    							record.cascadeBy(function(node) {  
		    								node.set('checked', false);  
		    							});
		    						}  
		    				    }  
		    				}
		    			}
		    	    })]
		        }]
			}]
		})],
		buttons: [{
			id: "btnOK",
		    text:'确定',
		    width: 80,
		    handler: function(){			    		 		
		    	saveAdmins(Ext.getCmp("winAdmins"), Ext.getCmp("frmAdmins"), Ext.getCmp("gridAdmins"));
		    }
		},{
			id: "btnCancel",
		    text:'取消',
		    width: 80,
		    handler: function(){
		    	Ext.getCmp("winAdmins").hide();
		    }
		}]
	});
  	    
  	    
	//------------------------------------------------------------------------------------//
	
	var storeAdmins = Ext.create('Ext.data.Store', {
        pageSize: 50,
        remoteSort: true,
        idProperty: 'userId',
        fields: [
			'userId', 'username', 'name', 'code', 'sex', 'position', 'mobile', 'email', 
			'regTime', 'updateTime', 'lastLoginTime', 'lastLoginIp', 'visitCount', 'isAllowLogin', 'enable', 
			'deptIds', 'deptNames', 'roleIds', 'roleNames', 'roleModuleIds', 'userModuleIds'
        ],
        proxy: {
            type: 'jsonp',
            url: Url.getAdmins,
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
            property: 'userId',
            direction: 'asc'
        }],
        listeners: {
        	beforeload: function(store, operation, options) {
        		// 设置查询参数
                Ext.apply(store.proxy.extraParams, { 
                	searchKey: Ext.getCmp("searchKey").getValue()
                });
			}
		}
    });
	
    //用户列表
    var gridAdmins = Ext.create('Ext.grid.Panel', {
		id: 'gridAdmins',
		border: false,
		disableSelection: false,
		loadMask: true,
		store: storeAdmins,
		selModel: Ext.create('Ext.selection.CheckboxModel'),
        columns:[{
            text: '用户名',
            dataIndex: 'username',
            width: 150,
            sortable: true
        },{
            text: '姓名',
            dataIndex: 'name',
            width: 150,
            sortable: true,
            renderer: function(value, p, record) {
            	return "["+ record.get("code") +"] " + value;
			}
        },{
            text: '角色',
            dataIndex: 'roleNames',
            width: 150,
            sortable: true
        },{
            text: '部门',
            dataIndex: 'deptNames',
            width: 150,
            sortable: true
        },{
            text: '注册时间',
            dataIndex: 'regTime',
            width: 140,
            align: 'center',
            hidden: true,
            sortable: true
        },{
            text: '更新时间',
            dataIndex: 'updateTime',
            width: 140,
            align: 'center',
            hidden: true,
            sortable: true
        },{
            text: '最后登录时间',
            dataIndex: 'lastLoginTime',
            width: 140,
            align: 'center',
            hidden: false,
            sortable: true
        },{
            text: '最后登录IP',
            dataIndex: 'lastLoginIp',
            width: 110,
            align: 'center',
            hidden: false,
            sortable: true
        },{
            text: '访问次数',
            dataIndex: 'visitCount',
            width: 90,
            align: 'right',
            sortable: true
        },{
            text: '允许登录',
            dataIndex: 'isAllowLogin',
            width: 90,
            align: 'center',
            sortable: true,
            renderer: function(value, p, record) {
            	if(value=='1')
            		return '允许';
            	else
            		return '不允许';
			}
        },{
            text: '使用状态',
            dataIndex: 'enable',
            width: 90,
            align: 'center',
            sortable: true,
            renderer: function(value, p, record) {
            	if(value=='1')
            		return '启用';
            	else
            		return '禁用';
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
		    	winAdmins.show(this,function(){
		    		this.setTitle("添加用户");
		    		var frmAdmins = Ext.getCmp("frmAdmins");
		    		var tabAdmins = frmAdmins.items.get("tabAdmins");
		    		var tabAdminsNormal = tabAdmins.items.get("tabAdminsNormal");
		    		var tabAdminsRoles = tabAdmins.items.get("tabAdminsRoles");
		    		var tabAdminsModules = tabAdmins.items.get("tabAdminsModules");
		    		
		    		tabAdmins.setActiveTab(tabAdminsNormal);
		    		
		    		//初始化输入
		    		frmAdmins.getForm().reset();
		    		tabAdminsNormal.items.get("userId").setValue(null);
		    		tabAdminsNormal.items.get("radIsAllowLogin").setValue({
		    		    'user.isAllowLogin': 0
		    		});
		    		tabAdminsNormal.items.get("radEnable").setValue({
		    		    'user.enable': 1
		    		});
		    		tabAdminsNormal.items.get("txtUsername").setReadOnly(false);
		    		
		    		//勾选部门列表
		    		try{
		        		var tree = tabAdminsNormal.items.get("cbbDept").tree;
						tree.getStore().getRootNode().cascadeBy(function(node){
							if(node.raw)
								node.set('checked', false);
						});	
		    		}catch(err){		    			
		    		}		    		
		    		
		    		//勾选角色列表
		    		var treeAdminsRoles = tabAdminsRoles.items.get("treeAdminsRoles");
		    		treeAdminsRoles.modified = false;
		    		treeAdminsRoles.getStore().getRootNode().cascadeBy(function(node){
						if(node.raw){
							node.set('checked', false);
						}
					});
					
					//勾选权限模块
		    		var treeAdminsModules = tabAdminsModules.items.get("treeAdminsModules");
		    		treeAdminsModules.modified = false;
		    		treeAdminsModules.getStore().getRootNode().cascadeBy(function(node){
						if(node.raw){
							node.set('checked', false);
						}
					});					
		    	});
		    }
        },{
        	id: 'btnEdit',
            text: '编辑',
            iconCls: 'icon-edit',
		    handler: function(btn, event) {
	    		//判断是否选择一条记录
	    		var rows = Ext.getCmp("gridAdmins").selModel.getSelection();
		    	if (rows.length != 1) {
		    		Ext.Msg.alert("提示", "请选择一条记录");
		    		return;
		    	}
		    	
		    	//显示窗口
		    	winAdmins.show(this,function(){
		    		this.setTitle("编辑用户");
		    		var frmAdmins = Ext.getCmp("frmAdmins");
		    		var tabAdmins = frmAdmins.items.get("tabAdmins");
		    		var tabAdminsNormal = tabAdmins.items.get("tabAdminsNormal");
		    		var tabAdminsRoles = tabAdmins.items.get("tabAdminsRoles");
		    		var tabAdminsModules = tabAdmins.items.get("tabAdminsModules");
		    		
		    		tabAdmins.setActiveTab(tabAdminsNormal);
		   			
		    		//初始化输入
		    		tabAdminsNormal.items.get("userId").setValue(rows[0].get("userId"));
		    		tabAdminsNormal.items.get("txtUsername").setValue(rows[0].get("username"));
		    		tabAdminsNormal.items.get("txtPassword").setValue("********");
		    		tabAdminsNormal.items.get("txtRePassword").setValue("********");
		    		tabAdminsNormal.items.get("txtCode").setValue(rows[0].get("code"));
		    		tabAdminsNormal.items.get("txtName").setValue(rows[0].get("name"));
		    		tabAdminsNormal.items.get("cbbSex").setValue(rows[0].get("sex"));
		    		tabAdminsNormal.items.get("cbbDept").setValue(rows[0].get("deptIds"));
		    		tabAdminsNormal.items.get("cbbPosition").setValue(rows[0].get("position"));
		    		tabAdminsNormal.items.get("txtMobile").setValue(rows[0].get("mobile"));
		    		tabAdminsNormal.items.get("txtEmail").setValue(rows[0].get("email"));		    		
		    		tabAdminsNormal.items.get("radIsAllowLogin").setValue({
		    		    'user.isAllowLogin': Number(rows[0].get("isAllowLogin"))
		    		});	
		    		tabAdminsNormal.items.get("radEnable").setValue({
		    		    'user.enable': Number(rows[0].get("enable"))
		    		});
		    		tabAdminsNormal.items.get("txtUsername").setReadOnly(true);
		    		
		    		//勾选部门列表
		    		try{
						var tree = tabAdminsNormal.items.get("cbbDept").tree;
						var deptIds = rows[0].get("deptIds");
						tree.getStore().getRootNode().cascadeBy(function(node){
							if(node.raw){
								node.set('checked', false);
								if(deptIds && deptIds!=""){
									for(var i=0;i<deptIds.length;i++){
										if(node.raw.id==Number(deptIds[i])){
											node.set('checked', true);
											node.parentNode.set('checked', true);
											break;
										}
									}
								}
							}
						});
		    		}catch(err){		    			
		    		} 		
		    		
		    		//勾选角色列表
		    		var roleIds = rows[0].get("roleIds");
		    		var treeAdminsRoles = tabAdminsRoles.items.get("treeAdminsRoles");
		    		treeAdminsRoles.modified = false;
		    		treeAdminsRoles.getStore().getRootNode().cascadeBy(function(node){
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
					
					//勾选权限模块
					var roleModuleIds = rows[0].get("roleModuleIds");
					var userModuleIds = rows[0].get("userModuleIds");
		    		var treeAdminsModules = tabAdminsModules.items.get("treeAdminsModules");
		    		treeAdminsModules.modified = false;
		    		treeAdminsModules.getStore().getRootNode().cascadeBy(function(node){
		    			if(node.raw){
							node.set('checked', false);
							
							//勾选角色模块权限
							for(var i=0;i<roleModuleIds.length;i++){
								if(node.raw.moduleId==Number(roleModuleIds[i])){
									node.set('checked', true);									
									break;
								}
							}
							
							//勾选用户模块权限
							for(var i=0;i<userModuleIds.length;i++){
								if(node.raw.moduleId==Number(userModuleIds[i])){
									node.set('checked', true);
									break;
								}
							}
						}
					});
		    	});
		    }
        },{
            text: '删除',
            iconCls: 'icon-del',
		    handler: function(btn, event) {
		    	delAdmins(Ext.getCmp("gridAdmins"));
		    }
        },'->',{
        	id: 'searchKey',
            xtype: 'textfield',
            name: 'searchKey',
            emptyText: '请输入用户名/姓名/工号',
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
		    	Ext.getCmp("gridAdmins").getStore().loadPage(1);
		    }
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: storeAdmins,
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
	       	    items:[gridAdmins]
	        }]
	    })],
		renderTo: document.body
	});
	    
	    	    
	//------------------------------------------------------------------------------------//
 	    
	
	//保存用户
	function saveAdmins(win, frm, grid){
		// 构建Ajax参数
		var ajaxparams = "";
		var checkedNodes;

		var treeAdminsRoles = Ext.getCmp("treeAdminsRoles");	
		var treeAdminsModules = Ext.getCmp("treeAdminsModules");
		
		//勾选的角色
		ajaxparams += "roleModified="+ treeAdminsRoles.modified +"&roleIds=";
		checkedNodes = treeAdminsRoles.getChecked();
		for ( var i = 0; i < checkedNodes.length; i++) {
			if(checkedNodes[i].raw && checkedNodes[i].raw.roleId && checkedNodes[i].raw.nLeaf==1){
				ajaxparams += checkedNodes[i].raw.roleId;
				if (i < checkedNodes.length - 1) 
					ajaxparams += ",";
			}
		}
		
		//勾选的模块
		ajaxparams += "&moduleModified="+ treeAdminsModules.modified +"&moduleIds=";
		checkedNodes = treeAdminsModules.getChecked();
		for ( var i = 0; i < checkedNodes.length; i++) {
			if(checkedNodes[i].raw && checkedNodes[i].raw.moduleId){
				ajaxparams += checkedNodes[i].raw.moduleId;
				if (i < checkedNodes.length - 1) 
					ajaxparams += ",";
			}
		}
		
		// 提交表单
		frm.submit({
		    url: Url.saveAdmins,
			params : ajaxparams,
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
	
	
	//删除用户
	function delAdmins(grid){
    	var rows = grid.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的用户删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "userIds=";
    					for ( var i = 0; i < rows.length; i++) {
    						ajaxparams += rows[i].get('userId');
    						if (i < rows.length - 1) 
    							ajaxparams += ",";
    					}
    					
    					// 发送请求
    					grid.el.mask("正在删除用户...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delAdmins,
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
    		Ext.Msg.alert("提示", "请选择用户");
    	}
	}
	
	
	//搜索用户
	Ext.getCmp("btnSearch").handler();	
	
});
</script>
</head>
<body>
    <!-- 界面加载 -->
    <div id="loading"><span class="title"></span><span class="logo"></span></div>
</body>
</html>