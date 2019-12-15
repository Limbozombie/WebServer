package application.mvc.common;

import application.server.http.HttpRequest;
import application.server.http.HttpResponse;

/**
 * 视图解析器
 * 负责处理视图名，会依据视图名
 * 　　生成对应的地址，然后转发或者重定向到
 * 对应的地址。
 */
public class ViewResolver {
    
    public void processView(String viewName , HttpRequest request , HttpResponse response) {
        if (viewName.startsWith("redirect:")) {
            viewName = viewName.replace("redirect:" , "");
            response.sendRedirect(viewName);
        } else {
            //todo 转发
            System.out.println(viewName);
        }
    }
}
