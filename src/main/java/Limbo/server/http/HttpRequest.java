package Limbo.server.http;

import Limbo.server.exception.EmptyRequestException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
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
    
    public HttpRequest(Socket socket) throws EmptyRequestException {
        try {
            this.socket = socket;
            in = socket.getInputStream();
            //解析请求三步骤:
            //1:解析请求行
            parseRequestLine();
            //  2:解析消息头
            parseHeaders();
            //  3:解析消息正文
            parseContent();
        } catch (EmptyRequestException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     * 解析请求的三步
     * 一:  解析请求行
     *      包括    请求方式--请求资源路径--协议版本
     */
    private void parseRequestLine() throws EmptyRequestException {
        String requestLine = readLine();
        if ("".equals(requestLine)) {
            throw new EmptyRequestException("空请求异常");
        }
        //        \s    匹配任何空白字符，包括空格、制表符、换页符等等
        String[] data = requestLine.split("\\s");
        // HTTP协议有说明，允许客户端发送一个空请求
        // 即:连接上服务端后并没有发送完整的请求内容。
        //     这会导致我们在解析HttpRequest时，读取请求行时读取到一个空字符串
        //  那么在正常拆分method,url,protorol后会出现下标越界。
        this.method = data[0];
        this.url = data[1];
        parseURL();
        this.protocol = data[2];
    }
    
    /**
     * 进一步解析URL
     */
    private void parseURL() {
        // 如果浏览器地址栏上的URL是    http://localhost:8080/reg?username=root&password=123456
        if (this.url.contains("?")) {
            String[] strings = url.split("\\?");
            this.requestURI = strings[0];
            if (strings.length > 1) {
                this.queryString = strings[1];
                parseParameters(this.queryString);
            }
        } else {
            this.requestURI = this.url;
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
    
    /** 3:解析请求体 */
    //    post请求适用于客户端提向服务端交附件,或含有用户敏感信息数据时使用
    //    post会将数据包含在请求的消息正文部分,并且会在消息头中出现两个用于说明消息正文的消息头
    //    Content-Type: application/x-www-form-urlencoded     说明消息正文传递的数据类型
    //    Content-Length:说明消息正文占用的字节量
    private void parseContent() {
        //        通过判断请求头中是否有Content-Length来判断请求体是否有内容
        if (requestHeaders.containsKey("Content-Length")) {
            String contentType = requestHeaders.get("Content-Type");
            //判断是否为form提交的数据
            if ("application/x-www-form-urlencoded".equals(contentType)) {
                try {
                    Integer length = Integer.valueOf(requestHeaders.get("Content-Length"));
                    byte[] temp = new byte[length];
                    in.read(temp);
                    String content = new String(temp , "iso-8859-1");
                    parseParameters(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /** 解析get 或 post 请求方式的请求参数 */
    private void parseParameters(String content) {
        try {
            // username=%E8%8C%83%E4%BC%A0%E5%A5%87&password=123456
            // decode方法对%XX这样的字符操作，其他不做解释。
            // 例如:%E8%8C%83%
            // 第一步先将它们还原为字节
            // E8         8C         83
            // 11101000   10001100   10000011
            // 再将这组字节按照UTF-8编码还原为字符:范
            content = URLDecoder.decode(content , "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] keyAndValue = content.split("&");
        for (String s : keyAndValue) {
            String[] split = s.split("=");
            if (split.length == 1) {
                this.parameters.put(split[0] , null);
            } else {
                this.parameters.put(split[0] , split[1]);
            }
        }
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
    
    public String getParameter(String name) {
        return parameters.get(name);
    }
}
