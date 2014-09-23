package data.taskinfo;

import java.util.Arrays;
import java.util.Date;

public class TaskInfo {

    public String name;
    public Time startTime;
    public Time endTime;
    public Date date;
    public String details;
    public Tag[] tags;
    public Priority priority;
    public Status status;
    
    public int numberOfTimes;
    public int repeatIntervalDays;

    public static final int REPEAT_INDEFINITELY = -1;
    
    public TaskInfo() {
    }
    
    /**
     * Copy constructor
     * @param taskInfo taskInfo to copy.
     */
    public TaskInfo(TaskInfo taskInfo) {
        this.name = taskInfo.name;
        this.startTime = taskInfo.startTime;
        this.endTime = taskInfo.endTime;
        this.date = taskInfo.date;
        this.details = taskInfo.details;
        
        if (taskInfo.tags != null)
            this.tags = Arrays.copyOf(taskInfo.tags, taskInfo.tags.length);
        
        this.priority = taskInfo.priority;
        this.status = taskInfo.status;
        this.numberOfTimes = taskInfo.numberOfTimes;
        this.repeatIntervalDays = taskInfo.repeatIntervalDays;
    }
    
}
