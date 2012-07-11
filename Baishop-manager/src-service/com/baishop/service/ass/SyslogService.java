package com.baishop.service.ass;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

import com.baishop.entity.ass.Syslog;

/**
 * 日志服务接口
 * @author Linpn
 */
public interface SyslogService {
	
	/**
	 * 获取日志
	 * @param params 查询参数
	 * @return
	 */
	public Syslog getSyslog(int id);
	
	/**
	 * 获取日志列表
	 * @param params 查询参数
	 * @param sorters 记录的排序，如sorters.put("id","desc")，该参数如果为空表示按默认排序
	 * @param start 查询的起始记录,可为null
	 * @param limit 查询的总记录数, 如果值为-1表示查询到最后,可为null
	 * @return
	 */
	public List<Syslog> getSyslogList(Map<String,Object> params, Map<String,String> sorters, Long start, Long limit);

	/**
	 * 获取日志总数
	 * @param params 查询参数
	 * @return
	 */
	public long getSyslogCount(Map<String,Object> params);
	
	/**
	 * 添加日志
	 * @param syslog 日志对象
	 */
	public void logger(final Syslog syslog);

	/**
	 * 添加日志
	 * @param syslog 日志对象
	 * @param logger log4j对象
	 */
	public void logger(Syslog syslog, Logger logger);
	
	/**
	 * 添加日志Aop切面方法
	 * @param joinpoint
	 */
	public void loggerJoinPoint(final JoinPoint joinpoint);
	
	/**
	 * 清空过滤器缓存
	 */
	public void clearFilterCache();

}
