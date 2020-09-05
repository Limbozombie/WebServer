package net.io.bio;

import application.utils.CommonUtils;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author gulh
 * @since 2020/09/03 12:44
 */
@AllArgsConstructor
public class BIOClient {

	private final String HOST;
	private final int PORT;

	public void send(String data) {

		if (data == null) {
			throw new IllegalArgumentException("data is null");
		}
		try (
				Socket socket = new Socket(HOST, PORT);
				OutputStream outputStream = socket.getOutputStream();
				InputStream input = socket.getInputStream()
		) {
			outputStream.write(data.getBytes());
			socket.shutdownOutput();

			System.out.println("服务器端返回过来的是: " + CommonUtils.read(input));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
