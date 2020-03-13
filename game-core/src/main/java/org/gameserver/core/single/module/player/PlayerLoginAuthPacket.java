package org.gameserver.core.single.module.player;

import io.netty.buffer.ByteBuf;
import org.gameserver.core.network.packet.BasePacket;
import org.gameserver.core.network.packet.Packet;
import org.gameserver.core.network.packet.PacketId;

/**
 * 玩家登录验证
 *
 * @Author: shengbao
 * @Date: 2020/3/10 15:59
 */
@Packet(PacketId.PLAYER_LOGIN)
public class PlayerLoginAuthPacket extends BasePacket {

    private String accountName;


    @Override
    protected void read(ByteBuf byteBuf) {
        this.accountName = readString(byteBuf);
    }

    @Override
    protected void excute() {
        System.out.println("account:" + accountName + " 请求登录验证!" + " 当前线程:" + Thread.currentThread().getName());
        writeBegin();
        writeLong(1010101010L);
    }

}
