package net.io.bio;

import application.utils.CommonUtils;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

/**
 * @author gulh
 * @since 2020/09/03 12:46
 */
@AllArgsConstructor
public class BIOServer {


	private final int PORT;

	public void start() {
		try (
				ServerSocket serverSocket = new ServerSocket(PORT)
		) {
			Socket socket = serverSocket.accept();
			CompletableFuture.runAsync(new Handler(socket));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class Handler implements Runnable {

		private final Socket socket;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try (
					InputStream input = socket.getInputStream();
					OutputStream outputStream = socket.getOutputStream()
			) {
				System.out.println("客户端发送过来的是: " + CommonUtils.read(input));
				outputStream.write("received".getBytes());
				socket.shutdownOutput();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
