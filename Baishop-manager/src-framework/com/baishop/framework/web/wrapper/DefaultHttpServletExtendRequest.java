package com.baishop.framework.web.wrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.baishop.framework.web.HttpServletExtendRequest;
import com.baishop.framework.web.binder.ServletRequestExtendDataBinder;


public class DefaultHttpServletExtendRequest implements HttpServletExtendRequest {
	
	private HttpServletRequest request;
	
	public DefaultHttpServletExtendRequest(HttpServletRequest request){
		this.request = request;
	}
	
	
	@Override
	public HttpServletRequest getRequest() {
		return request;
	}


	@Override
	public String getParameter(String name) {
		return request.getParameter(name);
	}

	@Override
	public String getParameter(String name, String charsetName) {
		return this.getParameter(name, charsetName, null);
	}

	@Override
	public String getParameter(String name, String charsetName, String defaultValue) {
		try {
			String value = request.getParameter(name);
			
			if(StringUtils.isBlank(value))
				return defaultValue;
			
			if(StringUtils.isBlank(charsetName))
				charsetName = "UTF-8";
			
			return new String(value.getBytes("ISO8859-1"), charsetName);
			
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); 
		}
	}

	@Override
	public String getAuthType() {
		return request.getAuthType();
	}

	@Override
	public Cookie[] getCookies() {
		return request.getCookies();
	}

	@Override
	public long getDateHeader(String name) {
		return request.getDateHeader(name);
	}

	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return request.getHeaders(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return request.getHeaderNames();
	}

	@Override
	public int getIntHeader(String name) {
		return request.getIntHeader(name);
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public String getPathInfo() {
		return request.getPathInfo();
	}

	@Override
	public String getPathTranslated() {
		return request.getPathTranslated();
	}

	@Override
	public String getContextPath() {
		return request.getContextPath();
	}

	@Override
	public String getQueryString() {
		return request.getQueryString();
	}

	@Override
	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	@Override
	public boolean isUserInRole(String role) {
		return request.isUserInRole(role);
	}

	@Override
	public Principal getUserPrincipal() {
		return request.getUserPrincipal();
	}

	@Override
	public String getRequestedSessionId() {
		return request.getRequestedSessionId();
	}

	@Override
	public String getRequestURI() {
		return request.getRequestURI();
	}

	@Override
	public StringBuffer getRequestURL() {
		return request.getRequestURL();
	}

	@Override
	public String getServletPath() {
		return request.getServletPath();
	}

	@Override
	public HttpSession getSession(boolean create) {
		return request.getSession(create);
	}

	@Override
	public HttpSession getSession() {
		return request.getSession();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return request.isRequestedSessionIdValid();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return request.isRequestedSessionIdFromCookie();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return request.isRequestedSessionIdFromURL();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return request.isRequestedSessionIdFromUrl();
	}

	@Override
	public boolean authenticate(HttpServletResponse response)
			throws IOException, ServletException {
		return request.authenticate(response);
	}

	@Override
	public void login(String username, String password) throws ServletException {
		request.login(username, password);
	}

	@Override
	public void logout() throws ServletException {
		request.logout();
	}

	@Override
	public Collection<Part> getParts() throws IOException,
			IllegalStateException, ServletException {
		return request.getParts();
	}

	@Override
	public Part getPart(String name) throws IOException, IllegalStateException,
			ServletException {
		return request.getPart(name);
	}

	@Override
	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return request.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	@Override
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding(env);
	}

	@Override
	public int getContentLength() {
		return request.getContentLength();
	}

	@Override
	public String getContentType() {
		return request.getContentType();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return request.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return request.getParameterMap();
	}

	@Override
	public String getProtocol() {
		return request.getProtocol();
	}

	@Override
	public String getScheme() {
		return request.getScheme();
	}

	@Override
	public String getServerName() {
		return request.getServerName();
	}

	@Override
	public int getServerPort() {
		return request.getServerPort();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	@Override
	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	@Override
	public void setAttribute(String name, Object o) {
		request.setAttribute(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		request.removeAttribute(name);
	}

	@Override
	public Locale getLocale() {
		return request.getLocale();
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return request.getLocales();
	}

	@Override
	public boolean isSecure() {
		return request.isSecure();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return request.getRequestDispatcher(path);
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getRealPath(String path) {
		return request.getRealPath(path);
	}

	@Override
	public int getRemotePort() {
		return request.getRemotePort();
	}

	@Override
	public String getLocalName() {
		return request.getLocalName();
	}

	@Override
	public String getLocalAddr() {
		return request.getLocalAddr();
	}

	@Override
	public int getLocalPort() {
		return request.getLocalPort();
	}

	@Override
	public ServletContext getServletContext() {
		return request.getServletContext();
	}

	@Override
	public AsyncContext startAsync() {
		return request.startAsync();
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest,
			ServletResponse servletResponse) {
		return request.startAsync(servletRequest, servletResponse);
	}

	@Override
	public boolean isAsyncStarted() {
		return request.isAsyncStarted();
	}

	@Override
	public boolean isAsyncSupported() {
		return request.isAsyncSupported();
	}

	@Override
	public AsyncContext getAsyncContext() {
		return request.getAsyncContext();
	}

	@Override
	public DispatcherType getDispatcherType() {
		return request.getDispatcherType();
	}

	@Override
	public Iterator<String> getFileNames() {		
		if (request instanceof MultipartRequest) {
			MultipartRequest multipartRequest = (MultipartRequest) request;
			return multipartRequest.getFileNames();
		}
		
		return null;
	}

	@Override
	public MultipartFile getFile(String name) {		
		if (request instanceof MultipartRequest) {
			MultipartRequest multipartRequest = (MultipartRequest) request;
			return multipartRequest.getFile(name);
		}
		
		return null;
	}

	@Override
	public List<MultipartFile> getFiles(String name) {
		if (request instanceof MultipartRequest) {
			MultipartRequest multipartRequest = (MultipartRequest) request;
			return multipartRequest.getFiles(name);
		}
		
		return null;
	}

	@Override
	public Map<String, MultipartFile> getFileMap() {
		if (request instanceof MultipartRequest) {
			MultipartRequest multipartRequest = (MultipartRequest) request;
			return multipartRequest.getFileMap();
		}
		
		return null;
	}

	@Override
	public MultiValueMap<String, MultipartFile> getMultiFileMap() {
		if (request instanceof MultipartRequest) {
			MultipartRequest multipartRequest = (MultipartRequest) request;
			return multipartRequest.getMultiFileMap();
		}
		
		return null;
	}

	@Override
	public <T> T getBindObject(Class<T> clazz) {
		try {
			T obj = clazz.newInstance();

			ServletRequestDataBinder binder = new ServletRequestDataBinder(obj);
			binder.bind(request);
						
			return obj;
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public <T> T getBindObject(Class<T> clazz, String objectName) {
		try {
			T obj = clazz.newInstance();

			ServletRequestExtendDataBinder binder = new ServletRequestExtendDataBinder(obj);
			binder.bind(request, objectName);
						
			return obj;
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	
	@Override
	public Boolean getBooleanParameter(String name) {
		return this.getBooleanParameter(name, null);
	}

	@Override
	public Boolean getBooleanParameter(String name, Boolean defaultValue) {
		try{
			String value = request.getParameter(name);
			
			if(StringUtils.isBlank(value))
				return defaultValue;
			
			return Boolean.valueOf(value.trim());
			
		}catch(Exception e){
			return defaultValue;
		}
	}

	@Override
	public Integer getIntParameter(String name) {
		return this.getIntParameter(name, null);
	}

	@Override
	public Integer getIntParameter(String name, Integer defaultValue) {
		try{
			String value = request.getParameter(name);
			
			if(StringUtils.isBlank(value))
				return defaultValue;
			
			return Integer.valueOf(value.trim());
			
		}catch(Exception e){
			return defaultValue;
		}
	}

	@Override
	public Long getLongParameter(String name) {
		return this.getLongParameter(name, null);
	}

	@Override
	public Long getLongParameter(String name, Long defaultValue) {
		try{
			String value = request.getParameter(name);
			
			if(StringUtils.isBlank(value))
				return defaultValue;
			
			return Long.valueOf(value.trim());
			
		}catch(Exception e){
			return defaultValue;
		}
	}

	@Override
	public Float getFloatParameter(String name) {
		return this.getFloatParameter(name, null);
	}

	@Override
	public Float getFloatParameter(String name, Float defaultValue) {
		try{
			String value = request.getParameter(name);
			
			if(StringUtils.isBlank(value))
				return defaultValue;
			
			return Float.valueOf(value.trim());
			
		}catch(Exception e){
			return defaultValue;
		}
	}

	@Override
	public Double getDoubleParameter(String name) {
		return this.getDoubleParameter(name, null);
	}

	@Override
	public Double getDoubleParameter(String name, Double defaultValue) {
		try{
			String value = request.getParameter(name);
			
			if(StringUtils.isBlank(value))
				return defaultValue;
			
			return Double.valueOf(value.trim());
			
		}catch(Exception e){
			return defaultValue;
		}
	}

	@Override
	public Date getDateParameter(String name) {
		return this.getDateParameter(name, null);
	}

	@Override
	public Date getDateParameter(String name, String pattern){
		return this.getDateParameter(name, pattern, null);
	}
	

	@Override
	public Date getDateParameter(String name, String pattern, Date defaultValue) {
		try{
			String value = request.getParameter(name);
			
			if(StringUtils.isBlank(value))
				return defaultValue;
			
			SimpleDateFormat sdf;
			if(StringUtils.isBlank(pattern))
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			else
				sdf = new SimpleDateFormat(pattern);
			
			return sdf.parse(value.trim());
			
		}catch(Exception e){
			return defaultValue;
		}
	}
	
}
