package com.baishop.framework.lock;

/**
 * 集群锁异常类
 * @author Linpn
 */
public class CluserLockException extends RuntimeException {
	
	private static final long serialVersionUID = 7739908608271736696L;
	
	/**
	 * 构造集群锁异常类
	 */
	public CluserLockException() {
		super();
	}

	/**
	 * 构造集群锁异常类
	 * 
	 * @param message
	 *            异常信息
	 */
	public CluserLockException(String message) {
		super(message);
	}

	/**
	 * 构造集群锁异常类
	 * 
	 * @param cause
	 *            异常对象
	 */
	public CluserLockException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造集群锁异常类
	 * 
	 * @param message
	 *            异常信息
	 * @param cause
	 *            异常对象
	 */
	public CluserLockException(String message, Throwable cause) {
		super(message, cause);
	}

}
