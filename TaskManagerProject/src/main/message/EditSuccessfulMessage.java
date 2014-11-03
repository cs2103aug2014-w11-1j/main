package main.message;

import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0119432L
public class EditSuccessfulMessage implements Message{
    
    public enum Field {
        NAME,
        TIME,
        STATUS,
        DETAILS,
        PRIORITY,
        TAGS_ADD,
        TAGS_DELETE,
    }
    
    Field[] changedField;
    private TaskInfo[] tasks;
    private TaskId[] taskIds;
    
    public EditSuccessfulMessage(TaskInfo[] tasks, TaskId[] taskIds, Field[] changedField) {
        this.taskIds = taskIds;
        this.tasks = tasks;
        this.changedField = changedField;
    }
    
    public Type getType() {
        return Type.EDIT_SUCCESSFUL;
    }
    
    public Field[] getChangedField() {
        return changedField;
    }
    
    public TaskInfo[] getTask() {
        return tasks;
    }
    
    public TaskId[] getTaskId() {
        return taskIds;
    }
}
