package org.gameserver.core.server.net.session;

import java.nio.channels.Channel;

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
}
