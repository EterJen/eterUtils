package per.eter.utils.datetime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class SimpleWeek {
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //@JsonProperty("start_time")
    private LocalDateTime relativeLocalDateTime;
    private long weekOffset;


    private LocalDateTime startLocalDateTime;
    private LocalDateTime endLocalDateTime;
    private ArrayList<SimpleDayOfWeek> daysOfWeek = new ArrayList<>();

    public SimpleWeek(SimpleWeek simpleWeek) {
        this.relativeLocalDateTime = simpleWeek.relativeLocalDateTime;
        this.weekOffset = simpleWeek.weekOffset;

        this.startLocalDateTime = DateTimeUtils.dayOfWeek(relativeLocalDateTime, this.weekOffset, DayOfWeek.MONDAY, 0, 0, 0);
        this.endLocalDateTime = DateTimeUtils.dayOfWeek(relativeLocalDateTime, this.weekOffset, DayOfWeek.SUNDAY, 23, 59, 59);

        DayOfWeek[] dayOfWeeks = DayOfWeek.values();
        for (DayOfWeek dayOfWeek : dayOfWeeks) {
            daysOfWeek.add(new SimpleDayOfWeek(simpleWeek, dayOfWeek));
        }

    }

    public SimpleWeek() {

    }
}
