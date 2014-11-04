package manager.datamanager.searchfilter;

import java.time.LocalDateTime;

import data.taskinfo.TaskInfo;

//@author A0113011L
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
        if (task.endDate == null || task.endTime == null) {
            return false;
        }
        
        LocalDateTime taskTime = LocalDateTime.of(task.endDate, task.endTime);
        return minTime.compareTo(taskTime) <= 0 && 
                taskTime.compareTo(maxTime) <= 0;
    }
}
