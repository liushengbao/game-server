package org.gameserver.common.thread;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 服务线程
 *
 * @Author: shengbao
 * @Date: 2020/3/10 17:51
 */
public class ServerThread extends Thread {

    private volatile boolean stop;
    //命令执行队列
    private LinkedBlockingQueue<ICommand> command_queue = new LinkedBlockingQueue<>();
    //线程名称
    protected String threadName;

    private volatile boolean processingCompleted = false;

    public ServerThread(ThreadGroup group, String threadName) {
        super(group, threadName);
        this.threadName = threadName;
        this.setUncaughtExceptionHandler((t, e) -> {
            command_queue.clear();
        });
    }

    public void run() {
        stop = false;
        int loop = 0;
        while (!stop) {
            ICommand command = command_queue.poll();
            if (command == null) {
                try {
                    synchronized (this) {
                        loop = 0;
                        processingCompleted = true;
                        wait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    loop++;
                    processingCompleted = false;
                    final long start = System.currentTimeMillis();
                    command.action();
                    final long endMillis = System.currentTimeMillis() - start;
                    if (loop > 1000) {
                        loop = 0;
                        try {
                            Thread.sleep(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop(boolean flag) {
        stop = flag;
        this.command_queue.clear();
        try {
            synchronized (this) {
                if (processingCompleted) {
                    processingCompleted = false;
                    notify();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCommand(ICommand command) {
        try {
            this.command_queue.add(command);
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
