Ext.define('Ext.kadang.Enums', {
	extend: 'Ext.data.Store',
	alias: 'widget.enums', 
	
	fields: ['enumsKey', 'enumsValue'],
	
	getValue: function(key){
		var v = null;
		this.each(function(record){
    		if(record.get('enumsKey')==key){
				v = record.get('enumsValue');
    			return false;
    		}
		});
		return v;
	},
	getKey: function(value){
		var key = null;
		this.each(function(record){
    		if(record.get('enumsValue')==value){
    			key = record.get('enumsKey');
    			return false;
    		}
		});
		return key;
	}
});