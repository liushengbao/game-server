package org.gameserver.core.single.boot;

import org.gameserver.core.server.net.NettyServer;
import org.gameserver.core.server.net.SingleServerInitalizer;
import org.gameserver.core.server.net.packet.PacketScanner;
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
    public PacketScanner requstScanner() {
        PacketScanner packetScanner = new PacketScanner();
        packetScanner.setPackge(Arrays.asList("org.gameserver"));
        return packetScanner;
    }

    @Bean
    public NettyServer nettyServer() {
        return new NettyServer(9999, new SingleServerInitalizer());
    }

}
