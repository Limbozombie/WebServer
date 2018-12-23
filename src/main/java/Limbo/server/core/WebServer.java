package Limbo.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    
    private ExecutorService threadPool;
    private ServerSocket serverSocket;
    
    private WebServer() {
        try {
            // 协议 ip 端口  http://localhost:8080
            serverSocket = new ServerSocket(8080);
            threadPool = Executors.newFixedThreadPool(50);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void start() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                //将任务指派给线程池
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        WebServer server = new WebServer();
        System.out.println("启动服务器成功...");
        server.start();
    }
}
