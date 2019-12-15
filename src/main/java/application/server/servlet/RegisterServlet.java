package application.server.servlet;

import application.server.http.HttpRequest;
import application.server.http.HttpResponse;

/**
 * 注册
 */
public class RegisterServlet extends HttpServlet {
    
    public RegisterServlet() {
    }
    
    @Override
    public void service(HttpRequest request , HttpResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        response.sendRedirect("src\\main\\webapps\\registerSuccess.html");
        //        forward("src\\main\\webapps\\registerSuccess.html" , request , response);
    }
}
