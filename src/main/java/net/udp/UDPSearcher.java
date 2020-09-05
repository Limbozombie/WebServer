package net.udp;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author 271636872@qq.com
 * @since 2020/9/5 9:48
 */

@AllArgsConstructor
public class UDPSearcher {

	private final int PORT;


	public void search() {
		try (DatagramSocket receiver = new DatagramSocket(PORT)) {
			byte[] container = new byte[1024];
			DatagramPacket packet = new DatagramPacket(container, container.length);
			receiver.receive(packet);
			byte[] receivedData = packet.getData();
			int length = packet.getLength();
			System.out.println("收到了:" + new String(receivedData, 0, length));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
