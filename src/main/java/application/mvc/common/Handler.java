package application.mvc.common;

import java.lang.reflect.Method;

/**
 * 因为利用java反射，我们去调用一个方法，
 * 需要两个对象，一个是该方法对应的Method对象，
 * 另外一个就是该方法依附的java对象。
 * 所以，这儿设计了Handler类，用来封装执行某个方法
 * 需要的两个对象。
 */
public class Handler {
    
    private Object controller;
    private Method method;
    
    public Handler(Object controller , Method method) {
        this.method = method;
        this.controller = controller;
    }
    
    public Method getMethod() {
        return method;
    }
    
    public Object getController() {
        return controller;
    }
}
