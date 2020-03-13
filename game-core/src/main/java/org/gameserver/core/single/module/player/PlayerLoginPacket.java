package org.gameserver.core.single.module.player;

import org.gameserver.core.server.net.packet.BasePacket;
import org.gameserver.core.server.net.packet.Packet;
import org.gameserver.core.server.net.packet.PacketId;

/**
 * 玩家登录验证
 *
 * @Author: shengbao
 * @Date: 2020/3/10 15:59
 */
@Packet(PacketId.PLAYER_LOGIN)
public class PlayerLoginPacket extends BasePacket {

    private String accountName;

    @Override
    protected void read() {
        this.accountName = readString();
    }

    @Override
    protected void action() {
        System.out.println("account:" + accountName + " 请求登录验证!");
        writeBegin();
        writeLong(1010101010L);
    }

}
