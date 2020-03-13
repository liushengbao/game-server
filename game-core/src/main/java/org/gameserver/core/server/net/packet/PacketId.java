package org.gameserver.core.server.net.packet;

/**
 * 消息号定义
 *
 * @Author: shengbao
 * @Date: 2020/3/10 16:19
 */
public class PacketId {
    private PacketId() {

    }

    /**
     * 登录验证
     **/
    public static final int PLAYER_LOGIN = 101001;
    /**
     * 玩家注册
     **/
    public static final int PLAYER_REGIST = 101002;

}
