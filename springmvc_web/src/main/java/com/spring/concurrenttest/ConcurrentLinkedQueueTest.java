package com.spring.concurrenttest;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by liangll18922 on 2017/5/10.
 */
public class ConcurrentLinkedQueueTest {

    public static void main(String args[]){
        ConcurrentLinkedQueue queue=new ConcurrentLinkedQueue();
        queue.offer("a1");
        queue.offer("a2");
        queue.offer("a3");
        queue.offer("a4");
        queue.offer("a5");


        queue.poll();
        queue.poll();
        queue.poll();
        queue.poll();
        queue.poll();
        queue.poll();

        String str="[{'functioncode':'产品销售商接口'},{'functioncode':'基金信息接口'}]";
        List<Map> list=JSONObject.parseArray(str, Map.class);
        System.out.print(list);
    }
}
