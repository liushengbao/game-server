package org.gameserver.core;

import org.gameserver.core.single.boot.SingleBootConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 单服控制台启动
 *
 * @Author: shengbao
 * @Date: 2020/3/9 14:41
 */
@Configuration
@Order(1)
public class SingleServerConsole extends AbstractServerConsole implements ApplicationRunner {

    static Logger logger = LoggerFactory.getLogger(SingleServerConsole.class);

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SingleBootConfig.class, SingleServerConsole.class);
        builder.web(WebApplicationType.NONE);
        //自己管理jvm关闭钩子
        builder.build().setRegisterShutdownHook(false);
        builder.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("SingleServerConsole run args:{}", args);
    }


}
