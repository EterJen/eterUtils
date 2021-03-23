package per.eter.utils.datetime.intf.impl;

import per.eter.utils.datetime.intf.DayOfMonthNum2Str;

import java.io.Serializable;
import java.util.HashMap;

public class DayOfMonthNum2Str_CN implements DayOfMonthNum2Str , Serializable {
    private final static HashMap<String, String> dayMap = new HashMap<String, String>() {{
        put("1", "初一");
        put("2", "初二");
        put("3", "初三");
        put("4", "初四");
        put("5", "初五");
        put("6", "初六");
        put("7", "初七");
        put("8", "初八");
        put("9", "初九");
        put("10", "初十");
        put("11", "十一");
        put("12", "十二");
        put("13", "十三");
        put("14", "十四");
        put("15", "十五");
        put("16", "十六");
        put("17", "十七");
        put("18", "十八");
        put("19", "十九");
        put("20", "二十");
        put("21", "廿一");
        put("22", "廿二");
        put("23", "廿三");
        put("24", "廿四");
        put("25", "廿五");
        put("26", "廿六");
        put("27", "廿七");
        put("28", "廿八");
        put("29", "廿九");
        put("30", "三十");
    }};

    @Override
    public String dayOfMonthNum2Str(int dayOfMonth) {
        return dayOfMonthNum2Str("" + dayOfMonth);
    }

    @Override
    public String dayOfMonthNum2Str(String dayOfMonth) {
        return dayMap.get(dayOfMonth);
    }
}
