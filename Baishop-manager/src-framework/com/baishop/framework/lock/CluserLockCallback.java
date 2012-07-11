package com.baishop.framework.lock;


/**
 * 集群锁的回调接口
 * @author Linpn
 */
public interface CluserLockCallback {
	
	/**
	 * 集群锁的回调过程， 在执行该过程前会先上锁，执行后会解锁
	 * @return
	 */
	boolean doInCluserLock();
}
