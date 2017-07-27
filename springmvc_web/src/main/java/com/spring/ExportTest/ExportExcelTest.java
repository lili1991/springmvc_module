package com.spring.ExportTest;

/**
 * Created by liangll18922 on 2017/4/25.
 */
public class ExportExcelTest {

    public static void main(String[] args) throws Exception {
        long beginTime = System.currentTimeMillis();

        String path = "D:/test.xlsx";
        POIUtil.Excel2007AboveOperate(path);
        long endTime = System.currentTimeMillis();

        System.out.println("Cast time : " + (endTime - beginTime));
    }
}


