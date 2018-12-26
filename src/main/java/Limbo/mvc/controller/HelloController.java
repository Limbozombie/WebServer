package Limbo.mvc.controller;

import Limbo.mvc.annotation.RequestMapping;

public class HelloController {
    
    @RequestMapping("/hello")
    public String hello() {
        return "index.html";
    }
}
