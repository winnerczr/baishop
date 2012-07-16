/**
 * Listeners should be defined here instead of in the view classes
 */
Ext.define('Baishop.controller.Classes', {
    extend: 'Ext.app.Controller',

    requires: [
        'Baishop.History',
        'Baishop.Syntax',
        'Baishop.view.cls.Overview'
    ],

    stores: [
        'Favorites',
        'History',
        'Settings'
    ],

    models: [
        'Favorite',
        'History',
        'Setting'
    ],

    refs: [
        {
            ref: 'header',
            selector: 'classheader'
        },
        {
            ref: 'tabPanel',
            selector: 'classtabpanel'
        },
        {
            ref: 'tree',
            selector: 'classtree'
        }
    ],

    init: function() {
        this.addEvents(
            /**
             * @event showMember
             * Fired after class member scrolled to view. Used for analytics event tracking.
             * @param {String} cls  name of the class.
             * @param {String} anchor  name of the member in form type-name like "method-bind".
             */
            "showMember"
        );

        Ext.getBody().addListener('click', function(cmp, el) {
            this.loadClass(el.rel);
        }, this, {
            preventDefault: true,
            delegate: '.docClass'
        });

        this.control({
            'classtree': {
                // Can't simply assign the loadClass function as event
                // handler, because an extra event options object is
                // appended to the event arguments, which we don't
                // want to give to the loadClass, as this would render
                // the noHistory parameter to true.
                classclick: function(cls) {
                    this.loadClass(cls);
                }
            },

            'classoverview': {
                afterrender: function(cmp) {
                    // Expand member when clicked
                    cmp.el.addListener('click', function(cmp, el) {
                        Ext.get(Ext.get(el).up('.member')).toggleCls('open');
                    }, this, {
                        preventDefault: true,
                        delegate: '.expandable'
                    });

                    // Do nothing when clicking on not-expandable items
                    cmp.el.addListener('click', Ext.emptyFn, this, {
                        preventDefault: true,
                        delegate: '.not-expandable'
                    });
                }
            }
        });
    },
    
  

    /**
     * Loads class.
     *
     * @param {String} clsUrl  name of the class + optionally name of the method, separated with dash.
     * @param {Boolean} noHistory  true to disable adding entry to browser history
     */
    loadClass: function(clsUrl, noHistory) {
    	var url = this.getUrl(clsUrl);
    	if(url==="")
    		return;
    	
		if(url.lastIndexOf("_blank")==url.length-"_blank".length){
			window.open(url.replace(/\s.*/, ""),"_blank","");
		} 
		else if(url.lastIndexOf("_self")==url.length-"_seft".length){
			document.location.href = url.replace(/\s.*/, "");
		} 
		else {
	        Ext.getCmp('container').layout.setActiveItem(1);
	        Ext.get("iframe-content").dom.src = url.replace(/\s.*/, "");
	        
	        if (!noHistory) {
	            Baishop.History.push(clsUrl);
	        }
		}
    },

    /**
     * Returns base URL used for making AJAX requests.
     * @return {String} URL
     */
    getUrl: function(clsUrl) {
    	var url = Baishop.leafModules[clsUrl];
    	if(!url){
    		return ""; //Baishop.App.appFolder + "/NotFound.htm";
    	}
    	
    	if(url.indexOf("http://")==0){
			return url;
    	} else
    	if(url.indexOf("/")==0){
	        return url;
		}else{
			return "";
		}
    }

});
