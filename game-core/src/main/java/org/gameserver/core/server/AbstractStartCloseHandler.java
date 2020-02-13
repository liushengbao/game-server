package org.gameserver.core.server;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @Author: shengbao
 * @Date: 2020/3/9 15:03
 */
public abstract class AbstractStartCloseHandler implements ApplicationContextAware, ApplicationRunner {

    private Thread shutdownHookThread;

    protected ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //启动处理
        onStart();
        // 注册钩子
        registShutdownHook();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 注册jvm关闭钩子
     */
    public synchronized void registShutdownHook() {
        if (this.shutdownHookThread == null) {
            this.shutdownHookThread = new Thread(() -> {
                onClose();
                onCloseSpring();
                onCloseLog4j();
            });
        }
        Runtime.getRuntime().addShutdownHook(this.shutdownHookThread);
    }

    protected void onCloseSpring() {

        try {
            // spring销毁
            ((AbstractApplicationContext) this.applicationContext).close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void onCloseLog4j() {
        // 关闭log4j
        if (org.apache.logging.log4j.LogManager.getContext() instanceof LoggerContext) {
            Configurator.shutdown((LoggerContext) org.apache.logging.log4j.LogManager.getContext());
        }
    }

    protected abstract void onClose();

    protected abstract void onStart();

}
