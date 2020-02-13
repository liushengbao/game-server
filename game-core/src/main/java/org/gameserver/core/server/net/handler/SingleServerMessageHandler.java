package org.gameserver.core.server.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.gameserver.core.server.net.request.MsgRequest;
import org.gameserver.core.server.net.request.RequstScanner;

/**
 * 消息业务处理
 *
 * @Author: shengbao
 * @Date: 2020/3/10 15:42
 */
public class SingleServerMessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            byteBuf.retain();
            // 消息长度
            int msgLength = byteBuf.readInt();
            // 消息id
            int msgId = byteBuf.readInt();

            Class<?> clazz = RequstScanner.getClass(msgId);
            Object object = clazz.newInstance();
            MsgRequest msgRequest = (MsgRequest) object;

            Channel channel = ctx.channel();
            //执行消息请求
            msgRequest.excute(byteBuf);
        } finally {
            ReferenceCountUtil.release(byteBuf);
        }
    }
}
