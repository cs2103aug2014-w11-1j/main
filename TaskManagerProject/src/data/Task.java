package data;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

/**
 * The Task object that is stored in the TaskData class. Not available outside
 * of TaskData.<br>
 * In contrast, a TaskInfo object is available outside of TaskData.
 */
//@author A0065475X
public class Task {
    private static final int NOT_FOUND = -1;
    private TaskInfo taskInfo;
    private int id;
    
    public Task (TaskInfo taskInfo) {
        this.taskInfo = new TaskInfo(taskInfo);
        assert taskInfo.isValid() : "Invalid taskInfo detected in TaskData!";
        initialiseFields();
    }

    public TaskInfo getTaskInfo() {
        assert taskInfo.isValid() : "Invalid taskInfo detected in TaskData!";
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
    
    public boolean setName(String name) {
        String original = taskInfo.name;
        
        taskInfo.name = name;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.name = original;
            return false;
        }
    }
    
    public LocalTime getStartTime() {
        return taskInfo.endTime;
    }
    
    public boolean setStartTime(LocalTime startTime) {
        LocalTime original = taskInfo.startTime;
        
        taskInfo.startTime = startTime;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.startTime = original;
            return false;
        }
    }
    
    public LocalDate getStartDate() {
        return taskInfo.endDate;
    }
    
    public boolean setStartDate(LocalDate startDate) {
        LocalDate original = taskInfo.startDate;
        
        taskInfo.startDate = startDate;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.startDate = original;
            return false;
        }
    }
    
    public LocalTime getEndTime() {
        return taskInfo.endTime;
    }
    
    public boolean setEndTime(LocalTime endTime) {
        LocalTime original = taskInfo.endTime;
        
        taskInfo.endTime = endTime;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.endTime = original;
            return false;
        }
    }
    
    public LocalDate getEndDate() {
        return taskInfo.endDate;
    }
    
    public boolean setEndDate(LocalDate endDate) {
        LocalDate original = taskInfo.endDate;
        
        taskInfo.endDate = endDate;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.endDate = original;
            return false;
        }
    }
    
    public String getDetails() {
        return taskInfo.details;
    }
    
    public boolean setDetails(String details) {
        String original = taskInfo.details;
        
        taskInfo.details = details;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.details = original;
            return false;
        }
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
    
    public boolean setPriority(Priority priority) {
        Priority original = taskInfo.priority;
        
        taskInfo.priority = priority;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.priority = original;
            return false;
        }
    }
    
    public Status getStatus() {
        return taskInfo.status;
    }
    
    public boolean setStatus(Status status) {
        Status original = taskInfo.status;
        
        taskInfo.status = status;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.status = original;
            return false;
        }
    }
    
    public int getNumberOfTimes() {
        return taskInfo.numberOfTimes;
    }
    
    public boolean setNumberOfTimes(int numberOfTimes) {
        int original = taskInfo.numberOfTimes;
        
        taskInfo.numberOfTimes = numberOfTimes;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.numberOfTimes = original;
            return false;
        }
    }
    
    public Duration getRepeatIntervalDays() {
        return taskInfo.repeatInterval;
    }
    
    public boolean setRepeatIntervalDays(Duration repeatInterval) {
        Duration original = taskInfo.repeatInterval;
        
        taskInfo.repeatInterval = repeatInterval;
        if (taskInfo.isValid()) {
            return true;
        } else {
            taskInfo.repeatInterval = original;
            return false;
        }
    }
    
    public boolean setAllInfo(TaskInfo taskInfo) {
        if (taskInfo.isValid()) {
            this.taskInfo = new TaskInfo(taskInfo);
            return true;
        } else {
            return false;
        }
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
