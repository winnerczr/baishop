/**
 * The class documentation page. Consists of the header (class name) and tab panel.
 * TODO: Add framework version
 */
Ext.define('Baishop.view.cls.Container', {
    extend: 'Ext.container.Container',
    alias: 'widget.classcontainer',
    requires: [
        'Baishop.view.cls.Header',
        'Baishop.view.cls.TabPanel'
    ],

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function() {
        this.items = [
            Ext.create('Baishop.view.cls.Header'),
            Ext.create('Baishop.view.cls.TabPanel', {
                flex: 1
            })
        ];
        this.callParent(arguments);
    }
});
