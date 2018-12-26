package Limbo.mvc.common;

import Limbo.mvc.annotation.RequestMapping;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射处理器
 * 利用java反射读取处理器(比如HelloController)中的
 * RequestMapping 注解中的信息，然后建立请求路径与
 * 处理器及方法的对应关系。
 * 比如说:如果请求路径是"/hello.do"，那么应该由
 * HelloController的hello方法来处理。
 */
public class HandlerMapping {
    
    private Map<String, Handler> map = new HashMap<>();
    
    /**
     * 依据请求路径，返回Handler对象。
     */
    public Handler getHandler(String path) {
        return map.get(path);
    }
    
    /**
     * 遍历集合(存放的是处理器实例,比如HelloController
     * 对象),然后建立请求路径与处理器及方法的对应关系
     */
    public void process(List beans) {
        for (Object bean : beans) {
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String path = requestMapping.value();
                map.put(path , new Handler(bean , method));
            }
        }
    }
}
