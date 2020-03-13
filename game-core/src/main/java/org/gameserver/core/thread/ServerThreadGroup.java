package org.gameserver.core.thread;


import org.gameserver.core.network.packet.BasePacket;

/**
 * @Author: shengbao
 * @Date: 2020/3/13 19:48
 */
public class ServerThreadGroup  {

    private final ServerThread[] serverThreadArray;
    private volatile boolean stop;

    public ServerThreadGroup(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n 必须大于0!");
        }
        serverThreadArray = new ServerThread[n];

        ThreadGroup threadGroup = new ThreadGroup("server-thread");
        for (int i = 0; i < n; i++) {
            serverThreadArray[i] = new ServerThread(threadGroup, threadGroup.getName() + i);
            serverThreadArray[i].start();
        }
    }


    public void shutdown() {
        for (ServerThread t : serverThreadArray) {
            t.stop(true);
        }
    }

    public ServerThread[] getServerThreadArray() {
        return serverThreadArray;
    }

}
