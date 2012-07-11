package com.baishop.framework.utils;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class PropertiesUtil {
	private static PropertiesUtil instance = new PropertiesUtil();

	private PropertiesUtil() {
	}

	public static PropertiesUtil get() {
		return instance;
	}
	public String getString(String key, Properties properties) {
		return getString(key, properties, "");
	}
	public String getString(String key, Properties properties,
			String defaultValue) {
		String rs = properties.getProperty(key);
		if(StringUtils.isBlank(rs))
			return defaultValue;
		return rs;
	}

	public int getInt(String key, Properties properties) {
		return getInt(key, properties, 0);
	}
	public int getInt(String key, Properties properties,
			int defaultValue) {
		String value = (String) this.getString(key, properties, null);
		int v;
		try {
			v = value!=null? Integer.parseInt(value):defaultValue;
		} catch (NumberFormatException e) {
			v = defaultValue;
		}
		return v;
	}
	public long getLong(String key, Properties properties) {
		return getLong(key, properties, 0);
	}
	public long getLong(String key, Properties properties,
			long defaultValue) {
		String value = (String) this.getString(key, properties, null);
		long v;
		try {
			v = value!=null? Long.parseLong(value):defaultValue;
		} catch (NumberFormatException e) {
			v = defaultValue;
		}
		return v;
	}
	public boolean getBoolean(String key, Properties properties){
		return getBoolean(key, properties, false);
	}
	public boolean getBoolean(String key, Properties properties,
			boolean defaultValue) {
		String value = (String) this.getString(key, properties, null);
		if(value == null)
			return defaultValue;
		boolean v = value.equalsIgnoreCase("true")? true:false;
		return v;
	}

	public String[] getStringAsArray(String key, Properties properties, String separator){
		String value = this.getString(key, properties);
		String[] values = StringUtils.splitByWholeSeparator(value, separator);
		for(int i=0; i< values.length; i++){
			values[i] = StringUtils.trim(values[i]);
		}
		return values;
	}
}
