/**
 * Key-value pairs of Baishop app settings.
 */
Ext.define('Baishop.model.Setting', {
    fields: ['id', 'key', 'value'],
    extend: 'Ext.data.Model',
    proxy: {
        type: ('localStorage' in window && window['localStorage'] !== null) ? 'localstorage' : 'memory',
        id  : 'baishop-settings'
    }
});
