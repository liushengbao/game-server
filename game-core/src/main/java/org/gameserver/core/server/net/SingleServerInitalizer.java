package org.gameserver.core.server.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.ByteOrder;

/**
 * @Author: shengbao
 * @Date: 2020/3/10 14:01
 */
public class SingleServerInitalizer extends ChannelInitializer<Channel> {
    //表示的是包的最大长度，超出包的最大长度netty将会做一些特殊处理
    static int maxFrameLength = 1024 * 1024;
    // 指的是长度域的偏移量，表示跳过指定长度个字节之后的才是长度域
    static int lengthFieldOffset = 0;
    // 记录该帧数据长度的字段本身的长度
    static int lengthFieldLength = 4;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //空闲状态handler
        pipeline.addLast(new IdleStateHandler(120, 0, 0));
        //拆包粘包处理
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4));
        //byteBuf 转 业务消息处理
        pipeline.addLast();
    }

}
