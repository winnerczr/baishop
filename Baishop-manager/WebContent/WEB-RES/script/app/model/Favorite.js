/**
 * Favorite classes
 */
Ext.define('Baishop.model.Favorite', {
    fields: ['id', 'cls'],
    extend: 'Ext.data.Model',
    proxy: {
        type: ('localStorage' in window && window['localStorage'] !== null) ? 'localstorage' : 'memory',
        id  : 'baishop-favorites'
    }
});
