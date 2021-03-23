package per.eter.utils.datetime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import per.eter.utils.datetime.intf.DayOfMonthNum2Str;
import per.eter.utils.datetime.intf.MonthNum2Str;
import per.eter.utils.datetime.intf.impl.DayOfMonthNum2Str_CN;
import per.eter.utils.datetime.intf.impl.MonthNum2Str_CN;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class SimpleMonth implements Serializable {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime relativeLocalDateTime;
    private long monthOffset = 0;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startLocalDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endLocalDateTime;

    private int monthOfYear;
    private boolean currentMonth;
    private Map<String, SimpleDay> daysOfMonth = new LinkedHashMap<>();


    public SimpleMonth() {

    }

    public SimpleMonth(LocalDateTime localDateTime, long monthOffset) {
        this.relativeLocalDateTime = localDateTime;
        this.monthOffset = monthOffset;

        this.startLocalDateTime = DateTimeUtils.dayOfMonth(relativeLocalDateTime, this.monthOffset, TemporalAdjusters.firstDayOfMonth(), 0, 0, 0);
        this.endLocalDateTime = DateTimeUtils.dayOfMonth(relativeLocalDateTime, this.monthOffset, TemporalAdjusters.lastDayOfMonth(), 23, 59, 59);

        SimpleDay simpleDay = new SimpleDay(startLocalDateTime);
        while (0 >= simpleDay.getEndLocalDateTime().compareTo(this.getEndLocalDateTime())) {
            daysOfMonth.put(simpleDay.getDateStr(), simpleDay);
            simpleDay = new SimpleDay(simpleDay, 1);
        }

        this.defaultInit();
    }

    public SimpleMonth(LocalDateTime localDateTime) {
        this(localDateTime, 0);
    }

    public SimpleMonth(SimpleMonth simpleMonth) {
        this(simpleMonth.getStartLocalDateTime(), simpleMonth.getMonthOffset());
    }

    public void defaultInit() {
        this.setMonthOfYear();
    }

    private void setMonthOfYear() {
        int i = DateTimeUtils.monthOfYear(this.startLocalDateTime);
        this.setMonthOfYear(i);
        if (this.getMonthOfYear() == (LocalDateTime.now().getMonthValue())) {
            this.setCurrentMonth(true);
        }
    }


}
