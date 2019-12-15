package application.server.http;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应
 * HttpResponse的每一个实例用于表示一个服务端发送给客户端
 * 的响应内容。
 * 一个Http响应应当包含:状态行，响应头，响应正文
 */
public class HttpResponse {
    
    private OutputStream out;
    private Socket socket;
    //    定义状态行 三项
    //                协议版本
    private String protorol;
    //               状态代码
    private Integer statusCode;
    //             状态描述
    private String statusReason;
    //响应头
    private Map<String, String> responseHeaders = new HashMap<>();
    //响应实体
    private File entity;
    
    public HttpResponse(Socket socket) {
        this.socket = socket;
        try {
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //1发送状态行
    //                protorol  status_code  status_reason(CRLF)
    //                协议版本  状态代码         状态描述
    private void sendResponseLine() {
        printLine("HTTP/1.1" + " " + this.statusCode + " " + statusReason);
    }
    
    //2 发送响应头
    private void sendResponseHeader() {
        responseHeaders.forEach((key , value) -> printLine(key + ": " + value));
        //单独发送一组CR LF 表示响应头发送完毕
        printLine("");
    }
    
    //3 发送正文,将文件数据发送给客户端
    private void sendResponseEntity() {
        if (entity != null) {
            try (InputStream in = new FileInputStream(entity)) {
                byte[] bytes = new byte[10240];
                while (in.read(bytes) != - 1) {
                    out.write(bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void printLine(String line) {
        try {
            out.write(line.getBytes("ISO8859-1"));
            out.write(HttpContext.CR);
            out.write(HttpContext.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void flush() {
        sendResponseLine();
        sendResponseHeader();
        sendResponseEntity();
    }
    
    public void addResponseHeader(String key , String value) {
        this.responseHeaders.put(key , value);
    }
    
    public void setEntity(File entity) {
        this.entity = entity;
    }
    
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        this.statusReason = HttpContext.getStatusReason(statusCode);
    }
    
    /**
     * 使客户端重定向到指定路径
     * 重定向要求响应的状态代码为302,也有其他几种细分的状态码具体参考相关文档。
     * 重定向要求响应头中包含:Location,对应的值就是要求客户端再次发起请求访问的地址。
     * 重定向的响应可以没有响应正文部分。
     */
    public void sendRedirect(String url) {
        //设置状态代码
        this.setStatusCode(302);
        //设置响应头
        this.addResponseHeader("Location" , url);
    }
}
