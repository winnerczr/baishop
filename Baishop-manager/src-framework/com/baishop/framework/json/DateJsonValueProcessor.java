package com.baishop.framework.json;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import com.baishop.framework.utils.DateUtils;

/**
 * 
 * Json日期转换处理器
 * @author shawn.du
 *
 */
public class DateJsonValueProcessor implements JsonValueProcessor {

	private static final String format = DateUtils.DATETIME_FORMAT;  
	
	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value, jsonConfig);  
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return process(value, jsonConfig);  
	}
	
	private Object process( Object value, JsonConfig jsonConfig ) {  
        if (value instanceof Date) {  
            String str = new SimpleDateFormat(format).format((Date) value);  
            return str;  
        }  
        return value == null ? null : value.toString();  
    }  

}
