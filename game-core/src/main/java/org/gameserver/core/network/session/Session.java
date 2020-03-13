package org.gameserver.core.network.session;


import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * 玩家session
 *
 * @Author: shengbao
 * @Date: 2020/3/10 17:37
 */
public class Session {
    //netty的网络Channel
    private Channel channel;
    // 玩家角色id
    private Long roleId;
    /** 状态**/
    private int statu;

    public Session() {
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public void writeAndFlush(ByteBuf byteBuff) {
        if (this.channel != null && this.channel.isActive()) {
            this.channel.write(byteBuff);
        }
    }

}
