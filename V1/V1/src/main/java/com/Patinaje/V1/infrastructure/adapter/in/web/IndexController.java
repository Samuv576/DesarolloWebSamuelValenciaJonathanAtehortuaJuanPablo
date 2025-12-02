package com.Patinaje.V1.infrastructure.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/index")
public class IndexController{
    @GetMapping("/home")
    public String  Home () {
        return "index";
    }
    
}