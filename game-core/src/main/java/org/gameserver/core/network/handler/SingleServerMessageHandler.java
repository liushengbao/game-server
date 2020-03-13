package org.gameserver.core.network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.gameserver.core.network.packet.BasePacket;
import org.gameserver.core.network.packet.PacketFactory;
import org.gameserver.core.network.session.Session;
import org.gameserver.core.network.dispatch.PacketDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息业务处理
 *
 * @Author: shengbao
 * @Date: 2020/3/10 15:42
 */
public class SingleServerMessageHandler extends ChannelInboundHandlerAdapter {

    private static final AttributeKey<Object> SESSION_KEY = AttributeKey.newInstance("session");

    static Logger logger = LoggerFactory.getLogger(SingleServerMessageHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel:{} 连接", ctx.channel().toString());
        //每个channel 对应一个Session
        Session session = new Session();
        session.setChannel(ctx.channel());
        Attribute<Object> attribute = ctx.channel().attr(SESSION_KEY);
        attribute.set(session);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel:{} 关闭", ctx.channel().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            byteBuf.retain();
            Session session = (Session) ctx.channel().attr(SESSION_KEY).get();
            //创建一个完整的包，这个包应该是read完毕
            BasePacket packet = PacketFactory.createPacket(session, byteBuf);
            if (packet == null) {
                return;
            }

            //使用业务线程池
            PacketDispatcher.getInstance().dispatch(packet);

        } catch (Throwable t) {
            logger.error("", t);
        } finally {
            ReferenceCountUtil.release(byteBuf);
        }
    }

}
