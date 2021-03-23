package per.eter.utils.datetime.intf.impl;

import per.eter.utils.datetime.intf.MonthNum2Str;

import java.io.Serializable;
import java.util.HashMap;

public class MonthNum2Str_CN implements MonthNum2Str, Serializable {
    private final static HashMap<String, String> monthMap = new HashMap<String, String>() {{
        put("1", "正月");
        put("2", "二月");
        put("3", "三月");
        put("4", "四月");
        put("5", "五月");
        put("6", "六月");
        put("7", "七月");
        put("8", "八月");
        put("9", "九月");
        put("10", "十月");
        put("11", "冬月");
        put("12", "腊月");
    }};

    @Override
    public String monthNum2Str(int monthOfYear) {
        return monthNum2Str("" + monthOfYear);
    }

    @Override
    public String monthNum2Str(String monthOfYear) {
        return monthMap.get(monthOfYear);
    }
}
