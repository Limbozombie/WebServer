package Limbo.server.servlet;

import Limbo.server.http.HttpContext;
import Limbo.server.http.HttpRequest;
import Limbo.server.http.HttpResponse;
import java.io.File;

public abstract class HttpServlet {
    
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    
    public abstract void service(HttpRequest request , HttpResponse response);
    
    protected void forward(String path , HttpRequest request , HttpResponse response) {
        File file = new File(path);
        response.setStatusCode(200);
        response.addResponseHeader("Content-Type" , HttpContext.getMimeType("html"));
        response.addResponseHeader("Content-Length" , String.valueOf(file.length()));
        response.setEntity(file);
    }
}
