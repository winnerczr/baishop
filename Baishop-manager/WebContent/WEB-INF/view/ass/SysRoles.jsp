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
    	getRoles: '${page_context}/ass/SysRoles.jspx?func=getRoles',
    	delRoles: '${page_context}/ass/SysRoles.jspx?func=delRoles',
    	saveRoles: '${page_context}/ass/SysRoles.jspx?func=saveRoles',
      	upRoles: '${page_context}/ass/SysRoles.jspx?func=upRoles',
   		downRoles: '${page_context}/ass/SysRoles.jspx?func=downRoles',
   		getUsersByRoles: '${page_context}/ass/SysRoles.jspx?func=getUsersByRoles',   				
    	getModulesByRoles: '${page_context}/ass/SysModules.jspx?func=getModules&action=SysRoles'
    };
  	    
  	    
	//------------------------------------------------------------------------------------//
	
	//角色组添加/编辑窗口
    var winGroup = Ext.create('widget.window', {
    	id: 'winGroup',
		title: '角色组',
		width: 280,
		height: 200,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmGroup',
			bodyStyle:'padding:20px 0 0 23px',
			border: false,
			autoScroll: true,
			defaults: { 
				listeners: {
					specialkey: function(obj,e){
						 if (e.getKey() == Ext.EventObject.ENTER) {
							Ext.getCmp("btnGroupOK").handler();
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
			    id: 'group_hddRoleId',
			    name: 'role.roleId',
			    value: ''
			},{
			    xtype: 'hiddenfield',
			    id: 'group_hddRolePid',
			    name: 'role.rolePid',
			    value: ''
			},{
			    xtype: 'hiddenfield',
			    id: 'group_hddLeaf',
			    name: 'role.leaf',
			    value: ''
			},{
				xtype:'textfield',
				id: 'group_txtRoleName',
				name: 'role.roleName',
				fieldLabel: '角色组',
				allowBlank: false
			},{
				xtype:'textarea',
				id: 'group_txtRoleDesc',
				name: 'role.roleDesc',
				fieldLabel: '描述'
			},{
				xtype:'hiddenfield',
				id: 'group_hddRoleType',
				name: 'role.roleType',
				value: 'ROLE_GROUP'
			}]
		})],
		buttons: [{
			id: "btnGroupOK",
			text:'确定',
			width: 80,
			handler: function(){			    		 		
				saveRoles(Ext.getCmp("winGroup"), Ext.getCmp("frmGroup"), Ext.getCmp("treeRoles"));
			}
		},{
			text:'取消',
			width: 80,
			handler: function(){
				Ext.getCmp("winGroup").hide();
			}
		}]
	});
	
  	    
	//角色添加/编辑窗口
    var winRoles = Ext.create('widget.window', {
    	id: 'winRoles',
		title: '角色信息',
		width: 325,
		height: 477,
		layout: 'fit',
		closable: true,
		closeAction: 'hide',
		modal: true,
		resizable: false,
		items: [Ext.create('Ext.form.Panel', {
			id: 'frmRoles',
			bodyStyle:'padding:5px',
			border: false,
			autoScroll: false,
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 50, 
				msgTarget: 'side',
				width: 260
			},
			items: [{
				xtype: 'tabpanel',
				id: 'tabRoles',
		        activeTab: 0, 
		        plain: true,
		        items: [{
		        	id: 'tabRolesNormal',
		            title: '常规',
		       	    layout: 'fit',
		       	    showed: true,
					bodyStyle:'padding:20px',
					defaults: { 
						listeners: {
							specialkey: function(obj,e){
								 if (e.getKey() == Ext.EventObject.ENTER) {
									Ext.getCmp("btnRoleOK").handler();
								}
							}
						}
					},
		       	    items:[{
					    xtype: 'hiddenfield',
					    id: 'hddRoleId',
					    name: 'role.roleId',
					    value: ''
					},{
					    xtype: 'hiddenfield',
					    id: 'hddRolePid',
					    name: 'role.rolePid',
					    value: ''
					},{
					    xtype: 'hiddenfield',
					    id: 'hddLeaf',
					    name: 'role.leaf',
					    value: ''
					},{
						xtype:'textfield',
						id: 'txtRoleName',
						name: 'role.roleName',
						fieldLabel: '角色名',
						allowBlank: false
					},{
						xtype:'textarea',
						id: 'txtRoleDesc',
						name: 'role.roleDesc',
						fieldLabel: '描述'
					},{
						xtype:'combobox',
						id: 'cbbRoleType',
						name: 'role.roleType',
						fieldLabel: '类型',
						editable: false,
						store: [['ROLE_ADMIN','管理员'],['ROLE_USER','普通用户']],
						value: 'ROLE_USER'
					},{
			            xtype: 'container',
			            id: 'conRoleUsers',
			            layout: 'column',
			            items: [{
			                xtype: 'label',
			                text: '成员:',
			                margin: '5 0 5 0'
			            }, Ext.create('Ext.grid.Panel', {
			    			id: 'gridRolesAdmins',
			                margin: '5 0 5 25',
			        		width: 204,
			        		height: 205,
			    			border: true,
			    			disableSelection: false,
			    			loadMask: true,
			    			store: Ext.create('Ext.data.Store', {
			    		        idProperty: 'userId',
			    		        fields: [
			    					'userId', 'username', 'name', 'code'
			    		        ],
			    		        proxy: {
			    		            type: 'jsonp',
			    		            url: Url.getUsersByRoles,
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
			    	        columns:[{
			    	            text: '用户名',
			    	            dataIndex: 'username',
			    	            width: 101,
			    	            sortable: true
			    	        },{
			    	            text: '姓名',
			    	            dataIndex: 'name',
			    	            width: 101,
			    	            sortable: true,
			    	            renderer: function(value, p, record) {
			    	            	return "["+ record.get("code") +"] " + value;
			    				}
			    	        }]
			    		})]
			        }]
		        },{
		        	id: 'tabRolesModules',
		            title: '权限',
		       	    layout: 'fit',
		       	    showed: true,
					bodyStyle:'padding:5px',
		            tabConfig: {
		                tooltip: '角色拥有的模块权限'
		            },
		       	    items:[Ext.create('Ext.tree.Panel', {
		    			id: 'treeRoleModules',
			    		height: 368,
		    	        border: false,
		    	        useArrows: true,
		    	        rootVisible: false,
		    	        modified: false,
		    	        store: Ext.create('Ext.data.TreeStore', {
		    	            proxy: {
		    	                type: 'ajax',
		    	                url: Url.getModulesByRoles
		    	            }
		    	        }),
		    			viewConfig : {
		    				onCheckboxChange : function(e, t) {
		    				    var item = e.getTarget(this.getItemSelector(), this.getTargetEl()), record;  
		    				    if (item) {
		    				    	//标记为修改
		    				    	Ext.getCmp("treeRoleModules").modified = true;
		    				    	
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
		        }]
			}]
		})],
		buttons: [{
			id: "btnRoleOK",
			text:'确定',
			width: 80,
			handler: function(){			    		 		
				saveRoles(Ext.getCmp("winRoles"), Ext.getCmp("frmRoles"), Ext.getCmp("treeRoles"));
			}
		},{
			text:'取消',
			width: 80,
			handler: function(){
				Ext.getCmp("winRoles").hide();
			}
		}]
	});
  	    
  	    
	//------------------------------------------------------------------------------------//

	//角色列表
 	var treeRoles = Ext.create('Ext.tree.Panel', {
		id: 'treeRoles',
		border: false,
		split:true,
        animate: true,
		autoscroll: true,
		useArrows: true,
		multiSelect: false,
		singleExpand: false,
		rootVisible: true,
		store: Ext.create('Ext.data.TreeStore', {
			idProperty: 'roleId',
			fields: [
				'roleId', 'rolePid', 'roleName', 'roleType', 'roleDesc', 'sort', 'leaf', 'nLeaf', 'modules'
			], 
		    root: {  
		    	id : 0,
		        text:'用户角色',  
		    	roleId : 0,
		        roleName:'用户角色',
		        iconCls: 'icon-role-root',
		        expanded: true
		    },
	        proxy: {
	            type: 'ajax',
	            url: Url.getRoles,
				listeners: {
	    			exception: function(proxy, request, operation, options) {
	    				Ext.Msg.alert("提示", "加载数据出错："+proxy.getReader().rawData['msg'] );
	    			}
	    		}
	        }
		}),
		columns:[{
            xtype: 'treecolumn',
			text: '角色名',
			dataIndex: 'roleName',
			width: 200,
			sortable: false
		},{
			text: '描述',
			dataIndex: 'roleDesc',
			width: 450,
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
				text: '组',
				iconCls: 'icon-role-group',
				handler: function(btn, event) {
					//判断是否选择一条记录
					var rows = Ext.getCmp("treeRoles").selModel.getSelection();
					if (rows.length==1 && (rows[0].data.id==0 || rows[0].raw.nLeaf==0)) {	
						//显示窗口
						winGroup.show(this,function(){
							this.setTitle("添加角色组");
							var frmGroup = Ext.getCmp("frmGroup");
							frmGroup.getForm().reset();
							frmGroup.items.get("group_hddRoleId").setValue(null);
							frmGroup.items.get("group_hddRolePid").setValue(rows[0].get("roleId"));
							frmGroup.items.get("group_hddLeaf").setValue(0);
						});
					}else{
						Ext.Msg.alert("提示", "请选择角色组");
						return;
					}
				}
			},{
				text: '角色',
				iconCls: 'icon-role-leaf',
				handler: function(btn, event) {
					//判断是否选择一条记录
					var rows = Ext.getCmp("treeRoles").selModel.getSelection();
					if (rows.length==1 && (rows[0].data.id==0 || rows[0].raw.nLeaf==0)) {
						//显示窗口
						winRoles.show(this,function(){
							this.setTitle("添加角色");
							var frmRoles = Ext.getCmp("frmRoles");
				    		var tabRoles = frmRoles.items.get("tabRoles");
				    		var tabRolesNormal = tabRoles.items.get("tabRolesNormal");
				    		var tabRolesModules = tabRoles.items.get("tabRolesModules");
							
							frmRoles.getForm().reset();
							tabRoles.setActiveTab(tabRolesNormal);	
							tabRolesNormal.items.get("hddRoleId").setValue(null);
							tabRolesNormal.items.get("hddRolePid").setValue(rows[0].get("roleId"));
							tabRolesNormal.items.get("hddLeaf").setValue(1);
							tabRolesNormal.items.get("cbbRoleType").setValue('ROLE_USER');

							//载入成员
							tabRolesNormal.items.get("conRoleUsers").items.get("gridRolesAdmins").store.load({
								params:{
									roleId: null
								}
							});
							
							//勾选权限模块
				    		var treeRoleModules = tabRolesModules.items.get("treeRoleModules");
				    		treeRoleModules.modified = false;
				    		treeRoleModules.getStore().getRootNode().cascadeBy(function(node){
								if(node.raw){
									node.set('checked', false);
								}
							});	
						});						
					}else{
						Ext.Msg.alert("提示", "请选择角色组");
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
				var rows = Ext.getCmp("treeRoles").selModel.getSelection();
				if (rows.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录");
					return;
				}
				
				if(rows[0].data.id==0){
					Ext.Msg.alert("提示", "根节点不能编辑");
					return;
				}
				
				
				//显示窗口
				if(rows[0].raw.nLeaf==0){
					winGroup.show(this,function(){
						this.setTitle("编辑组");
						var frmGroup = Ext.getCmp("frmGroup");
						frmGroup.items.get("group_hddRoleId").setValue(rows[0].get("roleId"));
						frmGroup.items.get("group_txtRoleName").setValue(rows[0].get("roleName"));
						frmGroup.items.get("group_txtRoleDesc").setValue(rows[0].get("roleDesc"));
					});		
				}else{
					winRoles.show(this,function(){
						this.setTitle("编辑角色");
						var frmRoles = Ext.getCmp("frmRoles");
			    		var tabRoles = frmRoles.items.get("tabRoles");
			    		var tabRolesNormal = tabRoles.items.get("tabRolesNormal");
			    		var tabRolesModules = tabRoles.items.get("tabRolesModules");
			    		
			    		tabRoles.setActiveTab(tabRolesNormal);			    		
						tabRolesNormal.items.get("hddRoleId").setValue(rows[0].get("roleId"));
						tabRolesNormal.items.get("txtRoleName").setValue(rows[0].get("roleName"));
						tabRolesNormal.items.get("txtRoleDesc").setValue(rows[0].get("roleDesc"));
						tabRolesNormal.items.get("cbbRoleType").setValue(rows[0].get("roleType"));
						
						//载入成员
						tabRolesNormal.items.get("conRoleUsers").items.get("gridRolesAdmins").store.load({
							params:{
								roleId: rows[0].get("roleId")
							}
						});
						
						//勾选权限模块
						var roleModuleIds = rows[0].get("modules");
			    		var treeRoleModules = tabRolesModules.items.get("treeRoleModules");
			    		treeRoleModules.modified = false;
			    		treeRoleModules.getStore().getRootNode().cascadeBy(function(node){
			    			if(node.raw){
								node.set('checked', false);
								
								//勾选角色模块权限
								for(var i=0;i<roleModuleIds.length;i++){
									if(node.raw.moduleId==Number(roleModuleIds[i])){
										node.set('checked', true);									
										break;
									}
								}
							}
						});
					});				
				}	
			}
		},{
			text: '删除',
			iconCls: 'icon-del',
			handler: function(btn, event) {
				delRoles(Ext.getCmp("treeRoles"));
			}
        },'-',{
            text: '上移',
            iconCls: 'icon-up',
		    handler: function(btn, event) {
				upRoles(Ext.getCmp("treeRoles"));
		    }
        },{
            text: '下移',
            iconCls: 'icon-down',
		    handler: function(btn, event) {		    	
				downRoles(Ext.getCmp("treeRoles"));				    	
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
				items:[treeRoles]
			}]
		})],
		renderTo: document.body
	});
	    
	    	    
	//------------------------------------------------------------------------------------//
 	    
	
	//保存角色
	function saveRoles(win, frm, tree){
		// 构建Ajax参数
		var ajaxparams = "";
		var treeRoleModules = Ext.getCmp("treeRoleModules");
		
		//勾选的模块
		ajaxparams += "&moduleModified="+ treeRoleModules.modified +"&moduleIds=";
		var checkedNodes = treeRoleModules.getChecked();
		for ( var i = 0; i < checkedNodes.length; i++) {
			if(checkedNodes[i].raw && checkedNodes[i].raw.moduleId){
				ajaxparams += checkedNodes[i].raw.moduleId;
				if (i < checkedNodes.length - 1) 
					ajaxparams += ",";
			}
		}
		
		// 提交表单
		frm.submit({
		    url: Url.saveRoles,
			params : ajaxparams,
			waitTitle : "提示",
			waitMsg : "正在保存...",
		    success: function(form, action) {
		    	tree.store.load();
		    	win.hide();
		    },
		    failure: function(form, action) {
		    	Ext.formFailure(form, action);
		    }
		});
	}
		
		
	//删除角色
	function delRoles(tree){
    	var rows = tree.selModel.getSelection();
    	if (rows.length > 0) {
			if(rows[0].data.id==0){
				Ext.Msg.alert("提示", "根节点不能删除");
				return;
			}
    		
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的角色删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "roleId=" + rows[0].get('roleId');
    					
    					// 发送请求
    					tree.el.mask("正在删除角色...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delRoles,
    						params : ajaxparams,
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
    		Ext.Msg.alert("提示", "请选择角色");
    	}
	}	
	
	
	//上移角色
	function upRoles(tree){
    	var rows = tree.selModel.getSelection();
    	if (rows.length > 0) {
	    	if(rows[0].data.id==0){
				Ext.Msg.alert("提示", "根节点不能上移");
				return;
			}

    		//找到上一个节点
			var node = tree.store.getNodeById(rows[0].get('roleId'));
			var prevRoleId = "";
			for(var i=0;i<node.parentNode.childNodes.length;i++){
				if(node.parentNode.childNodes[i]==node){
					if(i-1<0){
						return;
					}else{
						prevRoleId = node.parentNode.childNodes[i-1].data.id;
						break;
					}
				}
			}    					

			// 构建Ajax参数
			var ajaxparams = "roleId=" + rows[0].get('roleId') + "&prevRoleId=" + prevRoleId;
			
			// 发送请求
			tree.el.mask("正在上移角色...", 'x-mask-loading');
			Ext.Ajax.request({
				url : Url.upRoles,
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
    		Ext.Msg.alert("提示", "请选择角色");
    	}
	}
	
	
	//下移角色
	function downRoles(tree){
    	var rows = tree.selModel.getSelection();
    	if (rows.length > 0) {
	    	if(rows[0].data.id==0){
				Ext.Msg.alert("提示", "根节点不能下移");
				return;
			}			

    		//找到下一个节点
			var node = tree.store.getNodeById(rows[0].get('roleId'));
			var nextRoleId = "";
			for(var i=0;i<node.parentNode.childNodes.length;i++){
				if(node.parentNode.childNodes[i]==node){
					if(i+1>=node.parentNode.childNodes.length){
						return;
					} else {    							
						nextRoleId = node.parentNode.childNodes[i+1].data.id;
						break;
					}
				}
			}    					

			// 构建Ajax参数
			var ajaxparams = "roleId=" + rows[0].get('roleId') + "&nextRoleId=" + nextRoleId; 			
			
			// 发送请求
			tree.el.mask("正在下移角色...", 'x-mask-loading');
			Ext.Ajax.request({
				url : Url.downRoles,
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
    		Ext.Msg.alert("提示", "请选择角色");
    	}
	}
  		
});
</script>
</head>
<body>
    <!-- 界面加载 -->
    <div id="loading"><span class="title"></span><span class="logo"></span></div>
</body>
</html>