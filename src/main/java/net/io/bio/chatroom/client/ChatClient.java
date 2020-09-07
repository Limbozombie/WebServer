package net.io.bio.chatroom.client;

import java.io.*;
import java.net.Socket;

public class ChatClient {

	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	public static void main(String[] args) {
		ChatClient chatClient = new ChatClient();
		chatClient.start();
	}

	// 发送消息给服务器
	public void send(String msg) throws IOException {
		if (!socket.isOutputShutdown()) {
			writer.write(msg + "\n");
			writer.flush();
		}
	}

	// 从服务器接收消息
	public String receive() throws IOException {
		String msg = null;
		if (!socket.isInputShutdown()) {
			msg = reader.readLine();
		}
		return msg;
	}

	// 检查用户是否准备退出
	public boolean readyToQuit(String msg) {
		return "quit".equals(msg);
	}

	public void close() {
		if (writer != null) {
			try {
				System.out.println("关闭socket");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {

		try {
			socket = new Socket("127.0.0.1", 8888);

			// 创建IO流
			reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream())
			);
			writer = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())
			);

			// 处理用户的输入
			new Thread(new UserInputHandler(this)).start();

			// 读取服务器转发的消息
			String msg;
			while ((msg = receive()) != null) {
				System.out.println(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
}