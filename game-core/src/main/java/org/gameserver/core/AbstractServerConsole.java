package org.gameserver.core;

import org.apache.commons.lang3.StringUtils;
import org.gameserver.core.annotation.ConsoleCommand;
import org.gameserver.core.server.GameLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @Author: shengbao
 * @Date: 2020/3/9 16:56
 */
public abstract class AbstractServerConsole implements BeanPostProcessor, ApplicationContextAware, ApplicationListener<ApplicationEvent> {

    static Logger logger = LoggerFactory.getLogger(AbstractServerConsole.class);

    protected static volatile boolean stop;

    private static Map<String, Command> commandMap = new HashMap<>();


    /**
     * 启动控制台输入
     **/
    protected void start() {
        //初始化控制台命令
        initConsoleCommand();
        //启动控制台输入线程
        new Thread(new ConsoleWorker(), "控制台输入线程").start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithLocalMethods(AopUtils.getTargetClass(bean), (method) -> {
            ConsoleCommand anno = AnnotationUtils.getAnnotation(method, ConsoleCommand.class);
            if (anno == null) {
                return;
            }
            Command cmd = new Command() {
                @Override
                public String name() {
                    return anno.value();
                }

                @Override
                public void execute() throws Exception {
                    try {
                        method.invoke(bean, new Object[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("方法执行时出现异常", e);
                    }
                }
            };
            commandMap.put(cmd.name(), cmd);
        });

        commandMap.put("stop", new Command() {
            @Override
            public String name() {
                return "stop";
            }

            @Override
            public void execute() throws Exception {
                GameLifeCycle.exit();
            }
        });
        return bean;
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartedEvent) {
            start();
        }
    }

    private void initConsoleCommand() {

    }

    /**
     * 停止
     **/
    protected void stop() {
        GameLifeCycle.exit();
    }


    static class ConsoleWorker implements Runnable {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (!stop) {
                String str = scanner.nextLine();
                if (str == null || str.length() == 0) {
                    continue;
                }
                String cmdStr = StringUtils.split(str)[0];
                Command command = commandMap.get(cmdStr);
                if (command != null) {
                    try {
                        command.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.error("ConsoleCommand is not exsit cmd:{}", cmdStr);
                }

            }
        }
    }

    interface Command {
        String name();

        void execute() throws Exception;
    }
}
