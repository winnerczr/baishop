package com.baishop.service.impl.ass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.baishop.entity.ass.Syslog;
import com.baishop.framework.exception.ServiceException;
import com.baishop.service.BaseService;
import com.baishop.service.ass.SyslogService;

public class SyslogServiceImpl extends BaseService implements SyslogService {
	
	@Autowired
	private SqlMapClientTemplate sqlMapClientSyslog;
	
	
	@Override
	public void logger(Syslog syslog) {
		try{
			this.sqlMapClientSyslog.insert("Syslog.addSyslog", syslog);
			logger.debug("日志：" + syslog.getDescription());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Syslog getSyslog(int id) {
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("id", id);
			
			Syslog syslog = (Syslog)this.sqlMapClientSyslog.queryForObject("Syslog.getSyslog", params);
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
			List<Syslog> list = this.sqlMapClientSyslog.queryForList("Syslog.getSyslog", params);
			return list;			
		}catch(Exception e){
			throw new ServiceException(903001, e);
		}
	}

	@Override
	public long getSyslogCount(Map<String,Object> params) {
		try{
			long count = (Long)this.sqlMapClientSyslog.queryForObject("Syslog.getSyslogCount", params);			
			return count;		
		}catch(Exception e){
			throw new ServiceException(903001, e);
		}
	}
}
