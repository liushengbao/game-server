package org.gameserver.core.server;


import org.gameserver.core.network.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 单服启动关闭处理类
 *
 * @Author: shengbao
 * @Date: 2020/3/9 14:57
 */
@Component
@Order(2)
public class SingleStartCloseHandler extends AbstractStartCloseHandler {

    static Logger logger = LoggerFactory.getLogger(SingleStartCloseHandler.class);

    @Autowired
    NettyServer nettyServer;

    @Override
    protected void onClose() {
        logger.info("单服开始关服");
        logger.info("单服关服成功");
    }

    @Override
    protected void onStart() {
        logger.info("单服启动开始");
        try {
            nettyServer.start();
        } catch (Throwable t) {
            logger.error("", t);
        }
        logger.info("单服启动成功");
    }

}
