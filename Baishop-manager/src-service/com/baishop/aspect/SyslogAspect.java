package com.baishop.aspect;

import java.io.Serializable;
import java.lang.reflect.Field;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.baishop.entity.ass.Params;
import com.baishop.entity.ass.Syslog;
import com.baishop.framework.json.JsonConfigGlobal;
import com.baishop.service.ass.ParamsService;
import com.baishop.service.ass.SyslogService;

/**
 * Aop日志切面
 * @author Linpn
 */
public class SyslogAspect implements Serializable {
	
	private static final long serialVersionUID = -3575826094887922083L;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private ParamsService paramsService;
	@Autowired
	private SyslogService syslogService;
	
	//日志过滤器缓存
	private String[] filterCache;
	
	
	/**
	 * 日志切面方法 
	 * @param joinpoint
	 */
	public void loggerJoinPoint(final JoinPoint joinpoint) {
		//通过线程池打印日志
		threadPoolTaskExecutor.execute(new Runnable(){
			@Override
			public void run() {
				try{
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
						
						syslogService.logger(syslog);
						Logger logger = getLog4j(joinpoint.getTarget());
						if(logger!=null)
							logger.debug("日志：" + syslog.getDescription());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/**
	 * 清除过滤缓存
	 */
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
	 * 过滤算法
	 * @param filter 过滤关键字
	 * @param signature 接口方法签名
	 * @return
	 */
	protected boolean matches(String filter, String signature){
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
	
	
	/**
	 * 获取对象自身的log4j属性
	 * @param target
	 * @return
	 */
	private Logger getLog4j(Object target){		
		try{
			Field field = target.getClass().getField("logger");
			Logger logger = (Logger)field.get(target);
			return logger;
		}catch(Exception e){
			return null;
		}
	}
	
}
