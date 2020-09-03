package input_output;

import java.io.*;
import java.net.Socket;

/**
 * @author gulh
 * @since 2020/09/03 12:44
 */
public class BIOClient {


	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("localhost", 5566);

		//向服务器端发送数据
		OutputStream outputStream = socket.getOutputStream();

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));


		System.out.println("请输入");
		String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
		writer.write(str);
		writer.flush();
		//读取服务器端数据

		InputStream input = socket.getInputStream();
		System.out.println("服务器端返回过来的是: " + read(input));

		socket.close();

	}

	private static String read(InputStream input) throws IOException {

		StringBuilder stringBuilder = new StringBuilder();

		int ch;
		while ((ch = input.read()) != -1) {
			stringBuilder.append(ch);
		}

		return stringBuilder.toString();
	}
}
