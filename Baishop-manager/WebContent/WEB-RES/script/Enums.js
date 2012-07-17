Ext.define('Ext.kadang.Enums', {
	extend: 'Ext.data.Store',
	alias: 'widget.enums', 
	
	fields: ['enumsName', 'enumsText'],
	
	getValue: function(key){
		var v = null;
		this.each(function(record){
    		if(record.get('enumsName')==key){
				v = record.get('enumsText');
    			return false;
    		}
		});
		return v;
	},
	getKey: function(value){
		var key = null;
		this.each(function(record){
    		if(record.get('enumsText')==value){
    			key = record.get('enumsName');
    			return false;
    		}
		});
		return key;
	}
});