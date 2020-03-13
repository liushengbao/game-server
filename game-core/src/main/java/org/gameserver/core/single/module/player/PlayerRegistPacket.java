package org.gameserver.core.single.module.player;

import org.gameserver.core.server.net.packet.BasePacket;
import org.gameserver.core.server.net.packet.Packet;
import org.gameserver.core.server.net.packet.PacketId;

/**
 * 玩家注册
 *
 * @Author: shengbao
 * @Date: 2020/3/10 16:03
 */
@Packet(PacketId.PLAYER_REGIST)
public class PlayerRegistPacket extends BasePacket {

    @Override
    protected void read() {

    }

    @Override
    protected void action() {

    }
}
