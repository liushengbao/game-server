package org.gameserver.core.server;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author: shengbao
 * @Date: 2020/3/9 16:51
 */
public class GameLifeCycle {

    public static void exit() {
        System.exit(0);
    }

    /** 关闭进程先写错误状态 **/
    public static void startupDone(final String str) {
        try (final FileWriter writer = new FileWriter("startup.done")) {
            if (str != null) {
                writer.write(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
