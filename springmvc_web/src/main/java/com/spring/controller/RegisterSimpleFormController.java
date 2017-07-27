package com.spring.controller;

import com.google.gson.Gson;
import com.spring.model.User;
import com.spring.service.UserService;
import mybatis.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by liangll18922 on 2017/1/11.
 */
@Controller
public class RegisterSimpleFormController{
    @Autowired
    private UserService userservice;

    @RequestMapping("/register")
    public ModelAndView registUser(){
        HashMap map=new HashMap();
        String [] citys={"北京","上海","南京","杭州","成都","重庆","济南","合肥","天津","大连","三亚","广州"};
        int length=citys.length;
        for(int i=0;i<1000000;i++){
            User user=new User();
            user.setEmail("user"+i+"@qq.com");
            int index=(int)(Math.random()*length);
            user.setCity(citys[index]);
            user.setUserName("user"+i);
            user.setPassword("123456");
            userservice.insertUser("insertUserInfo",user);
        }
        System.out.println("register~~~~~~~~~~~~~~");
        return new ModelAndView("register", map);
    }

    @RequestMapping("/loadUserInfo")
    public ModelAndView loadUserInfo(){
        HashMap params=new HashMap();
        Page page=new Page();
        page.setPageNo(10);
        page.setPageSize(2);
        params.put("limit", 10);
        params.put("offset", 2);
        List<User> userInfoList=userservice.fetchUser("selectPageUserInfo", params);
        for(User user:userInfoList){
            System.out.println(new Gson().toJson(user));
        }
        System.out.println("loadUserInfo~~~~~~~~~~~~~~");
        return new ModelAndView("register", params);
    }

    @RequestMapping("/exportBigData")
    public void exportBigData(HttpServletResponse response){
        try {
//            XSSFWorkbook sxssfWorkbook  = new XSSFWorkbook();
            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(50);
            sxssfWorkbook.createSheet();
            Sheet first =  sxssfWorkbook.getSheetAt(0);
            System.out.println("start select:"+ System.currentTimeMillis());
            List<User> userInfoList=userservice.fetchAll();
            System.out.println("end   select:"+ System.currentTimeMillis());
            String colummName[]={"姓名","邮箱","城市"};
            for (int i = 0; i < userInfoList.size(); i++) {
                Row row = first.createRow(i);
                for (int j = 0; j < 3; j++) {
                    if(i == 0) {
                        // 首行
                        row.createCell(j).setCellValue(colummName[j]);
                    } else {
                        // 数据
                        if (j == 0) {
                            CellUtil.createCell(row, j,userInfoList.get(i).getUserName());
                        }else if (j == 1) {
                            CellUtil.createCell(row, j, userInfoList.get(i).getEmail());
                        } else
                            CellUtil.createCell(row, j, userInfoList.get(i).getCity());
                    }
                }
            }
            FileOutputStream out = new FileOutputStream("D:/test.xlsx");

//            String filename = java.net.URLEncoder.encode("用户信息", "UTF-8");
//            filename += ".xlsx";
//            response.setContentType("application/vnd.ms-excel");
//            response.setHeader("Content-disposition", "attachment;filename=" + filename);
//            OutputStream out =response.getOutputStream();
            sxssfWorkbook.write(out);
            out.close();
            System.out.println("end   export:" + System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/changeExcelToTxt")
    public void changeExcelToTxt(){

        try {
            XSSFWorkbook workbook   = new XSSFWorkbook(new FileInputStream(new File("C:/Users/liangll18922/Desktop/行情信息.xlsx")));
            Sheet sheet = workbook.getSheetAt(0);
            File file = new File("d:/1.txt");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            // j为行数,y为列数
            int j = sheet.getLastRowNum();
            int y = 0;
            if(sheet.getRow(0)!=null){
                y=sheet.getRow(0).getLastCellNum();
            }
            for (int i = 0; i < j; i++) {
                Row row = sheet.getRow(i);
                for(int x=0; x<y; x++){

                    Cell c = (Cell) row.getCell(x);
                    String s =c.getStringCellValue();
                    bw.write(s);
                    bw.write(" ");
                    bw.flush();
                }
                bw.newLine();
                bw.flush();
            }
            System.out.println("写入结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/importExcel")
    public void importExcel(){
        try {
            FileInputStream fileIn = new FileInputStream("D:/work/bz.xls");
//根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb0 = new HSSFWorkbook(fileIn);
//获取Excel文档中的第一个表单
            Sheet sht0 = wb0.getSheetAt(0);
//对Sheet中的每一行进行迭代
            Map parameters =new HashMap();
            for (Row r : sht0) {
               if(StringUtils.isEmpty(r.getCell(0))){
                   break;
               }
                User user=new User();
                user.setCity(r.getCell(2)+"");
                user.setUserName(r.getCell(0)+"");
                user.setPassword(r.getCell(1)+"");
                userservice.insertUser("insertDictionaryInfo",user);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
