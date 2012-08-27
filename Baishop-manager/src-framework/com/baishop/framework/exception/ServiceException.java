package com.baishop.framework.exception;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 服务异常类
 * 
 * @author Linpn
 */
public class ServiceException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = -5893023440437427958L;

	public static final String DEFAULTMSG = "系统出错，请重新操作或联系客服人员！";
	private static ResourceBundleMessageSource props = new ResourceBundleMessageSource();

	
	static {
		props.setBasename("exception");
	}
	
	/**
	 * 初始化异常配置文件
	 * @param source
	 */
	public static void init(String source) {
		init(new String[]{source});
	}
	
	public static void init(String[] sources) {
		props.setBasenames(sources);
	}

	/**
	 * 通过异常编号获取异常信息
	 * 
	 * @param code
	 *            异常编号
	 * @return 异常信息
	 */
	public static String getMessageByCode(int code) {
		return getMessageByCode(code, null);
	}

	/**
	 * 通过异常编号获取异常信息
	 * 
	 * @param code
	 *            异常编号
	 * @param args
	 *            异常返回信息中的参数
	 * @return 异常信息
	 */
	public static String getMessageByCode(int code, String[] args) {
		try {
			if(args!=null){
				for(int i=0;i<args.length;i++){
					args[i] = new String(args[i].getBytes("UTF-8"), "ISO-8859-1");
				}
			}
			
			String msg = props.getMessage(String.valueOf(code), args, DEFAULTMSG, Locale.CHINA);
			if (DEFAULTMSG.equals(msg)) {
				return msg;
			}
			
			return new String(msg.getBytes("ISO-8859-1"), "UTF-8") + "(" + code + ")";
			
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 构造服务异常类
	 */
	public ServiceException() {
		super();
	}

	/**
	 * 构造服务异常类
	 * 
	 * @param message
	 *            异常信息
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * 构造服务异常类
	 * 
	 * @param cause
	 *            异常对象
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造服务异常类
	 * 
	 * @param message
	 *            异常信息
	 * @param cause
	 *            异常对象
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造服务异常类
	 * 
	 * @param code
	 *            异常编号
	 */
	public ServiceException(int code) {
		super(ServiceException.getMessageByCode(code));
	}

	/**
	 * 构造服务异常类
	 * 
	 * @param code
	 *            异常编号
	 * @param args
	 *            异常返回信息中的参数
	 */
	public ServiceException(int code, String... args) {
		super(ServiceException.getMessageByCode(code, args));
	}
	
	/**
	 * 构造服务异常类
	 * 
	 * @param code
	 *            异常编号
	 * @param cause
	 *            异常对象
	 */
	public ServiceException(int code, Throwable cause) {
		super(ServiceException.getMessageByCode(code), cause);
	}

	/**
	 * 构造服务异常类
	 * 
	 * @param code
	 *            异常编号
	 * @param args
	 *            异常返回信息中的参数
	 * @param cause
	 *            异常对象
	 */
	public ServiceException(int code, Throwable cause, String... args) {
		super(ServiceException.getMessageByCode(code, args), cause);
	}

}
