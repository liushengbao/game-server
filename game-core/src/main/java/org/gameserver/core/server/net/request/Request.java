package org.gameserver.core.server.net.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息请求注解
 *
 * @Author: shengbao
 * @Date: 2020/3/10 16:18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Request {
    int value();
}
