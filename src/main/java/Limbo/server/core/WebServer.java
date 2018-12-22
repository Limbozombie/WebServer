package Limbo.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    
    private ServerSocket serverSocket;
    
    private WebServer() {
        try {
            // 协议 ip 端口  http://localhost:8080
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void start() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
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
