package input_output;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author gulh
 * @since 2020/09/03 12:46
 */
public class BIOServer {


	//	public static void main(String[] args) {
//		try {
//			ServerSocket serverSocket = new ServerSocket(5566);
//			while (true) {
//
//				Socket socket = serverSocket.accept();
//				// 读取
//				DataInputStream input = new DataInputStream(socket.getInputStream());
//				String clientData= input.readUTF();
//				System.out.println("客户端发送过来的是: " +clientData);
//
//
//				//向服务器端发送数据
//				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//				System.out.print("请输入: \t");
//				String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
//				out.writeUTF(str);
//
//
//
//				socket.close();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(5566);
		while (true) {
			try {

				Socket socket = serverSocket.accept();
				InputStream input = socket.getInputStream();
				System.out.println("客户端发送过来的是: " + read(input));

				OutputStream outputStream = socket.getOutputStream();

				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

				writer.write("received");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
