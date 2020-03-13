package org.gameserver.core.server.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.gameserver.core.server.life.GameLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty网络服务
 *
 * @Author: shengbao
 * @Date: 2020/3/10 10:50
 */
public class NettyServer implements INetServer {

    static Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private ChannelFuture future;
    private final int port;
    private ChannelInitializer<?> channelInitializer;

    public NettyServer(int port, ChannelInitializer<?> channelInitializer) {
        this.port = port;
        this.channelInitializer = channelInitializer;
    }

    @Override
    public void start() {
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        //连接
        bootstrap.handler(new LoggingHandler(LogLevel.INFO));
        //处理io事件和io操作
        bootstrap.childHandler(channelInitializer);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);//非阻塞模式
        try {
            future = bootstrap.bind(this.port).sync();
        } catch (Throwable t) {
            logger.error("netty server start error!", t);
            GameLifeCycle.exit();
        }
    }

    @Override
    public void stop() {

        try {
            future.channel().close().get();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            workerGroup.shutdownGracefully().get();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            bossGroup.shutdownGracefully().get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
