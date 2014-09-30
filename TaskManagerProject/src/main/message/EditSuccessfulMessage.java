package main.message;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class EditSuccessfulMessage implements Message{
    
    public enum Field {
        NAME,
        TIME,
        PRIORITY,
        TAGS_ADD,
        TAGS_DELETE,
    }
    
    Field changedField;
    private TaskInfo task;
    private TaskId taskId;
    
    public EditSuccessfulMessage(TaskInfo task, TaskId taskId, Field changedField) {
        this.taskId = taskId;
        this.task = task;
        this.changedField = changedField;
    }
    
    public Type getType() {
        return Type.EDIT_SUCCESSFUL;
    }
    
    public Field getChangedField() {
        return changedField;
    }
    
    public TaskInfo getTask() {
        return task;
    }
    
    public TaskId getTaskId() {
        return taskId;
    }
}
