<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="../Meta.jsp" %>
<title>${module_title}</title>
<script type="text/javascript">
Ext.onReady(function(){
    Ext.tip.QuickTipManager.init();

    //业务请求的URL
    Url = {
    	getSellingGoodsList: '${page_context}/goods/GoodsManage.jspx?func=getGoodsList&isDelete=0&isOnSale=1',
    	getStorageGoodsList: '${page_context}/goods/GoodsManage.jspx?func=getGoodsList&isDelete=0&isOnSale=0',
    	getDeletedGoodsList: '${page_context}/goods/GoodsManage.jspx?func=getGoodsList&isDelete=1',
    	delGoods: '${page_context}/goods/GoodsManage.jspx?func=delGoods',
    	delRealGoods: '${page_context}/goods/GoodsManage.jspx?func=delRealGoods',
    	recoveryGoods: '${page_context}/goods/GoodsManage.jspx?func=recoveryGoods',
    	upShelfGoods: '${page_context}/goods/GoodsManage.jspx?func=upShelfGoods',
    	offShelfGoods: '${page_context}/goods/GoodsManage.jspx?func=offShelfGoods',
    	modifyPrice: '${page_context}/goods/GoodsManage.jspx?func=modifyPrice',
    	modifyInventory: '${page_context}/goods/GoodsManage.jspx?func=modifyInventory',
    	clearDeledGoods: '${page_context}/goods/GoodsManage.jspx?func=clearDeledGoods'
    };
    
     	
 	//------------------------------------------------------------------------------------//
  
    //出售中的商品数据源
    var storeSelling = Ext.create('Ext.data.Store', {
        pageSize: 50,
        remoteSort: true,
        idProperty: 'goodsId',
        fields: [
			'goodsId', 'goodsName', 'goodsImage', 'shopPrice', 'goodsNumber', 'sellCount', 'publishTime'
        ],
        proxy: {
            type: 'jsonp',
            url: Url.getSellingGoodsList,
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
            property: 'publishTime',
            direction: 'desc'
        }],
        listeners: {
        	beforeload: function(store, operation, options) {
        		// 设置查询参数
                Ext.apply(store.proxy.extraParams, { 
                	searchKey: Ext.getCmp("searchKeySelling").getValue()
                });
			}
		}
    });


    //出售中的商品列表
    var gridSelling = Ext.create('Ext.grid.Panel', {
    	id: 'gridSelling',
        border: false,
        disableSelection: false,
        loadMask: true,
        store: storeSelling,
        selModel: Ext.create('Ext.selection.CheckboxModel'),
        columns:[{
            dataIndex: 'goodsImage',
            width: 70,
            sortable: false,
            renderer: function(value, p, record) {
				return Ext.String.format(
					'<a href="/product.html?id={0}" target="_blank" title="{1}">'+
					'<img src="{2}" style="width: 54px;height: 54px; margin:5px 2px 5px 2px;"/>'+
					'</a>',
					record.data.goodsId,
					record.data.goodsName,
					value
				);
			}
        },{
            text: '商品名称',
            dataIndex: 'goodsName',
            flex: 1,
            sortable: true,
            renderer: function(value, p, record) {
				return Ext.String.format(
					'<a href="/product.html?id={0}" target="_blank" title="{1}">{1}</a>',
					record.data.goodsId,
					value
				);
			}
        },{
            text: '价格',
            dataIndex: 'shopPrice',
            width: 100,
            align: 'right',
            renderer: function(value, p, record) {
				return Ext.String.format(
					'<span style="color: #F60;">￥{0}</span>',
					value.fomatMoney(2)
				);
			},
            sortable: true,
            field: {
                xtype: 'numberfield',
                allowBlank: false,
                minValue: 0,
                maxValue: 99999999.99,
                hideTrigger: true,
                keyNavEnabled: false,
                mouseWheelEnabled: false
            }
        },{
            text: '库存',
            dataIndex: 'goodsNumber',
            width: 100,
            align: 'right',
            sortable: true,
            field: {
                xtype: 'numberfield',
                allowBlank: false,
                minValue: 0,
                maxValue: 999999999,
                hideTrigger: true,
                keyNavEnabled: false,
                mouseWheelEnabled: false
            }
        },{
            text: '总销量',
            dataIndex: 'sellCount',
            width: 100,
            align: 'right',
            sortable: true
        },{
            text: '发布时间',
            dataIndex: 'publishTime',
            width: 150,
            align: 'center',
            sortable: true
        }],
        tbar: [{
            text: '我要卖',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	window.location.href = "PublishGoods.jspx";
		    }
        },'-',{
            text: '下架',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	offShelfGoods(Ext.getCmp("gridSelling"));
		    }
        },'-',{
            text: '编辑',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	var rows = Ext.getCmp("gridSelling").selModel.getSelection();
		    	if (rows.length > 0) {
		    		window.location.href = "PublishGoods.jspx?action=edit&goodsId="+ rows[0].get('goodsId');
		    	} else {
		    		Ext.Msg.alert("提示", "请选择商品");
		    	}
		    }
        },{
            text: '删除',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	delGoods(Ext.getCmp("gridSelling"));
		    }
        },'->',{
        	id: 'searchKeySelling',
            xtype: 'textfield',
            name: 'searchKey',
            emptyText: '请输入商品名称或货号',
            listeners : {
            	specialkey: function(obj,e){
					if(e.getCharCode()==e.ENTER)
						Ext.getCmp("btnSearchSelling").handler();
				}
			}
        },{
        	id: 'btnSearchSelling',
            text: '搜索',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	storeSelling.loadPage(1);
		    }
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: storeSelling,
            displayInfo: true,
            displayMsg: '显示商品 {0} - {1} 共 {2}',
            emptyMsg: "没有可显示的商品",
            items:[
			    '-', {
			    text: '橱窗推荐',
			    handler: function(btn, event) {
			
			    }
			}, {
			    text: '设置促销',
			    handler: function(btn, event) {
			
			    }
			}, {
			    text: '设置运费',
			    handler: function(btn, event) {
			
			    }
			}]
        }),
        plugins: [
		    Ext.create('Ext.grid.plugin.CellEditing', {
		        clicksToEdit: 1
		    })
		],
        listeners : {
        	edit: function(editor, e, options){
        		switch(e.field){
        		case "shopPrice":
	        		modifyPrice(e.grid, e.record, e.value);
	        		break;
        		case "goodsNumber":
        			modifyInventory(e.grid, e.record, e.value);
        			break;
        		}
			}
		}
    });
    
    
    //------------------------------------------------------------------------------------//
    
    //仓库中的商品数据源
    var storeStorage = Ext.create('Ext.data.Store', {
        pageSize: 50,
        remoteSort: true,
        idProperty: 'goodsId',
        fields: [
			'goodsId', 'goodsName', 'goodsImage', 'shopPrice', 'goodsNumber', 'sellCount', 'publishTime'
        ],
        proxy: {
            type: 'jsonp',
            url: Url.getStorageGoodsList,
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
            property: 'publishTime',
            direction: 'desc'
        }],
        listeners: {
        	beforeload: function(store, operation, options) {
        		// 设置查询参数
                Ext.apply(store.proxy.extraParams, { 
                	searchKey: Ext.getCmp("searchKeyStorage").getValue()
                });
			}
		}
    });


    //仓库中的商品列表
    var gridStorage = Ext.create('Ext.grid.Panel', {
    	id: 'gridStorage',
        border: false,
        disableSelection: false,
        loadMask: true,
        store: storeStorage,
        selModel: Ext.create('Ext.selection.CheckboxModel'),
        columns:[{
            dataIndex: 'goodsImage',
            width: 70,
            sortable: false,
            renderer: function(value, p, record) {
				return Ext.String.format(
					'<a href="/product.html?id={0}" target="_blank" title="{1}">'+
					'<img src="{2}" style="width: 54px;height: 54px; margin:5px 2px 5px 2px;"/>'+
					'</a>',
					record.data.goodsId,
					record.data.goodsName,
					value
				);
			}
        },{
            text: '商品名称',
            dataIndex: 'goodsName',
            flex: 1,
            sortable: true,
            renderer: function(value, p, record) {
				return Ext.String.format(
					'<a href="/product.html?id={0}" target="_blank" title="{1}">{1}</a>',
					record.data.goodsId,
					value
				);
			}
        },{
            text: '价格',
            dataIndex: 'shopPrice',
            width: 100,
            align: 'right',
            renderer: function(value, p, record) {
				return Ext.String.format(
					'<span style="color: #F60;">￥{0}</span>',
					fomatMoney(value, 2)
				);
			},
            sortable: true,
            field: {
                xtype: 'numberfield',
                allowBlank: false,
                minValue: 0,
                maxValue: 99999999.99,
                hideTrigger: true,
                keyNavEnabled: false,
                mouseWheelEnabled: false
            }
        },{
            text: '库存',
            dataIndex: 'goodsNumber',
            width: 100,
            align: 'right',
            sortable: true,
            field: {
                xtype: 'numberfield',
                allowBlank: false,
                minValue: 0,
                hideTrigger: true,
                keyNavEnabled: false,
                mouseWheelEnabled: false
            }
        },{
            text: '总销量',
            dataIndex: 'sellCount',
            width: 100,
            align: 'center', 
            sortable: true
        },{
            text: '发布时间',
            dataIndex: 'publishTime',
            width: 150,
            align: 'center',
            sortable: true
        }],
        tbar: [{
            text: '上架',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	upShelfGoods(Ext.getCmp("gridStorage"));
		    }
        },'-',{
            text: '编辑',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	var rows = Ext.getCmp("gridStorage").selModel.getSelection();
		    	if (rows.length > 0) {
		    		window.location.href = "PublishGoods.jspx?action=edit&goodsId="+ rows[0].get('goodsId');
		    	} else {
		    		Ext.Msg.alert("提示", "请选择商品");
		    	}
		    }
        },{
            text: '删除',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	delGoods(Ext.getCmp("gridStorage"));
		    }
        },'->',{
        	id: 'searchKeyStorage',
            xtype: 'textfield',
            name: 'searchKey',
            emptyText: '请输入商品名称或货号',	
            listeners : {
            	specialkey: function(obj,e){
					if(e.getCharCode()==e.ENTER)
						Ext.getCmp("btnSearchStorage").handler();
				}
			}
        },{
        	id: 'btnSearchStorage',
            text: '搜索',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	storeStorage.loadPage(1);
		    }
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: storeStorage,
            displayInfo: true,
            displayMsg: '显示商品 {0} - {1} 共 {2}',
            emptyMsg: "没有可显示的商品",
            items:[
   			    '-', {
   			    text: '设置运费',
   			    handler: function(btn, event) {
   			
   			    }
   			}]
        }),
        plugins: [
   		    Ext.create('Ext.grid.plugin.CellEditing', {
   		        clicksToEdit: 1
   		    })
   		],
        listeners : {
        	edit: function(editor, e, options){
        		switch(e.field){
        		case "shopPrice":
	        		modifyPrice(e.grid, e.record, e.value);
	        		break;
        		case "goodsNumber":
        			modifyInventory(e.grid, e.record, e.value);
        			break;
        		}
			}
		}
    });
    
    
  	//------------------------------------------------------------------------------------//
  	
  	//已删除的商品数据源
    var storeDeleted = Ext.create('Ext.data.Store', {
        pageSize: 50,
        remoteSort: true,
        idProperty: 'goodsId',
        fields: [
			'goodsId', 'goodsName', 'goodsImage', 'shopPrice', 'goodsNumber', 'sellCount', 'publishTime'
        ],
        proxy: {
            type: 'jsonp',
            url: Url.getDeletedGoodsList,
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
            property: 'publishTime',
            direction: 'desc'
        }],
        listeners: {
        	beforeload: function(store, operation, options) {
        		// 设置查询参数
                Ext.apply(store.proxy.extraParams, { 
                	searchKey: Ext.getCmp("searchKeyDeleted").getValue()
                });
			}
		}
    });


    //已删除的商品列表
    var gridDeleted = Ext.create('Ext.grid.Panel', {
    	id: 'gridDeleted',
        border: false,
        disableSelection: false,
        loadMask: true,
        store: storeDeleted,
        selModel: Ext.create('Ext.selection.CheckboxModel'),
        columns:[{
            dataIndex: 'goodsImage',
            width: 70,
            sortable: false,
            renderer: function(value, p, record) {
				return Ext.String.format(
					'<a href="/product.html?id={0}" target="_blank" title="{1}">'+
					'<img src="{2}" style="width: 54px;height: 54px; margin:5px 2px 5px 2px;"/>'+
					'</a>',
					record.data.goodsId,
					record.data.goodsName,
					value
				);
			}
        },{
            text: '商品名称',
            dataIndex: 'goodsName',
            flex: 1,
            sortable: true,
            renderer: function(value, p, record) {
				return Ext.String.format(
					'<a href="/product.html?id={0}" target="_blank" title="{1}">{1}</a>',
					record.data.goodsId,
					value
				);
			}
        },{
            text: '价格',
            dataIndex: 'shopPrice',
            width: 100,
            align: 'center',
            renderer: function(value, p, record) {
				return Ext.String.format(
					'<span style="color: #F60;">￥{0}</span>',
					fomatMoney(value, 2)
				);
			},
            sortable: true
        },{
            text: '库存',
            dataIndex: 'goodsNumber',
            width: 100,
            align: 'center',
            sortable: true
        },{
            text: '总销量',
            dataIndex: 'sellCount',
            width: 100,
            align: 'center', 
            sortable: true
        },{
            text: '发布时间',
            dataIndex: 'publishTime',
            width: 150,
            align: 'center',
            sortable: true
        }],
        tbar: [{
            text: '恢复',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	recoveryGoods(Ext.getCmp("gridDeleted"));
		    }
        },{
            text: '彻底删除',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	delRealGoods(Ext.getCmp("gridDeleted"));
		    }
        },'->',{
        	id: 'searchKeyDeleted',
            xtype: 'textfield',
            name: 'searchKey',
            emptyText: '请输入商品名称或货号',	
            listeners : {
            	specialkey: function(obj,e){
					if(e.getCharCode()==e.ENTER)
						Ext.getCmp("btnSearchDeleted").handler();
				}
			}
        },{
        	id: 'btnSearchDeleted',
            text: '搜索',
            iconCls: 'icon-cls',
		    handler: function(btn, event) {
		    	storeDeleted.loadPage(1);
		    }
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: storeDeleted,
            displayInfo: true,
            displayMsg: '显示商品 {0} - {1} 共 {2}',
            emptyMsg: "没有可显示的商品",
            items:[
			    '-', {
			    text: '清空已删除商品',
			    handler: function(btn, event) {
			    	clearDeledGoods(Ext.getCmp("gridDeleted"));
			    }
			}]
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
		            title: '出售中的商品',
		       	    layout: 'fit',
		       	    showed: true,
		       	    items:[gridSelling]
		        },{
		            title: '仓库中的商品',
		       	    layout: 'fit',
		       	    showed: false,
					listeners: {
						activate: function(obj, options) {
							if(!obj.shwoed){
								obj.shwoed = true;
								obj.add(gridStorage);
								Ext.getCmp("btnSearchStorage").handler();
							}
						}
					}
		        },{
		            title: '已删除的商品',
		       	    layout: 'fit',
		       	    showed: false,
					listeners: {
						activate: function(obj, options) {
							if(!obj.shwoed){
								obj.shwoed = true;
								obj.add(gridDeleted);
								Ext.getCmp("btnSearchDeleted").handler();
							}
						}
					}
		        }]
		    })
		],
		renderTo: document.body
	});
 	
 	
	//------------------------------------------------------------------------------------//
 	 	
 	
 	//恢复商品
 	function recoveryGoods(grid) {
    	var rows = grid.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要恢复所选择的商品吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "goodsIds=";
    					for ( var i = 0; i < rows.length; i++) {
    						ajaxparams += rows[i].get('goodsId');
    						if (i < rows.length - 1) 
    							ajaxparams += ",";
    					}
    					
    					// 发送请求
    					grid.el.mask("正在恢复商品...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.recoveryGoods,
    						params : ajaxparams,
    						method : "POST",
    						waitMsg : "正在恢复...",
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
    		Ext.Msg.alert("提示", "请选择商品");
    	}
 	}
 	
 	
 	//删除商品
 	function delGoods(grid) {
    	var rows = grid.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的商品删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "goodsIds=";
    					for ( var i = 0; i < rows.length; i++) {
    						ajaxparams += rows[i].get('goodsId');
    						if (i < rows.length - 1) 
    							ajaxparams += ",";
    					}
    					
    					// 发送请求
    					grid.el.mask("正在删除商品...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delGoods,
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
    		Ext.Msg.alert("提示", "请选择商品");
    	}
 	}
 	
 	
 	//彻底删除
 	function delRealGoods(grid) {
    	var rows = grid.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的商品彻底删除吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "goodsIds=";
    					for ( var i = 0; i < rows.length; i++) {
    						ajaxparams += rows[i].get('goodsId');
    						if (i < rows.length - 1) 
    							ajaxparams += ",";
    					}
    					
    					// 发送请求
    					grid.el.mask("正在删除商品...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.delRealGoods,
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
    		Ext.Msg.alert("提示", "请选择商品");
    	}
 	}
 	
 	
 	//清空已删除删除
 	function clearDeledGoods(grid) {
    	var count = grid.getStore().count();
    	if (count > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定清空已删除的商品吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {    					
    					// 发送请求
    					grid.el.mask("正在清除商品...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.clearDeledGoods,
    						method : "POST",
    						waitMsg : "正在清除...",
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
    		Ext.Msg.alert("提示", "没有可清除的商品");
    	}
 	}
 	
 	
 	//上架
 	function upShelfGoods(grid) {
    	var rows = grid.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的商品上架吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "goodsIds=";
    					for ( var i = 0; i < rows.length; i++) {
    						ajaxparams += rows[i].get('goodsId');
    						if (i < rows.length - 1) 
    							ajaxparams += ",";
    					}
    					
    					// 发送请求
    					grid.el.mask("正在上架商品...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.upShelfGoods,
    						params : ajaxparams,
    						method : "POST",
    						waitMsg : "正在上架...",
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
    		Ext.Msg.alert("提示", "请选择商品");
    	}
 	}
 	
 	
 	//下架
 	function offShelfGoods(grid){
    	var rows = grid.selModel.getSelection();
    	if (rows.length > 0) {
    		Ext.Msg.show({
    			title : '提示',
    			msg : '确定要将选择的商品下架吗？',
    			buttons : Ext.Msg.OKCANCEL,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn, text) {
    				if (btn == 'ok') {
    					// 构建Ajax参数
    					var ajaxparams = "goodsIds=";
    					for ( var i = 0; i < rows.length; i++) {
    						ajaxparams += rows[i].get('goodsId');
    						if (i < rows.length - 1) 
    							ajaxparams += ",";
    					}
    					
    					// 发送请求
    					grid.el.mask("正在下架商品...", 'x-mask-loading');
    					Ext.Ajax.request({
    						url : Url.offShelfGoods,
    						params : ajaxparams,
    						method : "POST",
    						waitMsg : "正在下架...",
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
    		Ext.Msg.alert("提示", "请选择商品");
    	}
 	}
 	
 	
 	//修改价格
 	function modifyPrice(grid, record, value){
		// 构建Ajax参数
		var ajaxparams = "goodsId="+record.data.goodsId+"&shopPrice="+value;
 					
		// 发送请求
		grid.el.mask("正在修改...", 'x-mask-loading');
		Ext.Ajax.request({
			url : Url.modifyPrice,
			params : ajaxparams,
			method : "POST",
			waitMsg : "正在修改...",
			success : function(response, options) {
				grid.el.unmask();
				var json = Ext.JSON.decode(response.responseText);
				if (json.success) {
					record.commit();
				}else{				
					record.reject();
					Ext.Msg.alert("提示", json.msg);
				}
			},
			failure : function(response, options) {
				grid.el.unmask();				
				record.reject();
				Ext.ajaxFailure(response, options);
			}
		});
 	}
 	
 	
 	//修改库存
 	function modifyInventory(grid, record, value){
		// 构建Ajax参数
		var ajaxparams = "goodsId="+record.data.goodsId+"&goodsNumber="+value;
 					
		// 发送请求
		grid.el.mask("正在修改...", 'x-mask-loading');
		Ext.Ajax.request({
			url : Url.modifyInventory,
			params : ajaxparams,
			method : "POST",
			waitMsg : "正在修改...",
			success : function(response, options) {
				grid.el.unmask();
				var json = Ext.JSON.decode(response.responseText);
				if (json.success) {
					record.commit();
				}else{					
					record.reject();
					Ext.Msg.alert("提示", json.msg);
				}
			},
			failure : function(response, options) {
				grid.el.unmask();				
				record.reject();
				Ext.ajaxFailure(response, options);
			}
		});
 	}
 	
	
	//搜索出售中的商品
	Ext.getCmp("btnSearchSelling").handler();
    
});

</script>
</head>
<body>
</body>
</html>