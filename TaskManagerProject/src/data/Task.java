package data;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class Task {
    private static final int NOT_FOUND = -1;
    private TaskInfo taskInfo;
    private int id;
    
    public Task (TaskInfo taskInfo) {
        this.taskInfo = new TaskInfo(taskInfo);
        initialiseFields();
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
    
    public LocalTime getStartTime() {
        throw new UnsupportedOperationException("Not implemented yet");
        //return taskInfo.endTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        throw new UnsupportedOperationException("Not implemented yet");
        //taskInfo.endTime = startTime;
    }
    
    public LocalDate getStartDate() {
        throw new UnsupportedOperationException("Not implemented yet");
        //return taskInfo.endDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        throw new UnsupportedOperationException("Not implemented yet");
        //taskInfo.endDate = startDate;
    }
    
    public LocalTime getEndTime() {
        return taskInfo.endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        taskInfo.endTime = endTime;
    }
    
    public LocalDate getEndDate() {
        return taskInfo.endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        taskInfo.endDate = endDate;
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

    public boolean addTag(Tag tag) {
        
        if (findIndexOfTag(tag) == NOT_FOUND) {
            Tag[] tags = Arrays.copyOf(taskInfo.tags, taskInfo.tags.length+1);
            tags[tags.length-1] = tag;
            taskInfo.tags = tags;
            
            return true;
        } else {
            return false;
        }
    }
    
    public boolean removeTag(Tag tag) {
        
        int result = findIndexOfTag(tag);
        if (result == NOT_FOUND)
            return false;
        
        Tag[] tags = copyTagsIntoNewArray(result);
        taskInfo.tags = tags;
        
        return true;
    }
    
    public void clearTags() {
        taskInfo.tags = new Tag[0];
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
    
    public Duration getRepeatIntervalDays() {
        return taskInfo.repeatInterval;
    }
    
    public void setRepeatIntervalDays(Duration repeatInterval) {
        taskInfo.repeatInterval = repeatInterval;
    }
    
    public void setAllInfo(TaskInfo taskInfo) {
        this.taskInfo = new TaskInfo(taskInfo);
    }

    private void initialiseFields() {
        if (taskInfo.priority == null) {
            taskInfo.priority = Priority.defaultPriority();
        }
        if (taskInfo.status == null) {
            taskInfo.status = Status.defaultStatus();
        }
        if (taskInfo.tags == null) {
            taskInfo.tags = new Tag[0];
        }
    }
    
    private int findIndexOfTag(Tag tag) {
        for (int i = 0 ; i < taskInfo.tags.length; i++) {
            if (tag.equals(taskInfo.tags[i])) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    private Tag[] copyTagsIntoNewArray(int result) {
        Tag[] tags = new Tag[taskInfo.tags.length-1];
        int index = 0;
        for (int i = 0; i < tags.length; i++) {
            if (index == result)
                index++;
            tags[i] = taskInfo.tags[index];
            index++;
        }
        return tags;
    }
    
}
