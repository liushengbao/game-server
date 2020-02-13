package org.gameserver.core.server.net.request;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * 基础消息
 *
 * @Author: shengbao
 * @Date: 2020/3/10 15:57
 */
public abstract class MsgRequest {

    private final static String UTF8 = "UTF-8";

    protected ByteBuf byteBuf;

    /**
     * 消息体执行
     **/
    public void excute(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
        read();
        action();
    }

    protected abstract void read();

    protected byte readByte() {
        return byteBuf.readByte();
    }

    protected boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    protected short readShort() {
        return byteBuf.readShort();
    }

    protected int readInt() {
        return byteBuf.readInt();
    }

    protected long readLong() {
        return byteBuf.readLong();
    }

    protected String readString() {
        int len = byteBuf.readInt();
        if (len <= 0) {
            return null;
        }
        final byte[] bytes = new byte[len];
        this.byteBuf.readBytes(bytes);
        try {
            return new String(bytes, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected abstract void action();
}
