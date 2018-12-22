package Limbo.server.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * HttpRequest的每一个实例用于表示一个客户端发送过来的请求数据。
 * 一个请求包含内容为:请求行，消息头，消息正文信息。
 */
public class HttpRequest {
    
    //
    private InputStream in;
    private Socket socket;
    //    定义请求行 三项 信息
    /*请求方式*/
    private String method;
    /*请求资源路径*/
    private String url;
    /*协议版本*/
    private String protocol;
    //url中的请求地址部分,url中“?”左面的内容
    private String requestURI;
    //url中传递过来的参数部分,url中“?”右面的内容
    private String queryString;
    //保存解析后的所有参数
    private Map<String, String> parameters = new HashMap<>();
    //请求头
    private Map<String, String> requestHeaders = new HashMap<>();
    
    public HttpRequest(Socket socket) {
        this.socket = socket;
        try {
            in = socket.getInputStream();
            parseRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * 解析请求的三步
     * 一:  解析请求行
     *      包括    请求方式--请求资源路径--协议版本
     */
    private void parseRequestLine() {
        String requestLine = readLine();
        //        \s    匹配任何空白字符，包括空格、制表符、换页符等等
        String[] data = requestLine.split("\\s");
        this.method = data[0];
        //  todo    Exception in thread "Thread-1" java.lang.ArrayIndexOutOfBoundsException: 1
        this.url = data[1];
        parseURL();
        this.protocol = data[2];
    }
    
    /**
     * 进一步解析URL
     */
    private void parseURL() {
        // 如果  URL  是    http://localhost:8080/reg?username=root&password=123456
        if (url.contains("?")) {
            // this.url  是    /index.html/reg?username=root&password=123456
            String[] strings = url.split("\\?");
            this.requestURI = strings[0];
            this.queryString = strings[1];
            //        parameters  是  username=root&password=123456
            String[] keyAndValue = queryString.split("&");
            //       keyAndValue   是    {"username=root","password=123456"}
            for (String s : keyAndValue) {
                String[] split = s.split("=");
                this.parameters.put(split[0] , split[1]);
            }
        }
    }
    
    /*  解析请求的三步 之二
        二:解析请求头
      */
    private void parseHeaders() {
        /*
         * 使用readLine方法循环按行读取字符串，
         * 当读取到了空字符串时说明单独读取了CRLF，
         * 那么就可以停止读取了。
         * 然后将每个消息头的名字作为key
         * 消息头的值作为value存入到属性headers这个map中保存。
         */
        while (true) {
            String line = readLine();
            if ("".equals(line)) {
                break;
            }
            String[] data = line.split(": ");
            requestHeaders.put(data[0] , data[1]);
        }
    }
    //    todo 3:解析请求体
    
    //解析请求
    private void parseRequest() {
        parseRequestLine();
        parseHeaders();
    }
    
    /*从输入流中读取一行*/
    private String readLine() {
        StringBuilder stringBuilder = new StringBuilder();
        int CR = 0;
        int LF;
        try {
            while ((LF = in.read()) != - 1) {
                stringBuilder.append((char) LF);
                if (CR == 13 && LF == 10) {
                    break;
                }
                CR = LF;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString().trim();
    }
    
    public String getMethod() {
        return method;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getRequestURI() {
        return requestURI;
    }
    
    public Map<String, String> getParameters() {
        return parameters;
    }
}
