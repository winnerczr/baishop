/**
 * The main viewport, split in to a west and center region.
 * The North region should also be shown by default in the packaged
 * (non-live) version of the baishop. TODO: close button on north region.
 */
Ext.define('Baishop.view.Viewport', {
    extend: 'Ext.container.Viewport',
    requires: [
        'Baishop.view.cls.Container',
        'Baishop.view.index.Container',
        'Baishop.view.tree.Tree',
        'Baishop.History'
    ],

    id: 'viewport',
    layout: 'border',
    defaults: { xtype: 'container' },

    initComponent: function() {
        this.items = [

            {
                region:'west',
                width: 220,
                id: 'west-region-container',
                padding: '5 0 20 20',
                layout: 'vbox',
                defaults: {
                    xtype: 'container',
                    width: "100%"
                },
                items: [
					{
					    xtype: 'button',
					    cls: 'logo',
					    height: 60,
					    margin: '0 0 10 0',
					    width: 200,
					    border: 0,
					    ui: 'hmm',
					    listeners: {
					        click: function() {
					            Ext.getCmp('container').layout.setActiveItem(0);
					        }
					    }
					},
                    /*{
                        xtype: 'hovermenubutton',
                        cls: 'logo',
                        height: 60,
                        margin: '0 0 10 0',
                        width: 200,
                        border: 0,
                        ui: 'hmm',
                        menuCfg: {
                            margin: '41 0 0 10',
                            width: 200
                        },
                        store: Ext.create('Ext.data.Store', {
							fields: ['id', 'cls'],
							data: Baishop.listSystems
                	    }),
                        listeners: {
                            click: function() {
                                Ext.getCmp('container').layout.setActiveItem(0);
                            }
                        }
                    },*/
                    {
                        flex: 1,
                        xtype: 'classtree',
                        root: Baishop.treeModules
                    }
                ]
            },
            {
                region: 'center',
                id: 'center-container',
                layout: 'fit',
                minWidth: 800,
                items: {
                    id: 'container',
                    xtype: 'container',
                    layout: 'card',
                    padding: '10',
                    cls: 'container',
                    items: [
                        {
                            xtype: 'container',
                            contentEl: 'home-space'
                        },{
                            xtype: 'container',
                            html: "<iframe id='iframe-content' frameborder='0' width='100%' height='100%' src='about:blank'></iframe>"
                        }
                    ]
                }
            }
        ];

        this.callParent(arguments);
  	    Ext.get('home-space').setVisible(true);
    }
});
