package net.io.nio.chatroom.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class ChatServer {


	private final int PORT = 7777;

	private final Charset utf8 = StandardCharsets.UTF_8;
	private Selector selector;

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.start();
	}

	public void start() {

		try {

			ServerSocketChannel server = ServerSocketChannel.open();
			//设置为非阻塞状态
			server.configureBlocking(false);

			server.bind(new InetSocketAddress(PORT));

			//todo 差别
			// server.socket().bind(new InetSocketAddress(PORT));


			selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);

			System.out.println("启动服务器，监听端口：" + PORT);

			while (true) {

				int readyChannel = selector.select();
				if (readyChannel == 0) {
					continue;
				}

				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				selectedKeys.forEach(this::handle);
				selectedKeys.clear();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			close();
		}
	}

	private void handle(SelectionKey key) {

		try {
			// ACCEPT事件 - 和客户端建立了连接
			if (key.isAcceptable()) {
				ServerSocketChannel channel = (ServerSocketChannel) key.channel();
				SocketChannel socketChannel = channel.accept();
				//设置为非阻塞模式
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ);
			} else
				// READ事件 - 客户端发送了消息
				if (key.isReadable()) {
					SocketChannel channel = (SocketChannel) key.channel();

					ByteBuffer buffer = ByteBuffer.allocate(1024);
					while (channel.read(buffer) != -1) {
						buffer.flip();
						while (buffer.hasRemaining()) {
							channel.socket().getOutputStream().write(buffer.array());
//							System.out.println(buffer.asCharBuffer());

						}
						buffer.clear();
					}
				}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


//	public synchronized void addClient(Socket socket) throws IOException {
//		if (socket != null) {
//			int port = socket.getPort();
//			BufferedWriter writer = new BufferedWriter(
//					new OutputStreamWriter(socket.getOutputStream())
//			);
//			connectedClients.put(port, writer);
//			System.out.println("客户端[" + port + "]已连接到服务器");
//		}
//	}
//
//	public synchronized void removeClient(Socket socket) throws IOException {
//		if (socket != null) {
//			int port = socket.getPort();
//			if (connectedClients.containsKey(port)) {
//				connectedClients.get(port).close();
//			}
//			connectedClients.remove(port);
//			System.out.println("客户端[" + port + "]已断开连接");
//		}
//	}
//
//	public synchronized void forwardMessage(Socket socket, String fwdMsg) throws IOException {
//		for (Integer id : connectedClients.keySet()) {
//			if (!id.equals(socket.getPort())) {
//				Writer writer = connectedClients.get(id);
//				writer.write(fwdMsg);
//				writer.flush();
//			}
//		}
//	}
//
//	public synchronized void close() {
//		if (serverSocket != null) {
//			try {
//				serverSocket.close();
//				System.out.println("关闭serverSocket");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}


}