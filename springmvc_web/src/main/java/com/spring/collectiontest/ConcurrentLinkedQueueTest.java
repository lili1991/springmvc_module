package com.spring.collectiontest;

import com.alibaba.druid.support.json.JSONUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by liangll18922 on 2017/4/21.
 */
public class ConcurrentLinkedQueueTest {
    public static void main(String arg[]){
//        ConcurrentLinkedQueue queue=new ConcurrentLinkedQueue();
//        queue.add("1");
//        queue.add("2");
//        queue.offer("3");
//        queue.offer("4");
//        System.out.println(JSONUtils.toJSONString(queue));




        HashMap map=new HashMap<>();

        map.put("1","a");
        map.put("2","b");
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()){
            System.out.println(map.get("1"));
            it.next();
            it.remove();
        }
    }
}
