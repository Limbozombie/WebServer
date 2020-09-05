package net.udp;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @author 271636872@qq.com
 * @since 2020/9/5 9:48
 */

@AllArgsConstructor
public class UDPProvider {

	private final String RECEIVER_IP;
	private final int RECEIVER_PORT;


	public void send(String data) {
		if (data == null) {
			throw new IllegalArgumentException("data is null");
		}

		//不指定端口时由系统随机指定
		try (DatagramSocket client = new DatagramSocket()) {
			byte[] toSend = data.getBytes();
			int length = toSend.length;
			InetSocketAddress address = new InetSocketAddress(RECEIVER_IP, RECEIVER_PORT);
			DatagramPacket packet = new DatagramPacket(toSend, length, address);
			client.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
