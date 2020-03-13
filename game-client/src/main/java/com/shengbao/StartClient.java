package com.shengbao;

import com.shengbao.application.view.MainView;
import com.shengbao.framework.socket.GameClient;

public class StartClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//游戏网络客户端
		GameClient client = new GameClient();
		new MainView(client);
	}

}
