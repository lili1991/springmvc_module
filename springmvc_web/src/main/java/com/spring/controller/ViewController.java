package com.spring.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liangll18922 on 2017/1/11.
 */

@Controller
public class ViewController {

    @RequestMapping("/view")
    public String view(){
        System.out.println("congratulations~");
        return "index";
    }
}