/**
 * Main application definition for Baishop app.
 *
 * We define our own Application class because this way we can also
 * easily define the dependencies.
 */
Ext.define('Baishop.Application', {
    extend: 'Ext.app.Application',

    name: 'Baishop',
    appFolder: Ext.Loader.config.paths.Baishop,

    requires: [
        'Baishop.Favorites',
        'Baishop.History',
        'Baishop.Settings'
    ],

    controllers: [
        'Classes'
    ],

    autoCreateViewport: true,

    launch: function() {
        Baishop.App = this;
        Baishop.Favorites.init();
        Baishop.History.init();
        Baishop.Settings.init();

        // When google analytics event tracking script present on page
        if (Baishop.initEventTracking) {
            Baishop.initEventTracking();
        }
    }
});
