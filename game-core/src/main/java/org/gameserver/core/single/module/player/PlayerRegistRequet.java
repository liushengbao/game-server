package org.gameserver.core.single.module.player;

import io.netty.buffer.ByteBuf;
import org.gameserver.core.server.net.request.MsgRequest;
import org.gameserver.core.server.net.request.Request;
import org.gameserver.core.server.net.request.RequestId;

/**
 * 玩家注册
 *
 * @Author: shengbao
 * @Date: 2020/3/10 16:03
 */
@Request(RequestId.PLAYER_REGIST)
public class PlayerRegistRequet extends MsgRequest {

    @Override
    protected void read() {

    }

    @Override
    protected void action() {
        
    }
}
