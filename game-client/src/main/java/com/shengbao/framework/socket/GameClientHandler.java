package com.shengbao.framework.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: shengbao
 * @Date: 2020/2/13 19:48
 */
public class GameClientHandler extends ChannelInboundHandlerAdapter {

    static Logger logger = LoggerFactory.getLogger(GameClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channel Read---------");

    }
}
