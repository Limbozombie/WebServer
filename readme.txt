todo

完成用户注册功能

1:在webapps/myweb/目录中添加一个注册页面reg.html
由于reg.html里存在一个form表单，并将用户输入的注册信息
以GET请求形式提交到服务端，那么这时候请求的地址中会包含
请求的路径以及对应的用户数据，格式如:
/myweb/reg?username=fanchuanqi&password=123456&nickname=fancq&phonenumber=12345678911
为此，服务端在解析请求时要对请求行中的url部分做进一步的
解析工作。

3:创建包com.webserver.servlet,并在该包中添加用于处理注册
    业务的类RegServlet，并在其中完成service方法处理注册。

4:在ClientHandler中添加针对requestURI请求的分支判断，若
    请求的是/myweb/reg则实例化RegServlet并调用service方法
    处理该注册业务。否则执行原有的响应静态资源的代码。
