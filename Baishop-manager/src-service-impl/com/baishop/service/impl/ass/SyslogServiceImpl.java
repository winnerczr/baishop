package com.baishop.service.impl.ass;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.baishop.entity.ass.Params;
import com.baishop.entity.ass.Syslog;
import com.baishop.framework.exception.ServiceException;
import com.baishop.framework.json.JsonConfigGlobal;
import com.baishop.service.BaseService;
import com.baishop.service.ass.ParamsService;
import com.baishop.service.ass.SyslogService;

public class SyslogServiceImpl extends BaseService implements SyslogService {
	
	@Autowired
	private SqlMapClientTemplate sqlMapClientSyslog;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private ParamsService paramsService;
	
	//日志过滤器缓存
	private String[] filterCache;
	
	
	@Override
	public Syslog getSyslog(int id) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("id", id);
			
			Syslog syslog = (Syslog)sqlMapClientSyslog.queryForObject("Syslog.getSyslog", params);
			return syslog;			
		}catch(Exception e){
			throw new ServiceException(903001, e);
		}
	}
	
	@Override
	public List<Syslog> getSyslogList(Map<String,Object> params, Map<String,String> sorters, Long start, Long limit) {
		try{
			if(params==null)
				params = new HashMap<String,Object>();
			
			params.put("sort", this.getDbSort(sorters));
			params.put("start", start);
			params.put("limit", limit);
			
			@SuppressWarnings("unchecked")
			List<Syslog> list = sqlMapClientSyslog.queryForList("Syslog.getSyslog", params);
			return list;			
		}catch(Exception e){
			throw new ServiceException(903001, e);
		}
	}

	@Override
	public long getSyslogCount(Map<String,Object> params) {
		try{
			long count = (Long)sqlMapClientSyslog.queryForObject("Syslog.getSyslogCount", params);			
			return count;		
		}catch(Exception e){
			throw new ServiceException(903001, e);
		}
	}

	@Override
	public void logger(Syslog syslog) {
		this.logger(syslog, this.logger);
	}


	protected void logger(Syslog syslog, Logger logger) {
		try{
			sqlMapClientSyslog.insert("Syslog.addSyslog", syslog);
			
			if(logger!=null)
				logger.debug("日志：" + syslog.getDescription());
			
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void loggerJoinPoint(final JoinPoint joinpoint) {
		//通过线程池打印日志
		threadPoolTaskExecutor.execute(new Runnable(){
			@Override
			public void run() {
				//接口签名
				Signature signature = joinpoint.getSignature();
				
				//判断是否过滤日志
				if(!filter(signature.toString())){
					//日志对象
					Syslog syslog = new Syslog();					
					String args = JSONArray.fromObject(joinpoint.getArgs(), JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");
					
					syslog.setUser("admin");
					syslog.setSource((byte)1);
					syslog.setType(signature.getDeclaringTypeName());
					syslog.setMethod(signature.toString().replaceAll(signature.getDeclaringTypeName()+".", ""));
					syslog.setArgs(args.length()<=255?args:args.substring(0, 255));
					syslog.setDescription(signature.toString());
					
					logger(syslog, getLog4j(joinpoint.getTarget()));
				}
			}			
		});
	}
	
	@Override
	public void clearFilterCache(){
		this.filterCache = null;
		System.gc();
	}
	
	
	/**
	 * 日志过滤器
	 * @param signature 接口方法签名 
	 * 如:Params com.baishop.service.ParamsService.getParams(String)
	 * @return 返回是否被过滤, true表示被过滤，false表示没被过滤
	 */
	protected boolean filter(String signature){
		//从数据库中获取过滤数据
		if(filterCache==null || filterCache.length<=0){
			Params params = paramsService.getParams("logger_filter");
			filterCache = params.getParamsValue().split("\n");
		}
		
		//判断是否被过滤
		if(filterCache!=null){
			//过滤器为空
			if(filterCache.length==1 && filterCache[0].equals(""))
				return false;
			
			//判断是否被过滤
			for(String filter : filterCache){
				if(this.matches(filter, signature))
					return true;
			}
		}
			
		return false;
	}
	
	
	/**
	 * 获取对象自身的log4j属性
	 * @param target
	 * @return
	 */
	private Logger getLog4j(Object target){		
		try{
			Logger logger = null;
			if(target instanceof BaseService){
				Field field = target.getClass().getField("logger");
				logger = (Logger)field.get(target);
			}	
			return logger;
		}catch(Exception e){
			return null;
		}
	}

	
	/**
	 * 过滤算法
	 * @param filter 过滤关键字
	 * @param signature 接口方法签名
	 * @return
	 */
	private boolean matches(String filter, String signature){
		boolean reval = true;
		
		filter = filter.trim();
		signature = signature.trim();
		
		String[] keys = filter.split("\\*");
		String _sign = signature;
		int i=0;
		for(String key : keys){
			_sign = _sign.substring(i);
			if(!key.equals("") && !_sign.equals("")){
				i = _sign.indexOf(key);
				if(i>-1){
					i +=key.length();
				}else{
					reval = false;
					break;
				}
			}
		}
				
		return reval;
	}
	
}
