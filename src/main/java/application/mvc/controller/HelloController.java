package application.mvc.controller;

import application.mvc.annotation.RequestMapping;

public class HelloController {
    
    @RequestMapping("/hello")
    public String hello() {
        return "index.html";
    }
}
