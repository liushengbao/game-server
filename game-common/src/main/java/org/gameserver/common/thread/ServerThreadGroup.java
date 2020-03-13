package org.gameserver.common.thread;

/**
 * @Author: shengbao
 * @Date: 2020/3/10 18:03
 */
public class ServerThreadGroup {

    private final ServerThread[] threads;

    public ServerThreadGroup(int n) {
        ThreadGroup threadGroup = new ThreadGroup("ordered-thread");
        threads = new ServerThread[n];
        for (int i = 1; i <= n; i++) {
            threads[i] = new ServerThread(threadGroup, threadGroup.getName() + i);
        }
    }

    public void start() {
        for (ServerThread t : threads) {
            t.start();
        }
    }

    public void stop() {
        for (ServerThread t : threads) {
            t.stop(true);
        }
    }
    
}
