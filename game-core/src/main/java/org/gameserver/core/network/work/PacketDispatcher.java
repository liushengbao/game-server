package org.gameserver.core.network.work;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.gameserver.common.thread.SimpleThreadFactory;
import org.gameserver.core.network.packet.BasePacket;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消息包派发
 *
 * @Author: shengbao
 * @Date: 2020/3/13 16:36
 */
public class PacketDispatcher {

    private static PacketDispatcher instance = new PacketDispatcher();

    private static final ThreadPoolExecutor executor;

    private static final EventExecutorGroup executorGroup;

    static {
        executor = new ThreadPoolExecutor(50, 100, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new SimpleThreadFactory("packet-excutor"));
        executorGroup = new DefaultEventExecutorGroup(2);
    }

    public static PacketDispatcher getInstance() {
        return instance;
    }

    /**
     * 派发给业务线程池执行
     **/
    public void dispatch(BasePacket packet) {
//        executor.execute(packet);
        executorGroup.execute(packet);
    }
}
