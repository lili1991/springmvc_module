package com.spring.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liangll18922 on 2017/7/17.
 *
 * 如何用正则表达式求模板字符串替换前后的键值对
 */
public class RegecxTest {
    public static void main(String [] args){
        RegecxTest regecx=new RegecxTest();
        regecx.test();
    }

    public void test(){
        String tempStr = "(【工银信用卡】于${startTime}至${endTime}申办奋斗卡，无年费，赢郎平签名排球！详情${link}";
        String textStr ="(【工银信用卡】于昨天至今天申办奋斗卡，无年费，赢郎平签名排球！详情没有";
        Map<String, String> map = templateReplace(tempStr, textStr);
        // 遍历map
        for (Map.Entry<String, String> entry : map.entrySet()){
            System.out.println(entry.getKey() + " -- " + entry.getValue());
        }
    }

    public Map<String, String> templateReplace(String tempStr, String textStr){
        Map<String, String> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("\\$\\{.+?\\}");
        Matcher m = p.matcher(tempStr);
        // 找到占位符，并将其替换为(.*?)
        //模板字符串中变量的正则表达式
        String keyRegex = "\\$\\{.*?\\}";
        // **关键想法** 把模板字符串改装成正则表达式
        // 此处如果不加开始符^和结束符$的话，由于"?"是最短匹配，最后一个文本会是空字符串，即匹配不到示例中的  "没有"
        while(m.find()){
            list.add(m.group());
            map.put(m.group(), "");
            tempStr= "^" +tempStr.replaceAll(keyRegex, "(.*?)") + "$";
        }
        p = Pattern.compile(tempStr);
        m = p.matcher(textStr);
        if(m.find()){
            for (int i = 1; i <= m.groupCount(); i++){
                map.put(list.get(i - 1), m.group(i));
            }
        }
        return map;
    }
}
