package com.baishop.framework.config;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.baishop.framework.utils.PropsConf;

public class PropertyPlaceholderConfigurer extends
		org.springframework.beans.factory.config.PropertyPlaceholderConfigurer implements InitializingBean {

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private Resource profile;
	private String[] configs;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(configs==null)
			return;

		List<Resource> resources = new LinkedList<Resource>();
		resources.add(profile);
			
		PropsConf propsConf = new PropsConf();
		propsConf.load(profile.getInputStream());		
		String regex = "\\$\\{(.+)\\}";
		
		for(String config : configs){
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(config);
			for(int i=1;m.find();i++){
				config = config.replaceFirst(regex, propsConf.getProperty(m.group(i)));
			}
			
			Resource[] a = this.resourcePatternResolver.getResources(config);
			if(a!=null){
				for(Resource r : a){
					resources.add(r);
				}
			}
		}
		
		super.setLocations(resources.toArray(new Resource[0]));
	}


	public void setProfile(Resource profile) {
		this.profile = profile;
	}
	
	public void setConfigs(String[] configs){
		this.configs = configs;
	}
	
}
