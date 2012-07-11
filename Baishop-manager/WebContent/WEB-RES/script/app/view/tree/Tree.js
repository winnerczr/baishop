/**
 * The class tree
 */
Ext.define('Baishop.view.tree.Tree', {
    extend: 'Ext.tree.Panel',
    alias : 'widget.classtree',
    requires: [
		'Baishop.view.HoverMenuButtonLogout',
        'Baishop.view.HoverMenuButton',
        'Baishop.Favorites',
        'Baishop.History'
    ],

    cls: 'class-tree iScroll',
    folderSort: false,
    useArrows: true,
    rootVisible: false,

    border: false,
    bodyBorder: false,

    initComponent: function() {
        this.addEvents(
            /**
             * @event
             * Fired when class in tree was clicked on and needs to be loaded.
             * @param {String} cls  name of the class.
             */
            "classclick"
        );

        // Expand the main tree
        this.root.expanded = true;
        if(this.root.children &&  this.root.children.length>0)
        	this.root.children[0].expanded = true;

        this.on("itemclick", this.onItemClick, this);

        this.dockedItems = [
            {
                xtype: 'container',
                layout: 'hbox',
                dock: 'top',
                margin: '0 0 15 0',
                items: [
                    {
                        xtype: 'hovermenubutton',
                        cls: 'icon-fav sidebar',
                        text: '收藏',
                        menuCfg: {
                            cls: 'sidebar',
                            emptyText: '没有收藏',
                            showCloseButtons: true
                        },
                        store: Ext.getStore('Favorites'),
                        listeners: {
                            closeclick: function(cls) {
                                Baishop.Favorites.remove(cls);
                            }
                        }
                    },
                    {
                        xtype: 'hovermenubutton',
                        cls: 'icon-hist sidebar',
                        text: '历史',
                        menuCfg: {
                            cls: 'sidebar',
                            emptyText: '没有历史',
                            showCloseButtons: true
                        },
                        store: Ext.getStore('History'),
                        listeners: {
                            closeclick: function(cls) {
                                Baishop.History.removeClass(cls);
                            }
                        }
                    },
                    {
                        xtype: 'hovermenubuttonLogout',
                        cls: 'icon-logout sidebar',
                        text: '退出',
                        listeners: {
                            click: function(cls) {
                            	window.location.href="j_spring_security_logout";
                            }
                        }
                    }
                ]
            }
        ];

        this.callParent();
        
        // Add links for favoriting classes
        //
        // Wait for the Favorites to load, then wait for tree to render,
        // after which add the fav icons.
        //
        // Do all this after callParent, because the getRootNode() will work
        // after initComponent has run.
        Baishop.Favorites.setTree(this);
        Ext.getStore("Favorites").on("load", function() {
            this.rendered ? this.initFavIcons() : this.on("render", this.initFavIcons, this);
        }, this);
    },
    
    initFavIcons: function() {
        this.getRootNode().cascadeBy(this.addFavIcons, this);
    },

    addFavIcons: function(node) {
        if (node.get("leaf")) {
            var cls = node.raw.text;
            var show = Baishop.Favorites.has(cls) ? "show" : "";
            node.set("text", node.get("text") + Ext.String.format('<a rel="{0}" class="fav {1}"></a>', cls, show));
            node.commit();
        }
    },

    onItemClick: function(view, node, item, index, e) {
        var text = node.raw ? node.raw.text : node.data.text;

        if (text) {
            if (e.getTarget(".fav")) {
                var favEl = Ext.get(e.getTarget(".fav"));
                if (favEl.hasCls('show')) {
                    Baishop.Favorites.remove(text);
                }
                else {
                    Baishop.Favorites.add(text);
                }
            }
            else {
                this.fireEvent("classclick", text);
            }
        }
        else if (!node.isLeaf()) {
            if (node.isExpanded()) {
                node.collapse(false);
            }
            else {
                node.expand(false);
            }
        }
    },

    /**
     * Selects class node in tree by name.
     *
     * @param {String} cls
     */
    selectClass: function(cls) {
        var r = this.findRecordByClassName(cls);
        if (r) {
            this.getSelectionModel().select(r);
            r.bubble(function(n) {
                n.expand();
            });
        }
    },

    /**
     * Sets favorite status of class on or off.
     *
     * @param {String} cls  name of the class
     * @param {Boolean} enable  true to mark class as favorite.
     */
    setFavorite: function(cls, enable) {
        var r = this.findRecordByClassName(cls);
        if (r) {
            var show = enable ? "show" : "";
            r.set("text", r.get("text").replace(/class="fav *(show)?"/, 'class="fav '+show+'"'));
            r.commit();
        }
    },

    findRecordByClassName: function(cls) {
        return this.getRootNode().findChildBy(function(n) {
            return cls === n.raw.text;
        }, this, true);
    }
});
