package application.mvc.web;

import application.mvc.common.Handler;
import application.mvc.common.HandlerMapping;
import application.mvc.common.ViewResolver;
import application.server.http.HttpRequest;
import application.server.http.HttpResponse;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制器
 * a.负责接收请求(.do结尾的请求),然后依据
 * HandlerMapping的配置，调用对应的处理器
 * (比如HelloController)来处理该请求。
 * b.将处理器的返回结果交给视图解析器(ViewResolver)
 * 来解析，然后调用对应的视图来展现处理结果。
 */
public class DispatcherServlet {
    
    private HandlerMapping handlerMapping = new HandlerMapping();
    private ViewResolver viewResolver = new ViewResolver();
    
    public DispatcherServlet() {
    }
    
    public void init() {
        /*
         * 读取配置文件(mvc.xml)的内容,
         * 然后将这些类(即处理器)实例化。接下来，
         * 将这些处理器实例放到一个集合里面，然后
         * 将这个集合交给HandlerMapping来处理。
         */
        try {
            Document document = new SAXReader().read(new File("src\\main\\resources\\conf\\mvc.xml"));
            Element rootElement = document.getRootElement();
            List<Element> beans = rootElement.elements("bean");
            List<Object> objects = new ArrayList<>();
            for (Element e : beans) {
                String bean = e.attributeValue("class");
                Object o = Class.forName(bean).newInstance();
                objects.add(o);
            }
            handlerMapping.process(objects);
        } catch (DocumentException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void service(HttpRequest request , HttpResponse response) {
        //获得请求路径
        //        String requestURI = request.getRequestURI();
        String requestURI = "/hello";
        //根据路径获得Handler
        Handler handler = handlerMapping.getHandler(requestURI);
        //根据Handler获得控制器
        Object o = handler.getController();
        //获得控制器的方法(方法和路径对应,所以只有一个)
        Method method = handler.getMethod();
        //获得方法的所有参数的类型
        Class<?>[] types = method.getParameterTypes();
        //处理器方法的返回值
        Object returnVal = null;
        try {
            if (types.length == 0) {
                returnVal = method.invoke(o);
            } else {
                //构造一个存放参数的容器
                Object[] parameters = new Object[types.length];
                for (int i = 0 ; i < types.length ; i++) {
                    if (types[i] == HttpRequest.class) {
                        parameters[i] = request;
                    }
                    if (types[i] == HttpResponse.class) {
                        parameters[i] = response;
                    }
                }
                returnVal = method.invoke(o , parameters);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        viewResolver.processView((String) returnVal , request , response);
    }
}
