package com.baishop.framework.web.binder;


public interface DataBinderRequest {

	/**
	 * 获取request绑定后的对象
	 * @param clazz 要绑定的类型
	 * @return 返回绑定后的对象实例
	 */
	<T> T getBindObject(Class<T> clazz);
	
	/**
	 * 获取request绑定后的对象
	 * @param clazz 要绑定的类型
	 * @param objectName 要绑定的参数前缀，如要绑定user.username参数，则传user字符串进去
	 * @return 返回绑定后的对象实例
	 */
	<T> T getBindObject(Class<T> clazz, String objectName);
		
}
