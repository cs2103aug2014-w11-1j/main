package manager.datamanager.searchfilter;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
    
    private boolean isMatchingStartEnd(TaskInfo task) {
        LocalDateTime start = LocalDateTime.of(task.startDate, task.startTime);
        LocalDateTime end = LocalDateTime.of(task.endDate, task.endTime);
        
        return end.compareTo(minTime) > 0 && start.compareTo(maxTime) < 0;
    }
    
    private boolean isMatchingEndDateOnly(TaskInfo task) {
        LocalDateTime start = LocalDateTime.of(task.endDate, 
                LocalTime.MIDNIGHT);
        LocalDateTime end = LocalDateTime.of(task.endDate.plusDays(1), 
                LocalTime.MIDNIGHT);
        
        return end.compareTo(minTime) > 0 && start.compareTo(maxTime) < 0;
    }
    
    private boolean isMatchingEndDateTime(TaskInfo task) {
        LocalDateTime taskTime = LocalDateTime.of(task.endDate, task.endTime);
        return minTime.compareTo(taskTime) <= 0 && 
                taskTime.compareTo(maxTime) <= 0;
    }
    
    private boolean isMatchingFloating() {
        return false;
    }
    
    public boolean isMatching(TaskInfo task) {
        if (task.endTime == null && task.endDate == null) {
            return isMatchingFloating();
        } else if (task.startTime != null) {
            return isMatchingStartEnd(task);
        } else if (task.endTime == null) {
            return isMatchingEndDateOnly(task);
        } else {
            return isMatchingEndDateTime(task);
        }
        
    }
}
