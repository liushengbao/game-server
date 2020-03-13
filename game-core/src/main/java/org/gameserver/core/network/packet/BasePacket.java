package org.gameserver.core.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.gameserver.core.network.session.Session;

import java.io.UnsupportedEncodingException;

/**
 * 基础消息
 *
 * @Author: shengbao
 * @Date: 2020/3/10 15:57
 */
public abstract class BasePacket implements Runnable{

    private final static String UTF8 = "UTF-8";

    protected ByteBuf byteBuff;
    private Session session;
    //包id
    private int packId;
    //返回长度
    private int responseLength;

    public void setSession(Session session) {
        this.session = session;
    }

    public void setPackId(int packId) {
        this.packId = packId;
    }

    @Override
    public void run() {
        excute();
        // 写过去
        if (this.byteBuff != null && this.byteBuff.writerIndex() > 0) {
            flush(this.byteBuff);
        }
    }

    //刷到channel
    public void flush(ByteBuf byteBuf) {
        if (this.session != null) {
            this.session.writeAndFlush(byteBuf);
        }
    }

    /**
     * 消息体执行
     **/
    public void work(int packId, Channel channel, ByteBuf byteBuf) {
        excute();
        if (byteBuff != null) {
            byteBuff.setInt(0, responseLength);
            channel.writeAndFlush(byteBuff);
        }
    }

    protected abstract void read(ByteBuf byteBuf);

    protected byte readByte(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }

    protected boolean readBoolean(ByteBuf byteBuf) {
        return byteBuf.readBoolean();
    }

    protected short readShort(ByteBuf byteBuf) {
        return byteBuf.readShort();
    }

    protected int readInt(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }

    protected long readLong(ByteBuf byteBuf) {
        return byteBuf.readLong();
    }

    protected String readString(ByteBuf byteBuf) {
        int len = byteBuf.readInt();
        if (len <= 0) {
            return null;
        }
        final byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        try {
            return new String(bytes, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected abstract void excute();

    public void writeBegin() {
        byteBuff = Unpooled.buffer();
        //长度
        byteBuff.writeInt(0);
        // 包id
        writeInt(packId + 100000);
    }

    public void writeByte(byte value) {
        byteBuff.writeByte(value);
        responseLength += Byte.BYTES;
    }

    public void writeShort(short value) {
        byteBuff.writeShort(value);
        responseLength += Short.BYTES;
    }

    public void writeInt(int value) {
        byteBuff.writeInt(value);
        responseLength += Integer.BYTES;
    }

    public void writeLong(long value) {
        byteBuff.writeLong(value);
        responseLength += Long.BYTES;
    }

    public void writeString(String value) {
        if (value == null || value.length() == 0) {
            return;
        }
        try {
            byte[] bytes = value.getBytes(UTF8);
            byteBuff.writeInt(bytes.length);
            byteBuff.writeBytes(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Channel channel() {
        return session.getChannel();
    }

}
