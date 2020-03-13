package org.gameserver.core.server.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.gameserver.core.server.net.packet.BasePacket;
import org.gameserver.core.server.net.packet.PacketScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息业务处理
 *
 * @Author: shengbao
 * @Date: 2020/3/10 15:42
 */
public class SingleServerMessageHandler extends ChannelInboundHandlerAdapter {

    static Logger logger = LoggerFactory.getLogger(SingleServerMessageHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            byteBuf.retain();
            // 消息长度
            int msgLength = byteBuf.readInt();
            // 包id
            int packetId = byteBuf.readInt();

            Class<?> clazz = PacketScanner.getClass(packetId);
            Object object = clazz.newInstance();
            BasePacket packet = (BasePacket) object;
            //执行消息请求
            packet.work(packetId, ctx.channel(), byteBuf);

        } catch (Throwable t) {
            logger.error("", t);
        } finally {
            ReferenceCountUtil.release(byteBuf);
        }
    }

}
