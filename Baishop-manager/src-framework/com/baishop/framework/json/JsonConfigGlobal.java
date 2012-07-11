package com.baishop.framework.json;

import net.sf.json.JsonConfig;

public class JsonConfigGlobal {

	public static final JsonConfig jsonConfig = new JsonConfig();
	
	static{
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor());  
	}
}
