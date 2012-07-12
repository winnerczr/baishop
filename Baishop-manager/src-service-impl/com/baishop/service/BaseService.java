package com.baishop.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.baishop.framework.utils.ConvertUtils;

public abstract class BaseService {
	/**
	 * log4j对象
	 */
	public final Logger logger = Logger.getLogger(this.getClass());
	private SqlMapClientTemplate sqlMapClientAss;	
	private SqlMapClientTemplate sqlMapClientShop;	
	private SqlMapClientTemplate sqlMapClientSale;
	private Map<String, SqlMapClientTemplate> sqlMapClientSlaves;	
	
	
	/**
	 * 获取系统库SqlMapClientTemplate对象
	 */
	public SqlMapClientTemplate getSqlMapClientAss() {
		return sqlMapClientAss;
	}
	
	/**
	 * 设置系统库SqlMapClientTemplate对象
	 */
	public void setSqlMapClientAss(SqlMapClientTemplate sqlMapClientAss) {
		this.sqlMapClientAss = sqlMapClientAss;
	}
	
	/**
	 * 获取商城库SqlMapClientTemplate对象
	 */
	public SqlMapClientTemplate getSqlMapClientShop() {
		return sqlMapClientShop;
	}

	/**
	 * 设置商城库SqlMapClientTemplate对象
	 */
	public void setSqlMapClientShop(SqlMapClientTemplate sqlMapClientShop) {
		this.sqlMapClientShop = sqlMapClientShop;
	}
	
	/**
	 * 获取交易库SqlMapClientTemplate对象
	 */
	public SqlMapClientTemplate getSqlMapClientSale() {
		return sqlMapClientSale;
	}

	/**
	 * 设置交易库SqlMapClientTemplate对象
	 */
	public void setSqlMapClientSale(SqlMapClientTemplate sqlMapClientSale) {
		this.sqlMapClientSale = sqlMapClientSale;
	}
	
	/**
	 * 获取从库SqlMapClientTemplate对象
	 * @param slaveName 从库名称
	 */
	public SqlMapClientTemplate getSqlMapClientSlave(String slaveName) {
		return sqlMapClientSlaves.get(slaveName);
	}
	
	/**
	 * 设置从库SqlMapClientTemplate对象
	 */
	public void setSqlMapClientSlaves(Map<String, SqlMapClientTemplate> sqlMapClientSlaves) {
		this.sqlMapClientSlaves = sqlMapClientSlaves;
	}
	
	
	//-------------------------------------- service 公共方法 -------------------------------------------//


	/**
	 * 将排序的map参数转成sql中order by所需要的格式
	 * @param sorters service方法中的排序参数
	 * @return 返回sql语句中order by所需要的格式
	 */
	public String getDbSort(Map<String, String> sorters){
		if(sorters==null)
			return "";
		
		StringBuilder sbSort = new StringBuilder();	
		for(Map.Entry<String, String> entry : sorters.entrySet()){
			if(sbSort.length()>0)
				sbSort.append(",");
			
			String sort = ConvertUtils.toDatabaseField(entry.getKey());
			String dir = entry.getValue();
			
			if(!StringUtils.isBlank(sort)){
				sbSort.append(sort);			
				if(!StringUtils.isBlank(dir)){	
					sbSort.append(" ");
					sbSort.append(entry.getValue());
				}
			}else{
				return "";
			}
		}
		
		return sbSort.toString();
	}
	
}
