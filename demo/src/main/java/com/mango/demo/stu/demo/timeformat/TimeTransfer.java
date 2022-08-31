package com.mango.demo.stu.demo.timeformat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Mango
 * @Date 2022/9/1 1:08
 */
public class TimeTransfer {
    public static void main(String[] args) {
        String timeStr="30/08/2022";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = dateFormat.parse(timeStr);
            String format = simpleDateFormat.format(date);
            System.out.println(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
