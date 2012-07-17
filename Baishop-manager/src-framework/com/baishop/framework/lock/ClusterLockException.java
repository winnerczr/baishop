package com.baishop.framework.lock;

/**
 * 集群锁异常类
 * @author Linpn
 */
public class ClusterLockException extends RuntimeException {
	
	private static final long serialVersionUID = 7739908608271736696L;
	
	/**
	 * 构造集群锁异常类
	 */
	public ClusterLockException() {
		super();
	}

	/**
	 * 构造集群锁异常类
	 * 
	 * @param message
	 *            异常信息
	 */
	public ClusterLockException(String message) {
		super(message);
	}

	/**
	 * 构造集群锁异常类
	 * 
	 * @param cause
	 *            异常对象
	 */
	public ClusterLockException(Throwable cause) {
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
	public ClusterLockException(String message, Throwable cause) {
		super(message, cause);
	}

}
