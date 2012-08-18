package com.baishop.aspect;

import java.io.Serializable;

import net.sf.json.JSONArray;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.baishop.entity.ass.Params;
import com.baishop.entity.ass.Syslog;
import com.baishop.framework.json.JsonConfigGlobal;
import com.baishop.service.ass.ParamsService;
import com.baishop.service.ass.SyslogService;

/**
 * Service的调用日志和调用时间
 * @author Linpn
 */
public class ServiceAspect implements Serializable {
	
	private static final long serialVersionUID = -3575826094887922083L;
	
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private SyslogService syslogService;
	@Autowired
	private ParamsService paramsService;
	
	//日志过滤器缓存
	private String[] filterCache;
	
	
	/**
	 * 日志切面方法 
	 * @param joinpoint
	 * @throws Throwable 
	 */
	public Object around(final ProceedingJoinPoint joinpoint) 
			throws Throwable {

        //用 commons-lang 提供的 StopWatch 计时  
        final StopWatch clock = new StopWatch();  
        clock.start(); 
        final Object result = joinpoint.proceed();
        clock.stop(); 
        

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

						String sArgs = "", sResult = "";
						Object[] args = joinpoint.getArgs();
						
						if(args!=null && args.length>0)
							sArgs = JSONArray.fromObject(args, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");
						if(result!=null)
							sResult = JSONArray.fromObject(result, JsonConfigGlobal.jsonConfig).toString().replaceAll("\"", "");						
						
						syslog.setSource((byte)1);
						syslog.setSignature(signature.toString());
						syslog.setArgs(sArgs.length()<=255?sArgs:sArgs.substring(0, 255));
						syslog.setResult(sResult.length()<=255?sResult:sResult.substring(0, 255));
						syslog.setDescription("");
						syslog.setExecTime(clock.getTime());

						//输出访问日志
						if(logger.isInfoEnabled()){
							logger.info("Exec method ["+ syslog.getSignature() +", time: "+ syslog.getExecTime() +"ms]" );
						}
						if(logger.isDebugEnabled()){
							logger.debug("Method args ["+ syslog.getArgs() + "]");
							logger.debug("Method return ["+ syslog.getResult() +"]");
						}
						
						//插入数据库						
						syslogService.logger(syslog);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		return result;  
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
	
	
//	/**
//	 * 获取对象自身的log4j属性
//	 * @param target
//	 * @return
//	 */
//	private Logger getLog4j(Object target){		
//		try{
//			Field field = target.getClass().getField("logger");
//			Logger logger = (Logger)field.get(target);
//			return logger;
//		}catch(Exception e){
//			return null;
//		}
//	}
	
}
