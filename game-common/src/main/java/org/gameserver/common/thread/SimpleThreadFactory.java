package org.gameserver.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author shengbao.Liu
 * @email 646406929@qq.com
 * @time 2016年11月17日 下午2:33:56
 */
public class SimpleThreadFactory implements ThreadFactory {

	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private ThreadGroup group;
	private String groupName;

	public SimpleThreadFactory() {
		init();
		groupName = group.getName();
	}

	public SimpleThreadFactory(String groupName) {
		init();
		this.groupName = groupName;
	}

	private void init() {
		SecurityManager securitymanager = System.getSecurityManager();
		group = (securitymanager == null ? Thread.currentThread().getThreadGroup() : securitymanager.getThreadGroup());
	}

	@Override
    public Thread newThread(Runnable runnable) {
		String treadName = groupName + "-thread-" + threadNumber.getAndIncrement();
		Thread t = new Thread(group, runnable, treadName);
		if (t.isDaemon()) {
            t.setDaemon(false);
        }
		if (t.getPriority() != 5) {
            t.setPriority(5);
        }
		return t;
	}

}
