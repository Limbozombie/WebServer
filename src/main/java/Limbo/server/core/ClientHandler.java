package Limbo.server.core;

import Limbo.server.http.HttpContext;
import Limbo.server.http.HttpRequest;
import Limbo.server.http.HttpResponse;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ClientHandler implements Runnable {
    
    private Socket socket;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    
    public void run() {
        /*
         * 处理客户端请求的大致流程
         
         * 3:分析请求内容并处理该请求,将处理结果(即将给客户端回复的内容)设置到相应对象中
         * 4:将相应对象中的内容以HTTP相应格式发送回给客户端
         */
        // 1:解析请求并用一个对象保存请求内容
        HttpRequest request = new HttpRequest(socket);
        // 2:创建响应对象，表示给客户端回复的内容
        HttpResponse response = new HttpResponse(socket);
        String url = request.getUrl();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        Map<String, String> requestParameters = request.getParameters();
        System.out.println(url);
        System.out.println(method);
        System.out.println(requestURI);
        requestParameters.forEach((a , b) -> System.out.println(a + "====" + b));
        /*
         * 得到url用户要请求的资源路径后，从webapps目录中找到对应该相对路径的资源。
         */
        File file = new File("src\\main\\webapps\\" + url.substring(1));
        //文件相对地址  是相对于  System.getProperty("user.dir")
        //        String absolutePath = file.getAbsolutePath();
        //        String userDir = System.getProperty("user.dir");
        //        System.out.println(absolutePath.equals(userDir + "src\\main\\webapps\\" + url.substring(1)));
        if (file.exists()) {
            String[] data = url.split("\\.");
            String suffixName = data[data.length - 1];
            response.setStatusCode(200);
            response.setResponseHeader("Content-Type" , HttpContext.getMimeType(suffixName));
        } else {
            file = new File("src\\main\\webapps\\NotFound.html");
            response.setStatusCode(404);
            response.setResponseHeader("Content-Type" , HttpContext.getMimeType("html"));
        }
        response.setResponseHeader("Content-Length" , String.valueOf(file.length()));
        response.setEntity(file);
        response.flush();
        //1.0协议中，一次请求响应完毕后与客户端断开连接
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



