package com.spring.collectiontest;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangll18922 on 2017/5/24.
 */
public class ArrayListTest {
    public static void main(String[] args){
        List<String> list1=new ArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        list1.add("d");
        list1.add("e");
        list1.add("f");
        list1.add("g");
        list1.add("h");
        list1.add("i");
        list1.add("j");
        list1.add("k");
        list1.add("l");



        List<String> list2=new ArrayList<>(2);
        list2.add("A");
        list2.add("B");
        list2.add("C");
        list2.add("D");
        list2.add("E");
        list2.add("F");

        String [] strs=new String[10];
        strs[0]="1";
        strs[1]="2";
        strs[2]="3";
        strs[3]="4";
        strs[4]="5";
        strs[5]="6";
        System.out.println(new Gson().toJson(strs)+"----------before");
        System.arraycopy(strs, 4, strs, 5, 2);
        strs[4]="dao";
        System.out.println(new Gson().toJson(strs));

    }
}
