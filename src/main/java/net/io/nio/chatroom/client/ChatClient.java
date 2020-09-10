package net.io.nio.chatroom.client;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ChatClient {


	public static void main(String[] args) {
		ChatClient chatClient = new ChatClient();
		chatClient.start();
	}

	public void start() {

		try {

			SocketChannel client = SocketChannel.open();
			client.connect(new InetSocketAddress("localhost", 7777));
//			client.
//			socket = new Socket("127.0.0.1", 8888);
//
//			// 创建IO流
//			reader = new BufferedReader(
//					new InputStreamReader(socket.getInputStream())
//			);
//			writer = new BufferedWriter(
//					new OutputStreamWriter(socket.getOutputStream())
//			);
//
//			// 处理用户的输入
//			new Thread(new UserInputHandler(this)).start();
//
//			// 读取服务器转发的消息
//			String msg;
//			while ((msg = receive()) != null) {
//				System.out.println(msg);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			close();
		}
	}


	// 发送消息给服务器
//	public void send(String msg) throws IOException {
//		if (!socket.isOutputShutdown()) {
//			writer.write(msg + "\n");
//			writer.flush();
//		}
//	}
//
//	// 从服务器接收消息
//	public String receive() throws IOException {
//		String msg = null;
//		if (!socket.isInputShutdown()) {
//			msg = reader.readLine();
//		}
//		return msg;
//	}
//
//	// 检查用户是否准备退出
//	public boolean readyToQuit(String msg) {
//		return "quit".equals(msg);
//	}
//
//	public void close() {
//		if (writer != null) {
//			try {
//				System.out.println("关闭socket");
//				writer.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}


}