package com.baishop.framework.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;


/**
 * Properties扩展类
 * @author Linpn
 */
public class PropsConf extends Properties {
	
	private static final long serialVersionUID = 7618753756577836359L;
	
    /**
     * Creates an empty property list with no default values.
     */
    public PropsConf() {
    	super(null);
    }

    /**
     * Creates an empty property list with the specified defaults.
     *
     * @param   defaults   the defaults.
     */
    public PropsConf(Properties defaults) {
    	super(defaults);
    }
    
    
    /**
     * set an empty property list with the specified defaults.
     *
     * @param   defaults   the defaults.
     */
    public void setProperties(Properties defaults){
    	this.defaults = defaults;
    }
    
    
    
    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns the
     * default value argument if the property is not found.
     *
     * @param   key            the hashtable key.
     * @param   defaultValue   a default value.
     * @param   charsetName   a charsetName.
     *
     * @return  the value in this property list with the specified key value.
     * @see     #setProperty
     * @see     #defaults
     */
    public String getProperty(String key, String defaultValue, String charsetName) {
		try {
			String value = super.getProperty(key, defaultValue);
			value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
			return value;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); 
		}
    }

}
