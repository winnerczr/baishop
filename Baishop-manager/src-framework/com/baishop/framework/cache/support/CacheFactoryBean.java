package com.baishop.framework.cache.support;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.baishop.framework.cache.Cache;
import com.baishop.framework.cache.CacheProvider;

@SuppressWarnings("rawtypes")
public class CacheFactoryBean implements InitializingBean, FactoryBean {
	private static Log log = LogFactory.getLog(CacheFactoryBean.class);
	private String cacherName;
	private String cacheProvider;
	private Properties properties;
	private Cache cache;

	public void afterPropertiesSet() throws Exception {
		
        if (this.properties == null) {
            throw new IllegalArgumentException("Properties of cache should not be NULL!");
        }
        
        if (StringUtils.isBlank(this.cacherName)) {
            throw new IllegalArgumentException("CacherName of cache should not be NULL!");
        }
        
        if (StringUtils.isBlank(this.cacheProvider)) {
			throw new IllegalArgumentException("cacheProvider of cache should not be NULL!");
		}
        
        Class providerClass = Class.forName(cacheProvider);
        if (!CacheProvider.class.isAssignableFrom(providerClass)) {
        	throw new IllegalArgumentException("cacheProvider of cache must be implemented in com.kadang.kd6.framework.cache.CacheProvider!");
        }
        
        cache = CacheFactory.get().create(cacherName, providerClass, properties);
        
        log.info("CacheFactoryBean create a successful");
	}

	public Object getObject() throws Exception {
		return cache;
	}

	public Class getObjectType() {
		return Cache.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setCacheProvider(String cacheProvider) {
		this.cacheProvider = cacheProvider;
	}

	public void setCacherName(String regionName) {
		this.cacherName = regionName;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
