package data;

import java.util.Date;

import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;
import data.taskinfo.Time;

/**
 * 
 * @author Oh
 */
public class TaskData {
    
    public int next(int index) {
        return 0;
    }
    
    public int previous(int index) {
        return 0;
    }


    public String getTaskName (int index) {
        return null;
    }
    
    public Time getTaskStartTime (int index) {
        return null;
    }
    
    public Time getTaskEndTime (int index) {
        return null;
    }
    
    public Date getTaskDate (int index) {
        return null;
    }
    
    public String getTaskDetails (int index) {
        return null;
    }
    
    public Priority getTaskPriority (int index) {
        return null;
    }
    
    public boolean getTaskDone (int index) {
        return false;
    }

    public Tag[] getTaskTags (int index) {
        return null;
    }
    
    public void addTag(int index, Tag tag) {
    }
    
    public void removeTag(int index, Tag tag) {
    }
    
    public void clearTags(int index) {
    }

    public TaskInfo getTaskInfo(int index) {
        return null;
    }



    void updateTaskList(Task[] tasks) {
    }

    
}
