/**
 * Store for keeping search results.
 */
Ext.define('Baishop.store.Search', {
    extend: 'Ext.data.Store',

    fields: ['cls', 'member', 'type', 'xtypes'],
    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        }
    }
});