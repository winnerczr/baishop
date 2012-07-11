package com.baishop.framework.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartRequest;

import com.baishop.framework.web.binder.DataBinderRequest;


public interface HttpServletExtendRequest extends HttpServletRequest,
		MultipartRequest, DataBinderRequest {
	
	/**
	 * 获取原生的HttpServletRequest
	 * @return 返回HttpServletRequest对象
	 */
	public HttpServletRequest getRequest();
	
	/**
	 * 获取参数,并去空格
	 * @param name 参数名
	 * @return 返回参数值
	 */
	public String getParameter(String name);
	
	/**
	 * 获取参数,并去空格
	 * @param name 参数名
	 * @param charsetName 编码，如UTF-8
	 * @return 返回参数值
	 */
	public String getParameter(String name, String charsetName);
	
	/**
	 * 获取参数,并去空格
	 * @param name 参数名
	 * @param charsetName 编码，如UTF-8
	 * @param defaultValue 默认值
	 * @return 返回参数值
	 */
	public String getParameter(String name, String charsetName, String defaultValue);

	/**
	 * 获取boolean型参数
	 * @param name 参数名
	 * @return 返回参数值
	 */
	public Boolean getBooleanParameter(String name);
	
	/**
	 * 获取boolean型参数
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 返回参数值
	 */
	public Boolean getBooleanParameter(String name, Boolean defaultValue);
	
	/**
	 * 获取Integer型参数
	 * @param name 参数名
	 * @return 返回参数值
	 */
	public Integer getIntParameter(String name);
	
	/**
	 * 获取Integer型参数
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 返回参数值
	 */
	public Integer getIntParameter(String name, Integer defaultValue);
	
	/**
	 * 获取long型参数
	 * @param name 参数名
	 * @return 返回参数值
	 */
	public Long getLongParameter(String name);
	
	/**
	 * 获取long型参数
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 返回参数值
	 */
	public Long getLongParameter(String name, Long defaultValue);
	
	/**
	 * 获取float型参数
	 * @param name 参数名
	 * @return 返回参数值
	 */
	public Float getFloatParameter(String name);
	
	/**
	 * 获取float型参数
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 返回参数值
	 */
	public Float getFloatParameter(String name, Float defaultValue);
	
	/**
	 * 获取double型参数
	 * @param name 参数名
	 * @return 返回参数值
	 */
	public Double getDoubleParameter(String name);
	
	/**
	 * 获取double型参数
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 返回参数值
	 */
	public Double getDoubleParameter(String name, Double defaultValue);
	
	/**
	 * 获取Date型参数
	 * @param name 参数名
	 * @return 返回参数值
	 */
	public Date getDateParameter(String name);

	/**
	 * 获取Date型参数
	 * @param name 参数名
	 * @param pattern 日期格式
	 * @return 返回参数值
	 */
	public Date getDateParameter(String name, String pattern);
	
	/**
	 * 获取Date型参数
	 * @param name 参数名
	 * @param pattern 日期格式
	 * @param defaultValue 默认值
	 * @return 返回参数值
	 */
	public Date getDateParameter(String name, String pattern, Date defaultValue);
	
	
}
