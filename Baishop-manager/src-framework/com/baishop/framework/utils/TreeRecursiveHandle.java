package com.baishop.framework.utils;

import java.util.List;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public interface TreeRecursiveHandle<T> {
	/**
	 * 递归函数
	 * @param list
	 * @param treeNode
	 * @throws JSONException
	 */
	void recursive(List<T> list, JSONObject treeNode) throws JSONException;

}
