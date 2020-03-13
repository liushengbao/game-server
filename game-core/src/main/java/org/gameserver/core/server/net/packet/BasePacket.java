package org.gameserver.core.server.net.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.io.UnsupportedEncodingException;

/**
 * 基础消息
 *
 * @Author: shengbao
 * @Date: 2020/3/10 15:57
 */
public abstract class BasePacket {

    private final static String UTF8 = "UTF-8";

    protected ByteBuf readByteBuf;
    protected ByteBuf writeByteBuf;
    private Channel channel;
    //包id
    private int packId;
    //返回长度
    private int responseLength;

    public void setPackId(int packId) {
        this.packId = packId;
    }

    /**
     * 消息体执行
     **/
    public void work(int packId, Channel channel, ByteBuf byteBuf) {
        this.packId = packId;
        this.channel = channel;
        this.readByteBuf = byteBuf;

        read();
        action();
        if (writeByteBuf != null) {
            writeByteBuf.setInt(0, responseLength);
            channel.writeAndFlush(writeByteBuf);
        }
    }

    protected abstract void read();

    protected byte readByte() {
        return readByteBuf.readByte();
    }

    protected boolean readBoolean() {
        return readByteBuf.readBoolean();
    }

    protected short readShort() {
        return readByteBuf.readShort();
    }

    protected int readInt() {
        return readByteBuf.readInt();
    }

    protected long readLong() {
        return readByteBuf.readLong();
    }

    protected String readString() {
        int len = readByteBuf.readInt();
        if (len <= 0) {
            return null;
        }
        final byte[] bytes = new byte[len];
        this.readByteBuf.readBytes(bytes);
        try {
            return new String(bytes, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected abstract void action();

    public void writeBegin() {
        writeByteBuf = Unpooled.buffer();
        //长度
        writeByteBuf.writeInt(0);
        // 包id
        writeInt(packId + 100000);
    }

    public void writeByte(byte value) {
        writeByteBuf.writeByte(value);
        responseLength += Byte.BYTES;
    }

    public void writeShort(short value) {
        writeByteBuf.writeShort(value);
        responseLength += Short.BYTES;
    }

    public void writeInt(int value) {
        writeByteBuf.writeInt(value);
        responseLength += Integer.BYTES;
    }

    public void writeLong(long value) {
        writeByteBuf.writeLong(value);
        responseLength += Long.BYTES;
    }

    public void writeString(String value) {
        if (value == null || value.length() == 0) {
            return;
        }
        try {
            byte[] bytes = value.getBytes(UTF8);
            writeByteBuf.writeInt(bytes.length);
            writeByteBuf.writeBytes(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
