package com.baishop.framework.lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 集群锁模版类
 * 
 * @author Linpn
 */
public class CluserLockTemplate {

	protected final Log logger = LogFactory.getLog(getClass());

	// zookeeper连接串
	private String connectString = "127.0.0.1:2181";
	// 超时时间，单位：毫秒
	private int timeout = 40 * 1000;

	public CluserLockTemplate() {
	}

	public CluserLockTemplate(String connectString) {
		this.connectString = connectString;
	}

	public CluserLockTemplate(String connectString, int timeout) {
		this.connectString = connectString;
		this.timeout = timeout;
	}
	
	

	/**
	 * 执行集群同步
	 * 
	 * @param path
	 * @param action
	 * @return
	 */
	public synchronized boolean execute(String path, CluserLockCallback action) {
		try {
			// 创建集群锁对象
			CluserLock cluserLock = new CluserLock(connectString, path, timeout);

			try {
				// 上锁
				cluserLock.lock();
				logger.info("lock path : " + path);

				// 执行过程
				return action.doInCluserLock();

			} catch (Exception e) {
				throw e;
			} finally {
				// 解锁
				cluserLock.unLock();
				logger.info("unlock path : " + path);

				// 关闭连接
				cluserLock.close();
				logger.info("close ZooKeeper");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new CluserLockException(e);
		}
	}

	
	/**
	 * zookeeper连接串
	 * @return
	 */
	public String getConnectString() {
		return connectString;
	}

	/**
	 * zookeeper连接串
	 * @param connectString
	 */
	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}

	/**
	 * 超时时间，单位：毫秒
	 * @return
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * 超时时间，单位：毫秒
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
