package org.gameserver.core.single.boot;

import org.gameserver.core.server.net.request.RequstScanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 单服spring配置
 *
 * @Author: shengbao
 * @Date: 2020/3/9 14:43
 */
@Configuration
@SpringBootApplication
@ComponentScan(value = {"org.gameserver"})
public class SingleBootConfig {

    @Bean
    public RequstScanner requstScanner() {
        RequstScanner requstScanner = new RequstScanner();
        requstScanner.setPackge(Arrays.asList("org.gameserver"));
        return requstScanner;
    }

}
