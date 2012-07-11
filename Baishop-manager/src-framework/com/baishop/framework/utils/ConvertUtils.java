package com.baishop.framework.utils;

public class ConvertUtils {

	/**
	 * domain对象中的字段转成数据库字段。 如： paramsName 转成 params_name
	 * 
	 * @param domainField
	 *            domain对象中的字段
	 * @return 数据库字段
	 */
	public static String toDatabaseField(String domainField) {
		if (domainField == null)
			return "";

		StringBuilder sb = new StringBuilder();

		for (char c : domainField.toCharArray()) {
			if (c >= 'A' && c <= 'Z') {
				sb.append("_" + String.valueOf(c).toLowerCase());
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * String[] 转 long[]
	 * 
	 * @param arrString
	 *            String数组
	 * @return 返回 long数组
	 */
	public static long[] toArrLong(String[] arrString) {
		if(arrString==null || arrString.length==0 || arrString.length==1&&arrString[0].equals(""))
			return new long[0];
		
		long[] arrLong = new long[arrString.length];
		for (int i = 0; i < arrString.length; i++) {
			arrLong[i] = Long.parseLong(arrString[i]);
		}
		return arrLong;
	}

	/**
	 * String[] 转 int[]
	 * 
	 * @param arrString
	 *            String数组
	 * @return 返回int数组
	 */
	public static int[] toArrInteger(String[] arrString) {
		if(arrString==null || arrString.length==0 || arrString.length==1&&arrString[0].equals(""))
			return new int[0];
		
		int[] arrInteger = new int[arrString.length];
		for (int i = 0; i < arrString.length; i++) {
			arrInteger[i] = Integer.parseInt(arrString[i]);
		}
		return arrInteger;
	}

}
