package per.eter.utils.datetime;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static per.eter.utils.datetime.DateTimeUtils.*;

@Data
public class SimpleDayOfWeek {
    private LocalDateTime startLocalDateTime;
    private LocalDateTime endLocalDateTime;
    private long dayDurationNow;

    private DayOfWeek dayOfWeek;
    private String dayOfWeekZh;
    private String dateStr;
    private Object bzBean;

    public SimpleDayOfWeek(SimpleWeek simpleWeek, DayOfWeek dayOfWeek) {
        this.startLocalDateTime = DateTimeUtils.dayOfWeek(simpleWeek.getRelativeLocalDateTime(), simpleWeek.getWeekOffset(), dayOfWeek, 0, 0, 0);
        this.endLocalDateTime = DateTimeUtils.dayOfWeek(simpleWeek.getRelativeLocalDateTime(), simpleWeek.getWeekOffset(), dayOfWeek, 23, 59, 59);
        this.dayOfWeek = dayOfWeek;
        this.setDayOfWeekZh();
        this.setDateStr();
        this.setDayDurationNow();
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
}
