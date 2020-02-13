package org.gameserver.common.thread;

/**
 * @Author: shengbao
 * @Date: 2020/3/10 18:03
 */
public class OrderedThreadGroup {
    public OrderedThreadGroup(int n) {
        ThreadGroup threadGroup = new ThreadGroup("ordered-thread");
        OrderedThread[] threads = new OrderedThread[n];
        for (int i = 1; i <= n; i++) {
            threads[i] = new OrderedThread(threadGroup, threadGroup.getName() + i);

        }
    }
}
