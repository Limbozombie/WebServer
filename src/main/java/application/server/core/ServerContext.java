package application.server.core;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理 url 和 servlet的映射关系
 */
public class ServerContext {
    
    //url 和 servlet的映射关系
    private static Map<String, String> servletMapping = new HashMap<>();
    static {
        //初始化
        initServletMapping();
    }
    private static void initServletMapping() {
        File file = new File("src\\main\\resources\\conf\\servlets.xml");
        try {
            Element rootElement = new SAXReader().read(file).getRootElement();
            List<Element> elements = rootElement.elements("servlet");
            elements.forEach(element -> servletMapping.put(element.attributeValue("url") , element.attributeValue("class")));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    
    //    根据给定的请求路径获取对应的Servlet名字
    public static String getServletByUrl(String url) {
        return servletMapping.get(url);
    }
    
    //    判断该url是否对应一个Servlet
    public static boolean hasUrl(String url) {
        return servletMapping.containsKey(url);
    }
}
