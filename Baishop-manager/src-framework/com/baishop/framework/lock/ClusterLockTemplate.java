package com.baishop.framework.lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 集群锁模版类
 * 
 * @author Linpn
 */
public class ClusterLockTemplate {

	protected final Log logger = LogFactory.getLog(getClass());

	// zookeeper连接串
	private String connectString = "127.0.0.1:2181";
	// 超时时间，单位：毫秒
	private int timeout = 40 * 1000;

	public ClusterLockTemplate() {
	}

	public ClusterLockTemplate(String connectString) {
		this.connectString = connectString;
	}

	public ClusterLockTemplate(String connectString, int timeout) {
		this.connectString = connectString;
		this.timeout = timeout;
	}
	
	

	/**
	 * 执行集群同步锁，排队形式
	 * @param key 集群锁的键，标识是哪个集群锁，任意值(不允许'/'字符)，建议使用类的限定名，但要保证唯一
	 * @param action 回调方法
	 * @return 回调的返回值
	 */
	public synchronized boolean lock(String key, ClusterLockCallback action) {
		try {
			//组装zookeeper的path
			String path = "/ClusterLock-" + key;
			
			// 创建集群锁对象
			ClusterLock clusterLock = new ClusterLock(connectString, path, timeout);

			try {
				// 上锁
				clusterLock.lock();
				logger.info("lock path : " + path);

				// 执行过程
				return action.doInClusterLock();

			} catch (Exception e) {
				throw e;
			} finally {
				// 解锁
				clusterLock.unLock();
				logger.info("unlock path : " + path);

				// 关闭连接
				clusterLock.close();
				logger.info("close ZooKeeper");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ClusterLockException(e);
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
