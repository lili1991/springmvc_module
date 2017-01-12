package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.spring.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by liangll18922 on 2017/1/11.
 */
@Controller
public class RegisterSimpleFormController{

    @RequestMapping("/register")
    public ModelAndView registUser(){
        HashMap map=new HashMap();
        List<String> cityList=new ArrayList();
        cityList.add("北京");
        cityList.add("上海");
        cityList.add("南京");
        cityList.add("杭州");
        map.put("cityList",cityList);
        System.out.print("register~~~~~~~~~~~~~~`");
        return new ModelAndView("register", map);
    }

}
