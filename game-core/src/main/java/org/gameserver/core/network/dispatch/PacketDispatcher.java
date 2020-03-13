package org.gameserver.core.network.dispatch;

import org.gameserver.common.thread.SimpleThreadFactory;
import org.gameserver.core.network.packet.BasePacket;
import org.gameserver.core.thread.OrderedThreadPoolExecutor;

import java.util.concurrent.TimeUnit;

/**
 * 消息包派发
 *
 * @Author: shengbao
 * @Date: 2020/3/13 16:36
 */
public class PacketDispatcher {

    private static PacketDispatcher instance = new PacketDispatcher();

    //    private static final ThreadPoolExecutor executor;
//    private static final EventExecutorGroup executor;
//    private static final ServerThreadGroup executor;
    private static final OrderedThreadPoolExecutor executor;

    static {
//        executor = new ThreadPoolExecutor(50, 100, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new SimpleThreadFactory("packet-excutor"));
//        executorGroup = new DefaultEventExecutorGroup(2);
//        serverThreadGroup = new ServerThreadGroup(20);
        executor = new OrderedThreadPoolExecutor(5, 60L, TimeUnit.SECONDS, new SimpleThreadFactory("packet-excutor"));
    }

    public static PacketDispatcher getInstance() {
        return instance;
    }

    /**
     * 派发给业务线程池执行
     **/
    public void dispatch(BasePacket packet) {
        executor.execute(packet);
    }


}
