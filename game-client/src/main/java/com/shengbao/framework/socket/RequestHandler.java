package com.shengbao.framework.socket;

import java.io.UnsupportedEncodingException;
import com.shengbao.framework.Session;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class RequestHandler {
	
	private static final RequestHandler instance = new RequestHandler();
	
	private GameClient netServer;
	private Session session;
	
	public static RequestHandler getInstance() {
		return instance;
	}
	
	public void setNetServer(GameClient netServer) {
		this.netServer = netServer;
	}
	
	public GameClient getNetServer() {
		return this.netServer;
	}

	public void connet(String host, int port) throws InterruptedException {
		netServer.connect(host, port);
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	public void write(int packetId, String params) {
		ByteBuf buffer = Unpooled.buffer();
		int bodyLengthIndex = buffer.writerIndex();
		int length = 0; 
		buffer.writeInt(length);
		buffer.writeInt(packetId);
		buffer.writeInt(0);
		length += 8;
		if (!params.equals("")) {
			String[] arrs = params.split("&");
			for (int i = 0; i < arrs.length; i++) {
				String[] tempArr = arrs[i].split(":");
				if (tempArr[0].equalsIgnoreCase("byte")) {
					byte a = Byte.valueOf(tempArr[1]);
					buffer.writeByte(a);
					length += 1;
				} else if (tempArr[0].equalsIgnoreCase("short")) {
					short a = Short.valueOf(tempArr[1]);
					buffer.writeShort(a);
					length += 2;
				} else if (tempArr[0].equalsIgnoreCase("int")) {
					int a = Integer.valueOf(tempArr[1]);
					buffer.writeInt(a);
					length += 4;
				} else if (tempArr[0].equalsIgnoreCase("boolean")) {
					boolean a = Boolean.valueOf(tempArr[1]);
					buffer.writeByte(a ? 1 : 0);
					length += 1;
				} else if (tempArr[0].equalsIgnoreCase("string")) {
					try {
						byte[] a = tempArr[1].getBytes("UTF-8");
						int len = (int) a.length;
						length += 4;
						length += a.length;
						buffer.writeInt(len);
						buffer.writeBytes(a);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					System.err.println("params is illegal!");
					return;
				} 
			}
		}
	
		buffer.setInt(bodyLengthIndex, length);
		if (this.session != null) {
			this.session.getChannel();
		}
	}

	public void close() {
		if (session != null) {
			session.getChannel().close();
		}
	}
	
	
}
