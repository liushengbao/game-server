package org.gameserver.core.single.module.player;

import io.netty.buffer.ByteBuf;
import org.gameserver.core.server.net.request.MsgRequest;
import org.gameserver.core.server.net.request.Request;
import org.gameserver.core.server.net.request.RequestId;

/**
 * 玩家登录验证
 *
 * @Author: shengbao
 * @Date: 2020/3/10 15:59
 */
@Request(RequestId.PLAYER_LOGIN)
public class PlayerLoginRequest extends MsgRequest {

    private String accountName;
    private int serverId;

    @Override
    protected void read() {
        this.accountName = readString();
        this.serverId = readInt();
    }

    @Override
    protected void action() {

    }

}
