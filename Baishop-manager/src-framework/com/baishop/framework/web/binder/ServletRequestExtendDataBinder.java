package com.baishop.framework.web.binder;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.multipart.MultipartRequest;

public class ServletRequestExtendDataBinder extends ServletRequestDataBinder {

	public ServletRequestExtendDataBinder(Object target) {
		super(target);
	}
	
	
	public void bind(ServletRequest request, String objectName) {
		MutablePropertyValues _mpvs = new ServletRequestParameterPropertyValues(request);
		MutablePropertyValues mpvs = new MutablePropertyValues();
		
		PropertyValue[] propertyValues = _mpvs.getPropertyValues();
		objectName += ".";
		
		for(PropertyValue propertyValue : propertyValues){
			String name = propertyValue.getName();
			Object value = propertyValue.getValue();
			int index = name.indexOf(objectName);
			
			if(index<0 || index>0){
				_mpvs.removePropertyValue(name);
			}else{
				 mpvs.addPropertyValue(name.substring(objectName.length()), value);
			}
		}		
		
		if (request instanceof MultipartRequest) {
			MultipartRequest multipartRequest = (MultipartRequest) request;
			bindMultipart(multipartRequest.getMultiFileMap(), mpvs);
		}
		
		doBind(mpvs);
	}


}
