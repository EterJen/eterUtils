package per.eter.utils.datetime;

import lombok.Data;
import org.hothub.calendarist.Calendarist;
import org.hothub.calendarist.pojo.LunarDate;
import per.eter.utils.datetime.intf.DayOfMonthNum2Str;
import per.eter.utils.datetime.intf.MonthNum2Str;
import per.eter.utils.datetime.intf.impl.DayOfMonthNum2Str_CN;
import per.eter.utils.datetime.intf.impl.MonthNum2Str_CN;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import static per.eter.utils.datetime.DateTimeUtils.*;

@Data
public class SimpleDay  implements Serializable {
    private LocalDateTime startLocalDateTime;
    private LocalDateTime endLocalDateTime;
    private long dayDurationNow;
    private long offset = 0;
    private DayOfWeek dayOfWeek;
    private String dayOfWeekZh;
    private String dateStr;
    private String lunarDateStr;
    private Object bzBean;

    private DayOfMonthNum2Str dayOfMonthNum2Str = new DayOfMonthNum2Str_CN();
    private MonthNum2Str monthNum2Str = new MonthNum2Str_CN();


    public void defaultInit() {
        this.dayOfWeek = startLocalDateTime.getDayOfWeek();
        this.setDayOfWeekZh();
        this.setDateStr();
        this.setDayDurationNow();
        this.initLunarDateStr();
    }

    public SimpleDay() {
    }

    public SimpleDay(LocalDateTime localDateTime) {
        this.setStartLocalDateTime(localDateTime.withNano(0).withHour(0).withMinute(0).withSecond(0));
        this.setEndLocalDateTime(localDateTime.withNano(0).withHour(23).withMinute(59).withSecond(59));
        defaultInit();
    }

    public SimpleDay(SimpleWeek simpleWeek, DayOfWeek dayOfWeek) {
        this.startLocalDateTime = DateTimeUtils.dayOfWeek(simpleWeek.getRelativeLocalDateTime(), simpleWeek.getWeekOffset(), dayOfWeek, 0, 0, 0);
        this.endLocalDateTime = DateTimeUtils.dayOfWeek(simpleWeek.getRelativeLocalDateTime(), simpleWeek.getWeekOffset(), dayOfWeek, 23, 59, 59);
        defaultInit();
    }

    public SimpleDay(SimpleDay simpleDay, long offset) {
        this.startLocalDateTime = simpleDay.startLocalDateTime.plusDays(offset);
        this.endLocalDateTime = simpleDay.endLocalDateTime.plusDays(offset);
        defaultInit();
    }

    private void setDayDurationNow() {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startLocalDateTime = this.getStartLocalDateTime();
        Duration duration = Duration.between(localDateTime, startLocalDateTime);
        long l = duration.toDays();
        this.setDayDurationNow(l);
    }

    public void setDateStr() {
        this.dateStr = DATE.format(startLocalDateTime);
    }

    public static ArrayList<String> dateStrsOfRange(LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime) {
        ArrayList<String> strings = new ArrayList<>();
        String start = DATE.format(startLocalDateTime);
        String end = DATE.format(endLocalDateTime);
        strings.add(start);
        long i = 1;
        while (!end.equals(start)) {
            LocalDateTime localDateTime = startLocalDateTime.plusDays(i++);
            start = DATE.format(localDateTime);
            strings.add(start);
        }
        return strings;
    }

    public static ArrayList<String> dateStrsOfRange(Date startDateTime, Date endDateTime) {
        return dateStrsOfRange(date2Ldt(startDateTime), date2Ldt(endDateTime));
    }

    public void setDayOfWeekZh() {
        switch (this.dayOfWeek) {
            case MONDAY:
                this.dayOfWeekZh = "星期一";
                break;
            case TUESDAY:
                this.dayOfWeekZh = "星期二";
                break;
            case WEDNESDAY:
                this.dayOfWeekZh = "星期三";
                break;
            case THURSDAY:
                this.dayOfWeekZh = "星期四";
                break;
            case FRIDAY:
                this.dayOfWeekZh = "星期五";
                break;
            case SATURDAY:
                this.dayOfWeekZh = "星期六";
                break;
            case SUNDAY:
                this.dayOfWeekZh = "星期日";
                break;
        }

    }

    public void initLunarDateStr() {
        Date date = ldt2Date(startLocalDateTime);
        long time = date.getTime();
        Calendarist calendarist = Calendarist.fromSolar(time);
        LunarDate lunarDate = calendarist.toLunar();
        this.setLunarDateStr(monthNum2Str.monthNum2Str(lunarDate.getMonth())+dayOfMonthNum2Str.dayOfMonthNum2Str(lunarDate.getDay()));
    }
}
