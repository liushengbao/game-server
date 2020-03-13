package com.shengbao.framework.socket;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络服务器
 *
 * @author shengbao.Liu
 */
public class GameClient {

    static Logger logger = LoggerFactory.getLogger(GameClientHandler.class);

    private Channel channel;


    public void connect(String host, int port) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)

        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4));
                ch.pipeline().addLast(new GameClientHandler());
            }
        });
        // Start the client.
        ChannelFuture future = b.connect(host, port).sync();// (5)
        this.channel = future.channel();
    }

    public void write(Integer packetId, String params) {
        ByteBuf buffer = Unpooled.buffer();
        int bodyLengthIndex = buffer.writerIndex();
        int length = 0;
        buffer.writeInt(length);
        buffer.writeInt(packetId);
        buffer.writeInt(0);
        length += 8;
        if (!params.equals("")) {
            String[] arrs = params.split("&");
            for (int i = 0; i < arrs.length; i++) {
                String[] tempArr = arrs[i].split(":");
                if (tempArr[0].equalsIgnoreCase("byte")) {
                    byte a = Byte.valueOf(tempArr[1]);
                    buffer.writeByte(a);
                    length += 1;
                } else if (tempArr[0].equalsIgnoreCase("short")) {
                    short a = Short.valueOf(tempArr[1]);
                    buffer.writeShort(a);
                    length += 2;
                } else if (tempArr[0].equalsIgnoreCase("int")) {
                    int a = Integer.valueOf(tempArr[1]);
                    buffer.writeInt(a);
                    length += 4;
                } else if (tempArr[0].equalsIgnoreCase("boolean")) {
                    boolean a = Boolean.valueOf(tempArr[1]);
                    buffer.writeByte(a ? 1 : 0);
                    length += 1;
                } else if (tempArr[0].equalsIgnoreCase("string")) {
                    try {
                        byte[] a = tempArr[1].getBytes("UTF-8");
                        int len = (int) a.length;
                        length += 4;
                        length += a.length;
                        buffer.writeInt(len);
                        buffer.writeBytes(a);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("params is illegal!");
                    return;
                }
            }
        }

        buffer.setInt(bodyLengthIndex, length);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(buffer);
        }
    }
}
