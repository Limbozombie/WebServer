package Limbo.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 * 该注解提供请求路径与处理器的对应关系。
 */
@Target(ElementType.METHOD)//注解加在方法上
@Retention(RetentionPolicy.RUNTIME)//注解保留到运行时
public @interface RequestMapping {
    
    //路径
    String value();
}
