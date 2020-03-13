package org.gameserver.core.thread;

import io.netty.channel.Channel;
import org.gameserver.core.network.packet.BasePacket;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * @Author: shengbao
 * @Date: 2020/3/13 20:17
 */
public class OrderedThreadPoolExecutor extends ThreadPoolExecutor {

    private ConcurrentHashMap<Object, Executor> childExecutors = new ConcurrentHashMap<>();

    public OrderedThreadPoolExecutor(int corePoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
        super(corePoolSize, corePoolSize, keepAliveTime, unit, new LinkedBlockingQueue(), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void execute(Runnable command) {
        if (command instanceof BasePacket) {
            BasePacket packet = (BasePacket) command;
            Executor executor = getChildExecutor(packet.channel());
            executor.execute(packet);
        } else {
            super.execute(command);
        }
    }

    private Executor getChildExecutor(Channel channel) {
        Executor executor = childExecutors.get(channel);
        if (executor == null) {
            executor = new ChildExecutor();
            Executor oldExecutor = childExecutors.putIfAbsent(channel, executor);
            if (oldExecutor != null) {
                executor = oldExecutor;
            }
        }
        return executor;
    }

    protected final void doUnorderedExecute(Runnable task) {
        super.execute(task);
    }

    final class ChildExecutor implements Executor, Runnable {

        private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
        private boolean running;

        @Override
        public void run() {
            Thread thread = Thread.currentThread();
            for (;;) {
                final Runnable task;
                synchronized (this) {
                    task = tasks.poll();
                    if (task == null) {
                        running = false;
                        break;
                    }
                }

                beforeExecute(thread, task);
                try {
                    task.run();
                    afterExecute(task, null);
                } catch (RuntimeException e) {
                    afterExecute(task, e);
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void execute(Runnable command) {
            boolean start = false;
            synchronized (this) {
                try {
                    tasks.add(command);
                } catch (IllegalStateException ex) {
                    throw ex;
                }
                if (!running) {
                    running = true;
                    start = true;
                }
            }

            if (start) {
                try {
                    OrderedThreadPoolExecutor.super.execute(this);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }
}
