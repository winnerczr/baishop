/**
 * Browser history management using Ext.util.History.
 */
Ext.define("Baishop.History", {
    extend: 'Baishop.LocalStore',
    storeName: 'History',
    singleton: true,

    // Maximum number of items to keep in history store
    maxHistoryLength: 25,

    /**
     * Initializes history management.
     */
    init: function() {
        Ext.util.History.init(function() {
            this.navigate(Ext.util.History.getToken());
        }, this);
        Ext.util.History.on("change", this.navigate, this);
        this.callParent();
    },

    // Parses current URL and navigates to the page
    navigate: function(token) {
        if (this.ignoreChange) {
            this.ignoreChange = false;
            return;
        }

        if (token && token != "") {
            Baishop.App.getController('Classes').loadClass(token, true);
        }
        else {
            Ext.getCmp('container').layout.setActiveItem(0);
        }
    },


    /**
     * Adds URL to history
     *
     * @param {String} token  the part of URL after #
     */
    push: function(token) {
        this.ignoreChange = true;
        Ext.util.History.add(token);

        // Add class name to history store if it's not there already
        var cls = token;//.replace(/\//, '');
        if (cls && cls != "") {
            // When class already in history remove it and add again.
            // This way the most recently visited items will always be at the top.
            var oldIndex = this.store.findExact('cls', cls);
            if (oldIndex > -1) {
                this.store.removeAt(oldIndex);
            }

            // Add new item at the beginning
            this.store.insert(0, {cls: cls});

            // Remove items from the end of history if there are too many
            while (this.store.getAt(this.maxHistoryLength)) {
                this.store.removeAt(this.maxHistoryLength);
            }
            this.syncStore();
        }
    },

    /**
     * Removes class from History store
     *
     * @param {String} cls
     */
    removeClass: function(cls) {
        var index = this.store.findExact('cls', cls);
        this.store.removeAt(index);
        this.syncStore();
    }
});
