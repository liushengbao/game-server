package org.gameserver.core.network.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 批量消息的编码器
 * 
 * @author shengBao.Liu
 * @version date:2019/3/23
 */
public class TcpBatchMsgEncode extends ChannelOutboundHandlerAdapter {

	final static Logger logger = LoggerFactory.getLogger(TcpBatchMsgEncode.class);

	/** 是否直接内存 **/
	private final boolean preferDirect;

	public TcpBatchMsgEncode() {
		this(true);
	}

	protected TcpBatchMsgEncode(boolean preferDirect) {
		this.preferDirect = preferDirect;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		// 支持写单个和list<MsgPacket>
//		if (msg instanceof List) {
//			List<?> list = (List<?>) msg;
//			for (int i = 0; i < list.size(); i++) {
//				MsgPacket msgPacket = (MsgPacket) list.get(i);
//				wirte0(ctx, msgPacket, null);
//			}
//		} else {
//			wirte0(ctx, msg, promise);
//		}
	}

//	private void wirte0(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
//		ByteBuf buf = null;
//		try {
//			if (msg instanceof MsgPacket) {
//				MsgPacket cast = (MsgPacket) msg;
//				buf = allocateBuffer(ctx, cast, preferDirect);
//				try {
//					encode(ctx, cast, buf);
//				} finally {
//					ReferenceCountUtil.release(cast);
//				}
//
//				if (buf.isReadable()) {
//					ctx.write(buf);
//				} else {
//					buf.release();
//					ctx.write(Unpooled.EMPTY_BUFFER, promise);
//				}
//				buf = null;
//			} else {
//				ctx.write(msg, promise);
//			}
//
//		} catch (EncoderException e) {
//			throw e;
//		} catch (Throwable e) {
//			throw new EncoderException(e);
//		} finally {
//			if (buf != null) {
//				buf.release();
//			}
//		}
//	}
//
//	private void encode(ChannelHandlerContext ctx, MsgPacket packet, ByteBuf out) {
//		NettyProtobuf buf = new NettyProtobuf(out);
//		try {
//			int writerIndex = buf.getBuf().writerIndex();
//			buf.writeInt32(0);
//			buf.writeInt32(packet.getMsgId());
//			buf.writeInt32(packet.getStatus());
//			if (packet instanceof IDispatchMessage) {
//				IDispatchMessage forwardMsgPacket = (IDispatchMessage) packet;
//				buf.writeInt32(forwardMsgPacket.getAgentServerId());
//				buf.writeInt64(forwardMsgPacket.getRoleId());
//				if (forwardMsgPacket.getMsgPacket() != null) {
//					buf.writeInt32(forwardMsgPacket.getMsgPacket().getMsgId());
//					buf.writeInt32(forwardMsgPacket.getMsgPacket().getStatus());
//					forwardMsgPacket.getMsgPacket().encode(buf);
//				}
//			} else if (packet instanceof InnerMsgPacket) {
//				InnerMsgPacket innerMsgPacket = (InnerMsgPacket) packet;
//				buf.writeInt32(innerMsgPacket.getAgentServerId());
//				buf.writeInt64(innerMsgPacket.getRoleId());
//			}
//			packet.encode(buf);
//			int bodySize = buf.getBuf().readableBytes() - 4;
//			buf.getBuf().setIntLE(writerIndex, bodySize); // 小端
//			buf.getBuf().retain();
//		} catch (Exception e) {
//			logger.error("tcp 编码器编码错误~", e);
//		} finally {
//			ReferenceCountUtil.release(buf);
//		}
//
//	}
//
//	protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, MsgPacket msg, boolean preferDirect) throws Exception {
//		if (preferDirect) {
//			return ctx.alloc().ioBuffer();
//		} else {
//			return ctx.alloc().heapBuffer();
//		}
//	}

}
