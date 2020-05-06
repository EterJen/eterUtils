package per.eter.utils.datetime.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import per.eter.utils.datetime.DateTimeUtils;
import per.eter.utils.datetime.SimpleDayOfWeek;
import per.eter.utils.datetime.SimpleWeek;
import per.eter.utils.file.SimpFile;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;

public class Test {
    public static void main1(String[] args) {
        String s = DateTimeUtils.DATE_TIME.formatNow();
        System.out.println(s);
        Date parse = DateTimeUtils.DATE_TIME.parse(s);
        System.out.println(parse);

        Date date = DateTimeUtils.DATE_TIME.dayOfWeek(new Date(), 0, DayOfWeek.MONDAY, 0, 0, 0);
        System.out.println(DateTimeUtils.DATE_TIME.formatDate(date));
        date = DateTimeUtils.DATE_TIME.dayOfWeek(new Date(), 0, DayOfWeek.SUNDAY, 0, 0, 0);
        System.out.println("本星期天"+DateTimeUtils.DATE_TIME.formatDate(date));
        date = DateTimeUtils.DATE_TIME.dayOfMonth(new Date(), 1, TemporalAdjusters.firstDayOfMonth(), 0, 0, 0);
        System.out.println(DateTimeUtils.DATE_TIME.formatDate(date));
        date = DateTimeUtils.DATE_TIME.dayOfMonth(new Date(), -1, TemporalAdjusters.lastDayOfMonth(), 0, 0, 0);
        System.out.println(DateTimeUtils.DATE_TIME.formatDate(date));
        int i = DateTimeUtils.DATE_TIME.weekOfYear(date);
        System.out.println(i);
        i = DateTimeUtils.DATE_TIME.monthOfYear(date);
        System.out.println(i);
    }

    public static void main2(String[] args) {
        String s = DateTimeUtils.DATE_TIME.formatNow();
        LocalDateTime localDateTime = DateTimeUtils.DATE_TIME.parse2LocalTime(s);
        System.out.println(DateTimeUtils.DATE_TIME.format(localDateTime));
        LocalDateTime localDateTime1 = localDateTime.plusDays(2);
        System.out.println(DateTimeUtils.DATE_TIME.format(localDateTime1));

        ArrayList<String> strings = SimpleDayOfWeek.dateStrsOfRange(localDateTime, localDateTime1);

        System.out.println(strings);
    }

    public static void main3(String[] args) {
        String s = DateTimeUtils.DATE_TIME.formatNow();
        LocalDateTime localDateTime = DateTimeUtils.DATE_TIME.parse2LocalTime(s);
        SimpleWeek simpleWeek = new SimpleWeek();
        simpleWeek.setRelativeLocalDateTime(localDateTime);
        simpleWeek.setWeekOffset(1);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        SimpFile simplefile = new SimpFile();
        simplefile.setOriginalFilename("232手动.ds.pdf");
        System.out.println(simplefile.getNameWithoutSuffix());
        SimpleWeek simpleWeek1 = new SimpleWeek(simpleWeek);

        SerializeConfig config = new SerializeConfig();
        System.out.println(JSON.toJSONString(simpleWeek1,true));
    }

    public static void main(String[] args) {
        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime localDateTime = now1.withMonth(12).withDayOfMonth(33).withHour(22).withMinute(2);
        System.out.println(DateTimeUtils.DATE_CN.formatDate(DateTimeUtils.ldt2Date(now1)));
        String formatNow = DateTimeUtils.DATE_CN.formatDate(DateTimeUtils.ldt2Date(localDateTime));
        System.out.println(formatNow);
    }

}
