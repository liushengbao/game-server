package org.gameserver.core.single.dev;

import org.gameserver.core.annotation.ConsoleCommand;
import org.springframework.stereotype.Component;

/**
 * 单服控制台命令
 * @Author: shengbao
 * @Date: 2020/3/9 17:04
 */
@Component
public class SingleServerConsoleCommand {

    @ConsoleCommand("test")
    public void test() {
        System.err.println("test command");
    }
}
