package net;


import net.io.bio.BIOClient;
import net.io.bio.BIOServer;
import net.udp.UDPProvider;
import net.udp.UDPSearcher;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

/**
 * @author 271636872@qq.com
 * @since 2020/9/5 9:30
 */
public class NetTest {

	@Test
	public void tcp() {

		String host = "localhost";
		int port = 65432;
		String data = "something";

		BIOServer server = new BIOServer(port);
		CompletableFuture.runAsync(server::start);

		BIOClient client = new BIOClient(host, port);
		client.send(data);
	}

	@Test
	public void udp() {

		String ip = "localhost";
		int receiver_port = 3333;
		int sender_port = 2222;
		String data = "something";

		UDPSearcher searcher = new UDPSearcher(receiver_port);
		CompletableFuture.runAsync(searcher::search);

		UDPProvider provider = new UDPProvider(sender_port, ip, receiver_port);
		provider.send(data);
	}

}
