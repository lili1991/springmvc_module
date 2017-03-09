package com.spring.aoptest;


import org.springframework.stereotype.Component;

/**
 * Created by liangll18922 on 2017/3/9.
 */
@Component
public class VideoHelper {
    public VideoHelper(){

    }

    public void viewpoint(){
    }

    public void beforeVideo(){

        System.out.println("马上要看电影咯~~~");
    }

    public void afterVideo(){

        System.out.println("电影看完了……");
    }
}
