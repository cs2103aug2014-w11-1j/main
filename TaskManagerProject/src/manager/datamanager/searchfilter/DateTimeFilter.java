package manager.datamanager.searchfilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import data.taskinfo.TaskInfo;

public class DateTimeFilter implements Filter{
    LocalTime minTime, maxTime;
    LocalDate minDate, maxDate;
    
    public DateTimeFilter(LocalTime minTime, LocalDate minDate,
            LocalTime maxTime, LocalDate maxDate) {
        this.minTime = minTime;
        this.minDate = minDate;
        this.maxTime = maxTime;
        this.maxDate = maxDate;
    }
    
    public Type getType() {
        return Type.FILTER_DATETIME;
    }
    
    public boolean filter(TaskInfo task) {
        LocalDateTime min = LocalDateTime.of(minDate, minTime);
        LocalDateTime max = LocalDateTime.of(maxDate, maxTime);
        LocalDateTime taskTime = LocalDateTime.of(task.endDate, task.endTime);
        return min.compareTo(taskTime) <= 0 && taskTime.compareTo(max) <= 0;
    }
}
