package org.gameserver.core.network.session;

import java.util.concurrent.ConcurrentHashMap;

/**
 * session 管理类
 *
 * @Author: shengbao
 * @Date: 2020/3/10 17:47
 */
public class SessionManager {

    /** 已经验证的玩家session **/
    private ConcurrentHashMap<Long, Session> sessionMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, Session> getSessionMap() {
        return sessionMap;
    }

}
