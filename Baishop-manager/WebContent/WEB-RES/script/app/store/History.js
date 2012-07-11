/**
 * History Store
 */
Ext.define('Baishop.store.History', {
    extend: 'Ext.data.Store',
    model: 'Baishop.model.History',
    // Sort history with latest on top
    sorters: [
        { property: 'id', direction: 'DESC' }
    ]
});
