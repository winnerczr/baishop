package com.baishop.framework.lock;

import java.io.IOException;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * 集群锁
 * @author Linpn
 */
public class ClusterLock {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private ZooKeeper zooKeeper;
	private String path;
	private int timeout;

	public ClusterLock(String connectString, String path, int timeout) throws IOException {
		this.zooKeeper = new ZooKeeper(connectString, timeout, new Watcher() {
			public void process(WatchedEvent event) {
				logger.info("event " + event.getType() + " has happened!");
			}
		});
		this.path = path;
		this.timeout = timeout;
	}

	/**
	 * 上锁
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public synchronized void lock() throws KeeperException, InterruptedException {
		int temp = timeout;
		while(true){
			if(!this.isLock()){
				try {
					zooKeeper.create(path, null,Collections.singletonList(new ACL(Perms.ALL,Ids.ANYONE_ID_UNSAFE)), CreateMode.EPHEMERAL);
					break;
				} catch (KeeperException.NodeExistsException e) {
					this.wait(100);
				}
			}else{
				this.wait(100);
			}
			
			temp -= 100;
			if(temp<=0)
				throw new InterruptedException("lock timeout!");
		}
	}

	/**
	 * 解锁
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public synchronized void unLock() throws InterruptedException, KeeperException {
		if(this.isLock()){
			try{
				zooKeeper.delete(path, -1);
			}catch(KeeperException.NoNodeException e){				
			}
		}
	}

	/**
	 * 是否有锁
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public synchronized boolean isLock() throws KeeperException, InterruptedException {
		Stat stat = zooKeeper.exists(path, true);
		if(stat!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * 关闭连接
	 * @throws InterruptedException
	 */
	public synchronized void close() throws InterruptedException {
		zooKeeper.close();
	}

}