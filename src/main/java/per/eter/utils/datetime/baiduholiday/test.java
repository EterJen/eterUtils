package per.eter.utils.datetime.baiduholiday;

import java.util.Date;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        BaiDuDateParse baiDuDateParse = new BaiDuDateParse();
        Map<Date, DayStatusEnum> dateDayStatusEnumMap = baiDuDateParse.holidayByList("2020");
        System.out.println(dateDayStatusEnumMap);
    }
}
