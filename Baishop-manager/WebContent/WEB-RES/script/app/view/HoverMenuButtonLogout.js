/**
 * Toolbar button with menu that appears when hovered over.
 */
Ext.define('Baishop.view.HoverMenuButtonLogout', {
    extend: 'Ext.toolbar.TextItem',
    alias: 'widget.hovermenubuttonLogout',
    componentCls: "hover-menu-button",
    requires: [
        'Baishop.view.HoverMenu'
    ],

    /**
     * @cfg {Ext.data.Store} store
     * Store with menu items (required).
     */

    /**
     * @cfg {Object} menuCfg
     * Additional config options for {@link Baishop.view.HoverMenu}
     */
    menuCfg: {},

    /**
     * @cfg {Boolean} showCount
     * True to show small number in button indicating the number of items in menu.
     * Defaults to false.
     */
    showCount: false,

    statics: {
        // Global list of all menus.
        // So we can hide all other menus while showing a specific one.
        menus: []
    },

    initComponent: function() {
        this.addEvents(
            /**
             * @event click
             * Fired when button clicked.
             */
            "click",
            /**
             * @event closeclick
             * Fired when close link in menu clicked.
             * @param {String} name  Name of the class and or member that was closed.
             * For example "Ext.Ajax" or "Ext.Ajax-method-request".
             */
            "closeclick"
        );

        // Append links count to button text, update it when store filtered
        if (this.showCount) {
            this.initialText = this.text;
            this.text += ' <sup>' + this.store.getCount() + '</sup>';
            this.store.on("datachanged", function() {
                this.setText(this.initialText + ' <sup>' + this.store.getCount() + '</sup>');
            }, this);
        }

        this.menu = Ext.create('Baishop.view.HoverMenu', Ext.apply({
            store: this.store
        }, this.menuCfg));

        this.callParent(arguments);
    },

    onRender: function() {
        this.callParent(arguments);

        this.renderMenu();

        this.getEl().on({
            click: function() {
                this.fireEvent("click");
            },
            mouseover: function() {
                // hide other menus
                
            },
            mouseout: this.deferHideMenu,
            scope: this
        });

        this.menu.getEl().on({
            mouseover: function() {
               // clearTimeout(this.hideTimeout);
            },
            mouseout: this.deferHideMenu,
            scope: this
        });

    },

    onDestroy: function() {
        // clean up DOM
        this.menu.destroy();
        // remove from global menu list
        Ext.Array.remove(Baishop.view.HoverMenuButton.menus, this.menu);

        this.callParent(arguments);
    },

    renderMenu: function() {
        this.menu.getEl().setVisibilityMode(Ext.core.Element.DISPLAY);
        this.menu.hide();

        this.menu.getEl().addListener('click', function(e) {
            if (e.getTarget(".close")) {
                this.fireEvent("closeclick", e.getTarget().rel);
            } else {
                this.menu.hide();
            }
            e.preventDefault();
        }, this);

        Baishop.view.HoverMenuButton.menus.push(this.menu);
    },

    deferHideMenu: function() {
        this.hideTimeout = Ext.Function.defer(function() {
            this.menu.hide();
        }, 200, this);
    },

    /**
     * Returns the store used by menu.
     * @return {Ext.data.Store}
     */
    getStore: function() {
        return this.store;
    }

});
