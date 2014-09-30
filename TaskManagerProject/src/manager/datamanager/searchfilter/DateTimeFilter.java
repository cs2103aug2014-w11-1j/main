package manager.datamanager.searchfilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import data.taskinfo.TaskInfo;

public class DateTimeFilter implements Filter{
    LocalDateTime minTime;
    LocalDateTime maxTime;
    
    public DateTimeFilter(LocalDateTime minTime, LocalDateTime maxTime) {
        this.minTime = minTime;
        this.maxTime = maxTime;
    }
    
    public Type getType() {
        return Type.FILTER_DATETIME;
    }
    
    public boolean filter(TaskInfo task) {
        LocalDateTime taskTime = LocalDateTime.of(task.endDate, task.endTime);
        return minTime.compareTo(taskTime) <= 0 && 
                taskTime.compareTo(maxTime) <= 0;
    }
}
