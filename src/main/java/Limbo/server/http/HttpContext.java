package Limbo.server.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Http协议相关内容定义
 *
 * 实现客户端请求一个静态资源时
 * 可以在response中对应的Content-Type头给定的值与该静态资源相符。
 */
public class HttpContext {
    
    //回车符
    public static final int CR = 13;
    // 换行符
    public static final int LF = 10;
    /**
     * 介质类型映射
     * key:资源后缀名
     * value:Content-Type对应的值
     */
    private static Map<String, String> mimeType = new HashMap<>();
    private static Map<Integer, String> statusCodeReason = new HashMap<>();
    /*
     * 静态块，通常用来初始化静态成员
     */
    static {
        initMimeType();
        initStatusCodeReason();
    }
    /**
     * 初始化 状态码--描述 映射
     */
    private static void initStatusCodeReason() {
        statusCodeReason.put(200 , "OK");
        statusCodeReason.put(302 , "Moved Temporarily");
        statusCodeReason.put(404 , "Not Found");
        statusCodeReason.put(500 , "Internal Server Error");
    }
    
    /**
     * 获取给定的资源类型所对应的Content-Type值
     */
    public static String getStatusReason(int code) {
        return statusCodeReason.get(code);
    }
    
    /**
     * 初始化介质类型映射
     * 常见的介质类型定义:
     * ico         image/x-icon
     * html       text/html
     * css         text/css
     * png        image/png
     * gif         image/gif
     * jpg         image/jpeg
     * js          application/javascript
     */
    private static void initMimeType() {
        //     tomcat.xml来自  tomcat安装目录中的conf/web.xml文件
        File file = new File("src\\main\\webapps\\WEB-INF\\tomcat.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(file);
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements();
            //   利用dom4j 解析web.xml文档
           /*   将如下父标签<mime-mapping>中
              子标签<extension>的内容   js   作为 mimeType中 key
              子标签<mime-type>的内容  application/javascript 作为 mimeType 中 value
           */
            //   <mime-mapping>
            //        <extension>js</extension>
            //        <mime-type>application/javascript</mime-type>
            //    </mime-mapping>
            //这里用到了java8 的stream API
            elements.stream().filter(e -> "mime-mapping".equals(e.getName())).forEach(e -> mimeType.put(e.elementText("extension") , e.elementText("mime-type")));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取给定的资源类型所对应的Content-Type值
     */
    public static String getMimeType(String suffixName) {
        return mimeType.get(suffixName);
    }
}
