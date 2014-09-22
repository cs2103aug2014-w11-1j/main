package data.taskinfo;

import java.util.Date;

public class TaskInfo {
    String name;
    Time startTime;
    Time endTime;
    Date date;
    String details;
    Tag[] tags;
    Priority priority;
    Status status;
    
    int numberOfTimes;
    int repeatIntervalDays;
    
    public static final int REPEAT_INDEFINITELY = -1;
}
