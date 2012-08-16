package junit.remoting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.core.io.Resource;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.web.util.NestedServletException;


/**
 * 扩展HttpInvokerServiceExporter， 使其支持权限控制、访问监控
 * @author Linpn
 */
public class HttpInvokerAuthsServiceExporter 
		extends org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter implements Serializable {
	
	private static final long serialVersionUID = -4009200224393352085L;
	
	private static JSONObject configurer;
	
	private HttpInvokerAuthsServiceExporter() {
		this.setService(new Integer(0));
		this.setServiceInterface(Serializable.class);
	}

	/**
	 * 重写基类方法，添加权限功能
	 */
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			RemoteInvocation invocation = readRemoteInvocation(request);
			
			// 验证是否有权限
			String user = (String)invocation.getAttribute("user");
			String password = (String)invocation.getAttribute("password");
			
			if(user==null)
				throw new NestedServletException("User name is empty, if the client use HttpInvokerWithAuthsProxyFactoryBean?");
			
			if(this.hasAuthorities(user, password, this.getServiceInterface().getSimpleName(), invocation.getMethodName())){
				RemoteInvocationResult result = invokeAndCreateResult(invocation, getProxy());
				writeRemoteInvocationResult(request, response, result);
			}else{
				throw new NestedServletException("Current user or password wrong, or no permission to access the current method");
			}
		}
		catch (ClassNotFoundException ex) {
			throw new NestedServletException("Class not found during deserialization", ex);
		}
	}
	
	
	/**
	 * 设置权限配置资源，只运行一次
	 * @param location
	 * @throws Exception
	 */
	public void setAuthsConfigurerService(RemoteAuthsConfigurer authsConfigurer) throws Exception {
		if(configurer==null){
			configurer = this.getConfigurer(authsConfigurer);
		}
	}
	
	
	/**
	 * 设置权限配置资源，只运行一次
	 * @param location
	 * @throws Exception
	 */
	public void setAuthsConfigurerLocation(Resource location) throws Exception {
		if(configurer==null){
			configurer = this.getConfigurer(location);
		}		
	}	
	
	
	/**
	 * 判断是否访问权限
	 * @param user 用户名
	 * @param password 密码
	 * @param interfaceName 简单接口名， 如：UserService
	 * @param methodName 简单方法名，如：getUser
	 * @return 如果有权限返回true,如则返回false
	 */
	protected boolean hasAuthorities(String user, String password, String interfaceName, String methodName) {
		try {
			// 验证用户名或密码
			JSONObject jsonUser = configurer.getJSONObject(user);
			if(!password.equals(jsonUser.getString("password"))) {
				return false;
			}
			
			// 验证是否有权限
			if("all".equals(jsonUser.get("auths"))){
				return true;
			}else{			
				JSONObject jsonAuths = jsonUser.getJSONObject("auths");
				JSONArray jsonMethods = jsonAuths.getJSONArray(interfaceName);			
				for(int i=0;i<=jsonMethods.size();i++){
					if(methodName.equals(jsonMethods.getString(i))){
						return true;
					}
				}
			}
			
			return false;
			
		} catch (Exception ex){
			return false;
		}
	}
	

	
	
	/**
	 * 从service中读取JSON格式的权限数据
	 * @param authsConfigurer AuthsConfigurer 对象
	 * @return 返回JSON
	 */
	private JSONObject getConfigurer(RemoteAuthsConfigurer authsConfigurer) throws Exception {
		return authsConfigurer.getAuthsConf();
	}
	
	
	/**
	 * 从配置文件中读取JSON格式的权限数据
	 * @param location 资源文件
	 * @return 返回JSON
	 */
	private JSONObject getConfigurer(Resource location) throws Exception {
		StringBuilder sb = new StringBuilder();
	
		// 从文件中读取数据
		sb.append("{");
		BufferedReader br = new BufferedReader(new InputStreamReader(location.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			if(line.trim().length()>0 && line.trim().charAt(0)!='#'){
				sb.append(line);
			}
		}
		sb.append("}");
		
		return JSONObject.fromObject(sb.toString());
		
//		配置文件格式如下：
//		#
//		# 远程服务用户和权限
//		# 配置格式(.json), 注释是(#)
//		#
//
//		# 管理员
//		admin : {
//			password : '123456',
//			provide: '管理员',
//			auths : 'all'
//		},
//
//		# 前台用户
//		user : {
//			password : '123456',
//			provide: '前台用户',
//			auths : {
//				UsersService : [
//					'getUsers',
//					'getUsersList',
//					'addUsers',
//					'editUsers'
//				]
//			}
//		}
		
	}

}
