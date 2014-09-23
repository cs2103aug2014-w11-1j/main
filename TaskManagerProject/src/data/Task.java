package data;
import java.util.Arrays;
import java.util.Date;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;
import data.taskinfo.Time;

public class Task {
    private TaskInfo taskInfo;
    private int id;
    
    public Task (TaskInfo taskInfo) {
        this.taskInfo = new TaskInfo(taskInfo);
    }

    public TaskInfo getTaskInfo() {
        return new TaskInfo(taskInfo);
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return taskInfo.name;
    }
    
    public void setName(String name) {
        taskInfo.name = name;
    }
    
    public Time getStartTime() {
        return taskInfo.startTime;
    }
    
    public void setStartTime(Time startTime) {
        taskInfo.startTime = startTime;
    }
    
    public Time getEndTime() {
        return taskInfo.endTime;
    }
    
    public void setEndTime(Time endTime) {
        taskInfo.endTime = endTime;
    }
    
    public Date getDate() {
        return taskInfo.date;
    }
    
    public void setDate(Date date) {
        taskInfo.date = date;
    }
    
    public String getDetails() {
        return taskInfo.details;
    }
    
    public void setDetails(String details) {
        taskInfo.details = details;
    }
    
    public Tag[] getTags() {
        return Arrays.copyOf(taskInfo.tags, taskInfo.tags.length);
    }
    
    public void setTags(Tag[] tags) {
        taskInfo.tags = tags;
    }
    
    public Priority getPriority() {
        return taskInfo.priority;
    }
    
    public void setPriority(Priority priority) {
        taskInfo.priority = priority;
    }
    
    public Status getStatus() {
        return taskInfo.status;
    }
    
    public void setStatus(Status status) {
        taskInfo.status = status;
    }
    
    public int getNumberOfTimes() {
        return taskInfo.numberOfTimes;
    }
    
    public void setNumberOfTimes(int numberOfTimes) {
        taskInfo.numberOfTimes = numberOfTimes;
    }
    
    public int getRepeatIntervalDays() {
        return taskInfo.repeatIntervalDays;
    }
    
    public void setRepeatIntervalDays(int repeatIntervalDays) {
        taskInfo.repeatIntervalDays = repeatIntervalDays;
    }
}
