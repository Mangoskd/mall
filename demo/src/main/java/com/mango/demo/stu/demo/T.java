package com.mango.demo.stu.demo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Mango
 * @Date 2022/7/27 22:42
 */
public class T {
    public static void main(String[] args) {
        List<YearNum> years = new ArrayList<>();
        Integer startYear = 2021;
        for (int i = 0; i < 15; i++) {
            startYear = startYear+i;
            if(i % 2 == 0){
                years.add(new YearNum(2021, "a",BigDecimal.valueOf(i)));
            }else{
                years.add(new YearNum(2022,"b",BigDecimal.valueOf(i)));
            }

        }


        Map<String, BigDecimal> numByYear = years.stream()
                .collect(Collectors.groupingBy(o -> o.getYear()+o.getTest(),
                        Collectors.reducing(BigDecimal.ZERO, YearNum::getNum, BigDecimal::add)));
        System.out.println(numByYear);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static
    class YearNum{
        private Integer year;
        private String test;
        private BigDecimal num;
    }
}
